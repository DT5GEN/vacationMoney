package com.dt5gen.vacationmoney;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.dt5gen.vacationmoney.api.VacationApi;
import com.dt5gen.vacationmoney.utils.VacationCalculator;

import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VacationCalculatorTest {

    private VacationCalculator vacationCalculator;
    private VacationApi vacationApiMock;

    @Before
    public void setUp() {
        // Создаем mock объекта VacationApi
        vacationApiMock = mock(VacationApi.class);
        vacationCalculator = new VacationCalculator(vacationApiMock);
    }

    @Test
    public void testCalculateVacationPay_Success() {
        // Подготавливаем фейковый ответ для успешного расчета
        Call<Double> mockCall = mock(Call.class);
        when(vacationApiMock.calculateVacationPay(anyDouble(), anyInt())).thenReturn(mockCall);

        // Когда API вызывает enqueue, мы вызываем onResponse с успешным результатом
        doAnswer(invocation -> {
            Callback<Double> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, Response.success(1000.0));
            return null;
        }).when(mockCall).enqueue(any());

        // Используем callback для проверки результата
        vacationCalculator.calculateVacationPay(2000.0, 14, new VacationCalculator.VacationCalculationCallback() {
            @Override
            public void onSuccess(double vacationPay) {
                assertEquals(1000.0, vacationPay, 0.01);  // Проверяем успешный результат
            }

            @Override
            public void onError(String errorMessage) {
                fail("Не должно быть ошибки");  // Если есть ошибка, тест должен провалиться
            }
        });
    }

    @Test
    public void testCalculateVacationPay_Error() {
        // Подготавливаем фейковый ответ для ошибки расчета
        Call<Double> mockCall = mock(Call.class);
        when(vacationApiMock.calculateVacationPay(anyDouble(), anyInt())).thenReturn(mockCall);

        // Когда API вызывает enqueue, мы вызываем onResponse с null телом ответа
        doAnswer(invocation -> {
            Callback<Double> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, Response.success(null));
            return null;
        }).when(mockCall).enqueue(any());

        // Используем callback для проверки результата
        vacationCalculator.calculateVacationPay(2000.0, 14, new VacationCalculator.VacationCalculationCallback() {
            @Override
            public void onSuccess(double vacationPay) {
                fail("Должна быть ошибка");
            }

            @Override
            public void onError(String errorMessage) {
                assertEquals("Ошибка расчета", errorMessage);  // Проверяем, что выводится ошибка
            }
        });
    }

    @Test
    public void testCalculateVacationPay_NetworkError() {
        // Подготавливаем фейковый ответ для сетевой ошибки
        Call<Double> mockCall = mock(Call.class);
        when(vacationApiMock.calculateVacationPay(anyDouble(), anyInt())).thenReturn(mockCall);

        // Когда API вызывает enqueue, мы вызываем onFailure с ошибкой сети
        doAnswer(invocation -> {
            Callback<Double> callback = invocation.getArgument(0);
            callback.onFailure(mockCall, new Throwable("Network failure"));
            return null;
        }).when(mockCall).enqueue(any());

        // Используем callback для проверки результата
        vacationCalculator.calculateVacationPay(2000.0, 14, new VacationCalculator.VacationCalculationCallback() {
            @Override
            public void onSuccess(double vacationPay) {
                fail("Должна быть ошибка сети");
            }

            @Override
            public void onError(String errorMessage) {
                assertEquals("Ошибка сети: Network failure", errorMessage);  // Проверяем, что ошибка сети корректно обрабатывается
            }
        });
    }
}