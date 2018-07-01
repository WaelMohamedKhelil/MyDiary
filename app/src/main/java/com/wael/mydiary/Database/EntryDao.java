package com.wael.mydiary.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface EntryDao {

    @Query("SELECT * FROM entry WHERE user_id = :userId ORDER BY created_at DESC ")
    List<Entry> loadAllEntriesByClient(String userId);
    @Query("SELECT * FROM entry ORDER BY created_at DESC ")
    List<Entry> loadAllEntries();

    @Insert
    void insertEntry(Entry entry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEntry(Entry entry);

    @Delete
    void deleteEntry(Entry entry);

    @Query("SELECT * FROM entry WHERE user_id = :userId AND id = :id ")
    Entry loadEntryById(String userId, int id);
}
