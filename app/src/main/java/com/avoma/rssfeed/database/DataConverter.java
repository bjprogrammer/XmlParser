package com.avoma.rssfeed.database;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import com.avoma.rssfeed.model.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

@ProvidedTypeConverter
public class DataConverter {
    @TypeConverter
    public String fromFeedsList(List<String> items) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.toJson(items, type);
    }

    @TypeConverter
    public List<String> toFeeds(String data) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(data, type);
    }
 }