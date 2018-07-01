package com.derrick.journalapp.pojos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.util.Date;

public class Journal {
    @NonNull
    private int id;
    @Nullable
    private String title;
    @Nullable
    private String description;

    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    private long orderID;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;

    }

    public Journal() {
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Journal(String title, String description, String date, String time, long orderID) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.orderID = orderID;
    }


    private String date;
    private String time;


    @NonNull

    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public String getTitleForList() {
        if (!Strings.isNullOrEmpty(title)) {
            return title.substring(0, 1).toUpperCase() + title.substring(1);
        } else {
            return description.substring(0, 1).toUpperCase() + description.substring(1);
        }
    }

    /**
     * checking if a journal is empty
     *
     * @return
     */
    public boolean isEmpty() {
        return Strings.isNullOrEmpty(title) &&
                Strings.isNullOrEmpty(description);
    }

    @Override
    public String toString() {
        return "Journal{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", orderID=" + orderID +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Journal journal = (Journal) o;
        return Objects.equal(id, journal.id) &&
                Objects.equal(title, journal.title) &&
                Objects.equal(description, journal.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, title, description);
    }


}
