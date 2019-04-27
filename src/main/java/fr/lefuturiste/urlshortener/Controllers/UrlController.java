package fr.lefuturiste.urlshortener.Controllers;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import fr.lefuturiste.urlshortener.Container;
import fr.lefuturiste.httpserver.Request;
import fr.lefuturiste.httpserver.Response;
import fr.lefuturiste.urlshortener.Models.Url;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import java.time.LocalDateTime;

/**
 * TODO: User input validation
 */
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
        Url url = this.getUrlManager().getOne(request.getParam("id"));
        if (url == null) {
            return this.getNotFoundResponse(response);
        }
        JSONObject responseObject =
                new JSONObject()
                        .put("success", true)
                        .put("url_id", request.getParam("id"))
                        .put("route", "url.getOne")
                        .put("data", url.toJsonObject());
        return response.setStatusCode(200).withJson(responseObject);
    }

    public Response redirect(Request request, Response response) {
        Url url = this.getUrlManager().getOneViaSlug(request.getParam("slug"));
        if (url == null) {
            return this.getNotFoundResponse(response);
        }
        return response.setStatusCode(301).putHeader("Location", url.getUrl()).setBody("\r\n\r\n");
    }

    public Response store(Request request, Response response) {
        JSONObject body = request.getJsonParsedBody();
        ObjectId objectId = this.getUrlManager().store(
                (new Url())
                    .setUrl((String) body.get("url"))
                    .setSlug((String) body.get("slug"))
                    .setCreatedAt(LocalDateTime.now())
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
        Url oldUrl = this.getUrlManager().getOne(request.getParam("id"));
        if (oldUrl == null) {
            return this.getNotFoundResponse(response);
        }
        JSONObject body = request.getJsonParsedBody();
        Url newUrl = oldUrl;
        if (body.has("slug")) newUrl.setSlug((String) body.get("slug"));
        if (body.has("url")) newUrl.setUrl((String) body.get("url"));
        if (!newUrl.equals(oldUrl)) {
            // updated
            newUrl.setUpdatedAt(LocalDateTime.now());
        }
        UpdateResult updateResult = this.getUrlManager().save(newUrl);
        JSONObject responseObject =
                new JSONObject()
                        .put("success", true)
                        .put("updated", updateResult.getMatchedCount() == 1 && updateResult.getModifiedCount() == 1 && updateResult.wasAcknowledged());
        return response.setStatusCode(200).withJson(responseObject);
    }

    public Response destroy(Request request, Response response) {
        Url url = this.getUrlManager().getOne(request.getParam("id"));
        if (url == null) {
            return this.getNotFoundResponse(response);
        }
        DeleteResult deleteResult = this.getUrlManager().destroy(url);
        JSONObject responseObject =
                new JSONObject()
                        .put("success", true)
                        .put("destroyed", deleteResult.getDeletedCount() == 1 && deleteResult.wasAcknowledged());
        return response.setStatusCode(200).withJson(responseObject);
    }

    public Response getNotFoundResponse(Response response) {
        JSONObject responseObject =
                new JSONObject()
                        .put("success", false)
                        .put("error", "Url not found");
        return response.setStatusCode(404).withJson(responseObject);
    }
}