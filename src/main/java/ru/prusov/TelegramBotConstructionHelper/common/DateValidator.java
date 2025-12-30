package ru.prusov.TelegramBotConstructionHelper.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidator {

    public static boolean isValidDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return false;
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        try {
            LocalDate.parse(dateString, dateTimeFormatter);
            return true;
        } catch (DateTimeParseException e){
            return false;
        }
    }
}
