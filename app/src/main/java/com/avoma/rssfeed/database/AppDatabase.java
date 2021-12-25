package com.avoma.rssfeed.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.avoma.rssfeed.model.Item;

@Database(entities = {Item.class}, version = 1)
@TypeConverters({DataConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract DataDao getDataDAO();
}

