package fr.lefuturiste.urlshortener.Models;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Url {
    private String id;
    private String url;
    private String slug = null;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt = null;

    public static Url fromDocument(Document document) {
        Url url = new Url();
        url.setId(document.get("_id").toString());
        url.setSlug(document.getString("slug"));
        url.setUrl(document.getString("url"));
        url.setCreatedAt(Url.parseDate(document.get("created_at")));
        url.setUpdatedAt(Url.parseDate(document.get("updated_at")));

        return url;
    }

    private static LocalDateTime parseDate(Object date) {
        if (date != null) {
            return ((Date) date).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public Url setUrl(String url) {
        this.url = url;
        return this;
    }

    public Url setSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public Url setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Url setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Url setId(String id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return this.url;
    }

    public Document toBsonDocument() {
        return (new Document())
                .append("url", this.url)
                .append("slug", this.slug)
                .append("created_at", this.createdAt)
                .append("updated_at", this.updatedAt);
    }

    public Document toBsonFilterDocument() {
        Document document = new Document();
        document.append("_id", new ObjectId(this.id));
        return document;
    }

    public JSONObject toJsonObject() {
        JSONObject object = new JSONObject();
        object.put("id", this.id);
        object.put("url", this.url);
        object.put("slug", this.slug);
        if (this.createdAt != null)
            object.put("created_at", this.createdAt);
        if (this.updatedAt != null)
            object.put("updated_at", this.updatedAt);
        return object;
    }
}
