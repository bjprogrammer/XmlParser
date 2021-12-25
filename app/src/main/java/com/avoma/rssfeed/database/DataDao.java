package com.avoma.rssfeed.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.avoma.rssfeed.model.Item;

import java.util.List;

import io.reactivex.rxjava3.core.Maybe;

@Dao
public abstract class DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insert(List<Item> data);

    @Query("SELECT * FROM  feed")
    public abstract Maybe<List<Item>> getFeeds();

    @Query("UPDATE feed SET isBookmarked = :flag WHERE id = :id")
    public abstract Maybe<Void> editNote(Boolean flag, int id);
}

