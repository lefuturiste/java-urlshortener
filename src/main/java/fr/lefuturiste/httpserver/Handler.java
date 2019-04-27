package fr.lefuturiste.httpserver;

import fr.lefuturiste.urlshortener.Container;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Handler {
    private String method;
    private String path;
    private Pattern pattern;
    private ArrayList<String> params = new ArrayList<>();
    private Method handlerMethod;
    private Class handlerClass;
    private Object controller;

    public Handler(HttpServer httpServer, String method, String path, Class controllerClass, String controllerMethod) {
        try {
            this.handlerMethod = controllerClass.getDeclaredMethod(controllerMethod, Request.class, Response.class);
            this.handlerClass = controllerClass.getClass();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            this.controller = controllerClass.getConstructor(Container.class).newInstance(httpServer.getContainer());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        this.method = method;
        this.path = path;
        String patternString = path;
        // parse path
        Matcher pathMatcher = Pattern.compile(":([a-zA-Z]+)").matcher(path);
        while (pathMatcher.find()) {
            if (pathMatcher.groupCount() == 1) {
                this.params.add(pathMatcher.group(1));
                patternString = patternString.replace(":" + pathMatcher.group(1), "([a-zA-Z\\-_0-9]+)");
            }
        }
        this.pattern = Pattern.compile(patternString);
    }

    public String getPath() {
        return this.path;
    }

    public Pattern getPattern() {
        return this.pattern;
    }

    public Response call(Request request, Response response) {
        return response;
    }

    public String getMethod() {
        return this.method;
    }

    public ArrayList<String> getParams() {
        return this.params;
    }

    public Method getHandlerMethod() {
        return this.handlerMethod;
    }

    public Object getHandlerClass() {
        return this.handlerClass;
    }

    public Object getHandlerController() {
        return this.controller;
    }
}
