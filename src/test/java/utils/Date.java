package utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Date {

    public static String getNextDay(){

        ZoneId zone = ZoneId.of("UTC-06:00");
        LocalDateTime date = LocalDateTime.now(zone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        return date.plusDays(1).format(formatter);
    }


}
