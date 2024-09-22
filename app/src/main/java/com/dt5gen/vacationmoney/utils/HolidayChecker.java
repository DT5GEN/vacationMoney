package com.dt5gen.vacationmoney.utils;

import android.icu.util.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HolidayChecker {

    private List<Date> holidays;

    public HolidayChecker() {
        holidays = new ArrayList<>();
        // Пример добавления праздников в формате "yyyy-MM-dd"
        addHoliday("2024-01-01"); // Новый год
        addHoliday("2024-05-01"); // День труда
        addHoliday("2024-12-25"); // Рождество
    }

    // Метод для добавления праздничных дней
    private void addHoliday(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date holiday = sdf.parse(dateString);
            holidays.add(holiday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Метод для проверки, является ли день праздничным
    public boolean isHoliday(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);

        for (Date holiday : holidays) {
            if (sdf.format(holiday).equals(dateString)) {
                return true;
            }
        }
        return false;
    }

    // Метод для подсчета праздничных дней в диапазоне
    public int countHolidaysInRange(Date startDate, Date endDate) {
        int holidayCount = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate)) {
            if (isHoliday(calendar.getTime())) {
                holidayCount++;
            }
            calendar.add(Calendar.DATE, 1); // Переходим на следующий день
        }
        return holidayCount;
    }
}