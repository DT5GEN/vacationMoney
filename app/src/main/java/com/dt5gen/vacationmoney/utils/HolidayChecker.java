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
        addHoliday("2021-01-01"); // Новый год
        addHoliday("2021-01-02"); // Новый год
        addHoliday("2021-01-03"); // Новый год
        addHoliday("2021-01-04"); // Новый год
        addHoliday("2021-01-05"); // Новый год
        addHoliday("2021-01-06"); // Новый год
        addHoliday("2021-01-07"); // Рождество Х
        addHoliday("2021-01-08"); // Новый год
        addHoliday("2021-02-23"); // День защитника Отечеств
        addHoliday("2021-03-08"); // Международной женский день
        addHoliday("2021-05-01"); // Праздник Весны и Труда
        addHoliday("2021-05-09"); // День Победы
        addHoliday("2021-06-12"); // День России
        addHoliday("2021-11-04"); // День народного единства
        addHoliday("2022-01-01"); // Новый год
        addHoliday("2022-01-02"); // Новый год
        addHoliday("2022-01-03"); // Новый год
        addHoliday("2022-01-04"); // Новый год
        addHoliday("2022-01-05"); // Новый год
        addHoliday("2022-01-06"); // Новый год
        addHoliday("2022-01-07"); // Рождество Х
        addHoliday("2022-01-08"); // Новый год
        addHoliday("2022-02-23"); // День защитника Отечеств
        addHoliday("2022-03-08"); // Международной женский день
        addHoliday("2022-05-01"); // Праздник Весны и Труда
        addHoliday("2022-05-09"); // День Победы
        addHoliday("2022-06-12"); // День России
        addHoliday("2022-11-04"); // День народного единства
        addHoliday("2023-01-01"); // Новый год
        addHoliday("2023-01-02"); // Новый год
        addHoliday("2023-01-03"); // Новый год
        addHoliday("2023-01-04"); // Новый год
        addHoliday("2023-01-05"); // Новый год
        addHoliday("2023-01-06"); // Новый год
        addHoliday("2023-01-07"); // Рождество Х
        addHoliday("2023-01-08"); // Новый год
        addHoliday("2023-02-23"); // День защитника Отечеств
        addHoliday("2023-03-08"); // Международной женский день
        addHoliday("2023-05-01"); // Праздник Весны и Труда
        addHoliday("2023-05-09"); // День Победы
        addHoliday("2023-06-12"); // День России
        addHoliday("2023-11-04"); // День народного единства
        addHoliday("2024-01-01"); // Новый год
        addHoliday("2024-01-02"); // Новый год
        addHoliday("2024-01-03"); // Новый год
        addHoliday("2024-01-04"); // Новый год
        addHoliday("2024-01-05"); // Новый год
        addHoliday("2024-01-06"); // Новый год
        addHoliday("2024-01-07"); // Рождество Х
        addHoliday("2024-01-08"); // Новый год
        addHoliday("2024-02-23"); // День защитника Отечеств
        addHoliday("2024-03-08"); // Международной женский день
        addHoliday("2024-05-01"); // Праздник Весны и Труда
        addHoliday("2024-05-09"); // День Победы
        addHoliday("2024-06-12"); // День России
        addHoliday("2024-11-04"); // День народного единства
        addHoliday("2025-01-01"); // Новый год
        addHoliday("2025-01-02"); // Новый год
        addHoliday("2025-01-03"); // Новый год
        addHoliday("2025-01-04"); // Новый год
        addHoliday("2025-01-05"); // Новый год
        addHoliday("2025-01-06"); // Новый год
        addHoliday("2025-01-07"); // Рождество Х
        addHoliday("2025-01-08"); // Новый год
        addHoliday("2025-02-23"); // День защитника Отечеств
        addHoliday("2025-03-08"); // Международной женский день
        addHoliday("2025-05-01"); // Праздник Весны и Труда
        addHoliday("2025-05-09"); // День Победы
        addHoliday("2025-06-12"); // День России
        addHoliday("2025-11-04"); // День народного единства
    }
    // https://www.consultant.ru/law/ref/calendar/proizvodstvennye/2021/
    // https://www.consultant.ru/law/ref/calendar/proizvodstvennye/2022/
    // https://www.consultant.ru/law/ref/calendar/proizvodstvennye/2023/
    // https://www.consultant.ru/law/ref/calendar/proizvodstvennye/2024/
    // https://www.consultant.ru/law/ref/calendar/proizvodstvennye/2025/


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