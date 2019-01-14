package com.example.abulm.mobileprogramming.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "movie")
public class Movie {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private Integer id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "releaseYear")
    private String releaseYear;
    @ColumnInfo(name = "rating")
    private Double rating;

    public Movie(@NonNull Integer id, String name, String releaseYear, Double rating) {
        this.id = id;
        this.name = name;
        this.releaseYear = releaseYear;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", releaseYear='" + releaseYear + '\'' +
                ", rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Movie) {
            Movie otherMovie = (Movie) other;
            return this.getId().equals(otherMovie.getId()) && this.getName().equals(otherMovie.getName()) &&
                    this.getRating().equals(otherMovie.getRating()) && this.getReleaseYear().equals(otherMovie.getReleaseYear());
        }
        return false;
    }
}
