package com.wael.mydiary.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(tableName="entry", foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "user_id",
        onDelete = CASCADE ))
public class Entry {
    private static final String TAG = "Entry";
    @PrimaryKey(autoGenerate = true)
    private int id ;
    private String title;
    private String text;
    @ColumnInfo(name = "created_at")
    private Date createdAt;
    @ColumnInfo(name = "user_id")
    public String userId;

    public Entry(int id, String title, String text, String userId) {
        this.id = id;
        this.text = text;
        this.title = title;
        createdAt = new Date();
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
