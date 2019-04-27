package fr.lefuturiste.urlshortener;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import fr.lefuturiste.urlshortener.Models.Url;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import java.util.ArrayList;

public class UrlManager {
    private MongoDatabase mongoDatabase;

    public UrlManager(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    private MongoCollection getCollection() {
        return this.mongoDatabase.getCollection("urls");
    }

    public ObjectId store(Url url) {
        Document document = url.toBsonDocument();
        ObjectId objectId = ObjectId.get();
        document.put("_id", objectId);
        this.getCollection().insertOne(document);
        return objectId;
    }

    public ArrayList<JSONObject> getMany() {
        MongoCursor<Document> mongoCursor = this.getCollection().find().iterator();
        ArrayList<JSONObject> urls = new ArrayList<>();
        while (mongoCursor.hasNext()) {
            Document document = mongoCursor.next();
            urls.add(Url.fromDocument(document).toJsonObject());
        }
        return urls;
    }

    public JSONObject getOne(String id) {
        Document document = new Document();
        document.append("_id", new ObjectId(id));
        MongoCursor<Document> mongoCursor = this.getCollection().find(document).iterator();
        if (!mongoCursor.hasNext()) return null;
        return Url.fromDocument(mongoCursor.next()).toJsonObject();
    }
}
