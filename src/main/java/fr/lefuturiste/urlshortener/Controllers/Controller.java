package fr.lefuturiste.urlshortener.Controllers;

import com.mongodb.client.MongoDatabase;
import fr.lefuturiste.urlshortener.Container;
import fr.lefuturiste.urlshortener.UrlManager;

public class Controller {
    protected Container container;

    public Controller (Container container) {
        this.container = container;
    }

    protected MongoDatabase getMongoDB() {
        return (MongoDatabase) this.container.get(MongoDatabase.class);
    }

    protected UrlManager getUrlManager() {
        return (UrlManager) this.container.get(UrlManager.class);
    }
}
