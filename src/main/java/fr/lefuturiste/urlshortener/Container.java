package fr.lefuturiste.urlshortener;

import java.util.HashMap;
import java.util.Map;

public class Container {
    Map<String, Object> items = new HashMap<>();

    public Container set(Class key, Object value) {
        this.items.put(key.toString(), value);
        return this;
    }

    public boolean has(Class key) {
        return this.items.containsKey(key.toString());
    }

    /**
     * Return the instance of the requested class
     *
     * @param key The class to get
     * @return Object
     */
    public Object get(Class key) {
        return this.items.get(key.toString());
    }

}
