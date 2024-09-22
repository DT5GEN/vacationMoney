package com.dt5gen.vacationmoney;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.dt5gen.vacationmoney.utils.HolidayChecker;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HolidayCheckerTest {

    private HolidayChecker holidayChecker;
    private SimpleDateFormat sdf;

    @Before
    public void setUp() {
        holidayChecker = new HolidayChecker();
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Добавляем несколько тестовых праздничных дней
        holidayChecker.addHoliday("2023-01-01"); // Новый год
        holidayChecker.addHoliday("2023-05-01"); // День труда
    }

    @Test
    public void testAddHoliday() throws ParseException {
        // Проверяем, что добавленный праздничный день правильно распознаётся
        Date newYear = sdf.parse("2023-01-01");
        assertTrue(holidayChecker.isHoliday(newYear));

        Date nonHoliday = sdf.parse("2023-02-01");
        assertFalse(holidayChecker.isHoliday(nonHoliday));
    }

    @Test
    public void testIsHoliday() throws ParseException {
        // Проверяем случай, когда день является праздничным
        Date holiday = sdf.parse("2023-05-01");
        assertTrue(holidayChecker.isHoliday(holiday));

        // Проверяем случай, когда день не является праздничным
        Date nonHoliday = sdf.parse("2023-04-01");
        assertFalse(holidayChecker.isHoliday(nonHoliday));
    }
}