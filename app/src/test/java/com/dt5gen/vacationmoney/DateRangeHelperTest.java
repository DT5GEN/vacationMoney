package com.dt5gen.vacationmoney;

import static org.junit.Assert.*;
import org.junit.Test;
import androidx.core.util.Pair;

import com.dt5gen.vacationmoney.utils.DateRangeHelper;

public class DateRangeHelperTest {

    @Test
    public void testGetSelectedVacationDays() {
        // Подготавливаем тестовые данные
        long startDate = 1661990400000L; // 2022-09-01 (в миллисекундах)
        long endDate = 1662336000000L; // 2022-09-05 (в миллисекундах)

        // Вызываем метод и проверяем результат
        int vacationDays = DateRangeHelper.getSelectedVacationDays(new Pair<>(startDate, endDate));
        assertEquals(5, vacationDays); // Должно быть 5 дней
    }

    @Test
    public void testGetSelectedVacationDays_SameDay() {
        // Когда даты одинаковы
        long date = 1661990400000L; // 2022-09-01 (в миллисекундах)

        // Ожидаем 1 день, так как начало и конец совпадают
        int vacationDays = DateRangeHelper.getSelectedVacationDays(new Pair<>(date, date));
        assertEquals(1, vacationDays);
    }

    @Test
    public void testGetSelectedVacationDays_NullDates() {
        // Проверяем случай, если даты не выбраны (null)
        int vacationDays = DateRangeHelper.getSelectedVacationDays(null);
        assertEquals(0, vacationDays); // Ожидаем 0 дней
    }

    @Test
    public void testFormatDate() {
        // Подготавливаем тестовую дату
        long dateInMillis = 1661990400000L; // 2022-09-01 (в миллисекундах)

        // Ожидаемая строка в формате yyyy-MM-dd
        String formattedDate = DateRangeHelper.formatDate(dateInMillis);
        assertEquals("2022-09-01", formattedDate); // Проверяем форматирование
    }
}