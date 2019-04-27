package fr.lefuturiste.urlshortener;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import fr.lefuturiste.urlshortener.Controllers.HomeController;
import fr.lefuturiste.urlshortener.Controllers.UrlController;
import fr.lefuturiste.httpserver.HttpServer;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        Container container = new Container();
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("urlshortener");
        container.set(MongoDatabase.class, mongoDatabase);
        container.set(UrlManager.class, new UrlManager(mongoDatabase));

        HttpServer httpServer = new HttpServer(8082);
        httpServer.setContainer(container);
        httpServer.addHandler("GET", "/", HomeController.class, "index");
        // urls
        httpServer.addHandler("GET", "/api/url", UrlController.class, "getMany");
        httpServer.addHandler("GET", "/api/url/:id", UrlController.class, "getOne");
        httpServer.addHandler("POST", "/api/url", UrlController.class, "store");
        httpServer.addHandler("PUT", "/api/url/:id", UrlController.class, "update");
        httpServer.addHandler("DELETE", "/api/url/:id", UrlController.class, "destroy");

        httpServer.addHandler("GET", "/:slug", UrlController.class, "redirect");

        httpServer.start();
    }
}
