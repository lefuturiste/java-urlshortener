package fr.lefuturiste.urlshortener.Controllers;

import fr.lefuturiste.urlshortener.Container;
import fr.lefuturiste.httpserver.Request;
import fr.lefuturiste.httpserver.Response;
import fr.lefuturiste.urlshortener.Models.Url;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.util.Date;

public class UrlController extends Controller {
    public UrlController(Container container) {
        super(container);
    }

    public Response getMany(Request request, Response response) {
        JSONObject responseObject =
                new JSONObject()
                        .put("success", true)
                        .put("route", "url.getMany")
                        .put("data", this.getUrlManager().getMany());
        return response.setStatusCode(200).withJson(responseObject);
    }

    public Response getOne(Request request, Response response) {
        JSONObject url = this.getUrlManager().getOne(request.getParam("id"));
        if (url == null) {
            JSONObject responseObject =
                    new JSONObject()
                            .put("success", false)
                            .put("error", "Document url not found");
            return response.setStatusCode(200).withJson(responseObject);
        }
        JSONObject responseObject =
                new JSONObject()
                        .put("success", true)
                        .put("url_id", request.getParam("id"))
                        .put("route", "url.getOne")
                        .put("data", url);
        return response.setStatusCode(200).withJson(responseObject);
    }

    public Response store(Request request, Response response) {
        JSONObject body = request.getJsonParsedBody();
        ObjectId objectId = this.getUrlManager().store(
                (new Url())
                    .setUrl((String) body.get("url"))
                    .setSlug((String) body.get("slug"))
                    .setCreatedAt(new Date())
        );
        JSONObject responseObject =
                new JSONObject()
                        .put("success", true)
                        .put("route", "url.store")
                        .put("id", objectId.toString())
                        .put("data", request.getJsonParsedBody());
        return response.setStatusCode(200).withJson(responseObject);
    }

    public Response update(Request request, Response response) {
        JSONObject responseObject =
                new JSONObject()
                        .put("success", true)
                        .put("mongo", this.getMongoDB().getName())
                        .put("url_id", request.getParam("id"))
                        .put("route", "url.update")
                        .put("data", true);
        return response.setStatusCode(200).withJson(responseObject);
    }

    public Response destroy(Request request, Response response) {
        JSONObject responseObject =
                new JSONObject()
                        .put("success", true)
                        .put("url_id", request.getParam("id"))
                        .put("route", "url.destroy")
                        .put("data", true);
        return response.setStatusCode(200).withJson(responseObject);
    }
}