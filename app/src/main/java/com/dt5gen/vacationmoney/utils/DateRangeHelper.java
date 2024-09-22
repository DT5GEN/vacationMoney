package com.dt5gen.vacationmoney.utils;

import androidx.core.util.Pair;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateRangeHelper {

    // Метод для получения количества дней между выбранными датами
    public static int getSelectedVacationDays(Pair<Long, Long> selectedDates) {
        if (selectedDates != null) {
            long startMillis = selectedDates.first;
            long endMillis = selectedDates.second;
            return (int) ((endMillis - startMillis) / (1000 * 60 * 60 * 24)) + 1; // Включаем последний день
        }
        return 0;
    }

    // Метод для форматирования дат в строку
    public static String formatDate(Long dateInMillis) {
        // Используем SimpleDateFormat для форматирования дат
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(dateInMillis);
    }
}