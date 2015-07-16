package com.ronyhe.movies.app.utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/** A utility that enables using the for-each language construct with a JSONArray */
public final class JsonIterable implements Iterator<JSONObject>, Iterable<JSONObject> {

    private final JSONArray array;
    private final int maxIndex;

    private int index = -1;

    public JsonIterable(JSONArray array) {
        this.array = array;
        maxIndex = array.length() - 1;
    }

    @Override
    public boolean hasNext() {
        return index < maxIndex;
    }

    @Override
    public JSONObject next() {
        try {
            return array.getJSONObject(++index);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<JSONObject> iterator() {
        return this;
    }
}
