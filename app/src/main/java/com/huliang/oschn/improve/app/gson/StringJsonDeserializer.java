package com.huliang.oschn.improve.app.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.huliang.oschn.util.TLog;

import java.lang.reflect.Type;

/**
 * Created by huliang on 17/3/20.
 */
public class StringJsonDeserializer implements JsonDeserializer<String> {
    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            return json.getAsString();
        } catch (Exception e) {
            TLog.log("StringJsonDeserializer-deserialize-error:" + (json != null ?
                    json.toString() : ""));
            return null;
        }
    }
}