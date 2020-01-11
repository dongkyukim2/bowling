package com.dk.project.post.utils;

import androidx.room.TypeConverter;

import java.sql.Timestamp;


public class DateConverter {

    @TypeConverter
    public static Timestamp toDate(Long timestamp) {
        return timestamp == null ? null : new Timestamp(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Timestamp date) {
        return date == null ? null : date.getTime();
    }
}