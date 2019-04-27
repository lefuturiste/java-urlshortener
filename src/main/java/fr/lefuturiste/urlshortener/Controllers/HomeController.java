package fr.lefuturiste.urlshortener.Controllers;

import fr.lefuturiste.urlshortener.Container;
import fr.lefuturiste.httpserver.Request;
import fr.lefuturiste.httpserver.Response;
import org.json.JSONObject;

public class HomeController extends Controller {
    public HomeController(Container container) {
        super(container);
    }

    public Response index(Request request, Response response) {
        JSONObject responseObject =
                new JSONObject()
                        .put("success", true)
                        .put("name", "urlshortener")
                        .put("author", "lefuturiste")
                        .put("version", "v0.0.1");
        return response.setStatusCode(200).withJson(responseObject);
    }
}
