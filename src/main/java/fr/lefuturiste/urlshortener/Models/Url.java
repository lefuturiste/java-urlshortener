package fr.lefuturiste.urlshortener.Models;

import org.bson.Document;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Url {
    private String id;
    private String url;
    private String slug = null;
    private Date createdAt;
    private Date updatedAt = null;

    public static Url fromDocument(Document document) {
        Url url = new Url();
        url.setId(document.get("_id").toString());
        url.setSlug(document.getString("slug"));
        url.setUrl(document.getString("url"));
        try {
            SimpleDateFormat formatter = (new SimpleDateFormat("yyyy/MM/dd hh:mm:ss"));
            url.setCreatedAt(document.getString("created_at") != null ? formatter.parse(document.getString("created_at")) : null);
            url.setUpdatedAt(document.getString("updated_at") != null ? formatter.parse(document.getString("updated_at")) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return url;
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

    public Url setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Url setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Document toBsonDocument() {
        return (new Document())
                .append("url", this.url)
                .append("slug", this.slug)
                .append("created_at", this.createdAt)
                .append("updated_at", this.updatedAt);
    }

    public Url setId(String id) {
        this.id = id;
        return this;
    }

    public JSONObject toJsonObject() {
        JSONObject object = new JSONObject();
        object.put("id", this.id);
        object.put("url", this.url);
        object.put("slug", this.slug);
        object.put("created_at", this.createdAt);
        object.put("updated_at", this.updatedAt);
        return object;
    }
}
