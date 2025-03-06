package uz.jtscorp.namoztime.utils;

import androidx.room.TypeConverter;

import java.sql.Time;
import java.util.Date;

public class Converters {
    @TypeConverter
    public static Time fromTimestamp(Long value) {
        return value == null ? null : new Time(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
} 