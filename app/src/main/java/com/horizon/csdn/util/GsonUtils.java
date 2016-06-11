package com.horizon.csdn.util;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GsonUtils {

    public static final GsonBuilder GSON_BUILDER;

    static {
        GSON_BUILDER = new GsonBuilder();
        GSON_BUILDER.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            @SuppressLint("UseValueOf")
            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                String date = json.getAsJsonPrimitive().getAsString();
                String JSONDateToMilliseconds = "\\/(Date\\((.*?)(\\+.*)?\\))\\/";
                Pattern pattern = Pattern.compile(JSONDateToMilliseconds);
                Matcher matcher = pattern.matcher(date);
                String result = matcher.replaceAll("$2");
                try {
                    return new Date(new Long(result));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static <T> ArrayList<T> getList(String JSONString, Class<T> classOfT) {
        final ArrayList<T> data = new ArrayList<T>();

        JSONArray array = null;
        try {
            array = new JSONArray(JSONString);
        } catch (final JSONException e) {
            e.printStackTrace();
        }

        if (array != null) {
            final Gson gson = GSON_BUILDER.create();

            for (int i = 0; i < array.length(); i++) {
                try {
                    final JSONObject object = array.getJSONObject(i);
                    final T entity = gson.fromJson(object.toString(), classOfT);
                    data.add(entity);
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return data;
    }

    public static <T> T getObject(String JSONString, Class<T> classOfT) {
        final Gson gson = GSON_BUILDER.create();
        T entity = null;
        try {
            entity = gson.fromJson(JSONString, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return entity;
    }

    public static <T> T getObject(byte[] bytes, Class<T> classOfT) {
        final Gson gson = GSON_BUILDER.create();
        T entity = null;
        try {
            entity = gson.fromJson(new String(bytes, "UTF-8"), classOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return entity;
    }

    public static <T> T getObject(String JSONString, Type classOfT) {
        final Gson gson = GSON_BUILDER.create();
        T entity = null;
        try {
            entity = gson.fromJson(JSONString, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return entity;
    }

    public static String getString(Object src) {
        final Gson gson = GSON_BUILDER.create();
        String result = gson.toJson(src);
        return result;
    }
}
