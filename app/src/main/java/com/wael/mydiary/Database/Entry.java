package com.wael.mydiary.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import java.util.Date;


@Entity(tableName="entry")
public class Entry {
    private static final String TAG = "Entry";
    @PrimaryKey(autoGenerate = true)
    private int id ;
    private String title;
    private String text;
    @ColumnInfo(name = "created_at")
    private Date createdAt;

    public Entry(int id, String title, String text) {
        this.id = id;
        this.text = text;
        this.title = title;
        createdAt = new Date();
        Log.d(TAG, "Entry: new entry created id = " + this.getId() +
                " title = " + this.getTitle() + " text = " + this.getText() +
                " Date = " + this.createdAt);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}
