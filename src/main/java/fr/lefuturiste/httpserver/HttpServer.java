package fr.lefuturiste.httpserver;

import fr.lefuturiste.urlshortener.Container;
import org.json.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.regex.Matcher;

public class HttpServer {
    private static final int defaultPort = 80;
    private static final String defaultListenInterface = "all";

    private String listenInterface;
    private int port;
    private ServerSocket serverSocket;
    private ArrayList<Handler> handlers = new ArrayList<Handler>();
    private Container container;

    public HttpServer() {
        this(HttpServer.defaultPort);
    }

    public HttpServer(int port) {
        this(port, HttpServer.defaultListenInterface);
    }

    public HttpServer(int port, String listenInterface) {
        this.port = port;
        this.listenInterface = listenInterface;
    }

    public void start() throws IOException {
        InetAddress inetAddress = null;
        switch (this.listenInterface) {
            case "loopback": inetAddress = InetAddress.getLoopbackAddress(); break;
            case "localhost": inetAddress = InetAddress.getLocalHost(); break;
        }
        this.serverSocket = new ServerSocket(this.port, 0, inetAddress);
        do {
            Socket socket = this.serverSocket.accept();
            new ServerThread(socket, this.handlers).start();
        } while (true);
    }

    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public HttpServer addHandler(String method, String path, Class controllerClass, String controllerMethod) {
        Handler handler = new Handler(this, method, path, controllerClass, controllerMethod);
        this.handlers.add(handler);
        return this;
    }

    public HttpServer setContainer(Container container) {
        this.container = container;
        return this;
    }

    public Container getContainer() {
        return this.container;
    }

    public static class ServerThread extends Thread {
        private Socket socket;
        private List<Handler> handlers;

        public ServerThread(Socket socket, List<Handler> handlers) {
            this.socket = socket;
            this.handlers = handlers;
        }

        private String capitalizeHeaderKey(String headerKey) {
            char[] headerKeyCharArray = headerKey.toCharArray();
            StringBuilder resultBuild = new StringBuilder();
            for (int i = 0; i < headerKeyCharArray.length; i++) {
                char headerKeyChar = headerKeyCharArray[i];
                if (headerKeyChar == '-') {
                    resultBuild.append('-');
                } else {
                    if (i == 0 || headerKeyCharArray[i - 1] == '-') {
                        resultBuild.append(Character.toUpperCase(headerKeyChar));
                    } else {
                        resultBuild.append(Character.toLowerCase(headerKeyChar));
                    }
                }
            }
            return resultBuild.toString();
        }

        public void run() {
            try {
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                Request request = new Request();
                String requestLine = reader.readLine();
                while (requestLine != null) {
                    String[] headerParts = requestLine.split(":");
                    if (headerParts.length == 2) {
                        String headerValue = headerParts[1];
                        String headerKey = headerParts[0];
                        if (headerValue.startsWith(" ")) {
                            headerValue = headerValue.substring(1);
                        }
                        request.addHeader(this.capitalizeHeaderKey(headerKey), headerValue);
                    } else {
                        String[] requestLineParts = requestLine.split(" ");
                        if (requestLineParts.length == 3) {
                            request.setMethod(requestLineParts[0]);
                            request.setUrl(requestLineParts[1]);
                            request.setProtocol(requestLineParts[2]);
                        }
                    }
                    if (requestLine.isEmpty()) break;
                    requestLine = reader.readLine();
                }

                System.out.println(request.getProtocol() + " - " + request.getMethod() + " - " + request.getUrl());

                // if therese is a content length present on the request, read the body
                // TODO: verify if content length is negative or equal to 0
                if (request.getHeaders().containsKey("Content-Length")) {
                    int contentLength = Integer.parseInt(request.getHeaders().get("Content-Length"));
                    StringBuilder requestBodyBuilder = new StringBuilder();
                    // read byte per byte
                    for (int i = 0; i < contentLength; i++) {
                        requestBodyBuilder.append((char) reader.read());
                    }
                    request.setBody(requestBodyBuilder.toString());
                }
                // parse request body in function of the content body

                // parse query params
                if (request.getUrl().contains("?")) {
                    Map<String, String> queryParams = new HashMap<>();
                    String queryString = request.getUrl().substring(request.getUrl().indexOf("?"));
                    queryString = queryString.substring(1);
                    String[] queryParamsRow = queryString.split("&");
                    for (String queryParam: queryParamsRow) {
                        String[] queryParamParts = queryParam.split("=");
                        queryParams.put(queryParamParts[0], queryParamParts[1]);
                    }
                    request.setQueryParams(queryParams);
                    request.setUrl(request.getUrl().replace("?" + queryString, ""));
                }

                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                Response response = new Response();

                boolean foundHandlerUrl = false;
                boolean foundHandlerMethod = false;
                for (Handler handler : this.handlers) {
                    Matcher matcher = handler.getPattern().matcher(request.getUrl());
                    if (matcher.matches()) {
                        for (int i = 1; i <= matcher.groupCount(); i++) {
                            request.addParam(handler.getParams().get(i - 1), matcher.group(i));
                        }
                        foundHandlerUrl = true;
                        if (handler.getMethod().equals(request.getMethod())) {
                            try {
                                response = (Response) handler.getHandlerMethod().invoke(handler.getHandlerController(), request, response);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                            foundHandlerMethod = true;
                        }
                    }
                }

                if (!foundHandlerUrl) {
                    response.setStatusCode(404).withJson(new JSONObject().put("success", false).put("error", "Not Found"));
                } else if (!foundHandlerMethod) {
                    response.setStatusCode(405).withJson(new JSONObject().put("success", false).put("error", "Method not allowed"));
                }

                for (String line : response.toLines()) {
                    writer.println(line);
                }

                input.close();
                output.close();
                socket.close();
            } catch (IOException ex) {
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
