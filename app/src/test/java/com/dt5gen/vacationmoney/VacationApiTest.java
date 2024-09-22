package com.dt5gen.vacationmoney;
import static org.junit.Assert.*;

import com.dt5gen.vacationmoney.api.VacationApi;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VacationApiTest {

    private MockWebServer mockWebServer;
    private VacationApi vacationApi;

    @Before
    public void setUp() throws Exception {
        // Создаем MockWebServer
        mockWebServer = new MockWebServer();

        // Запускаем сервер
        mockWebServer.start();

        // Настраиваем Retrofit с использованием MockWebServer
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/")) // Подключаемся к MockWebServer
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        vacationApi = retrofit.create(VacationApi.class);
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown(); // Останавливаем сервер после тестов
    }

    @Test
    public void testCalculateVacationPay() throws Exception {
        // Подготавливаем ответ от MockWebServer
        double mockResponseBody = 1500.00;  // Это будет ответ от API
        mockWebServer.enqueue(new MockResponse()
                .setBody(String.valueOf(mockResponseBody))
                .setResponseCode(200));  // Успешный ответ от API

        // Выполняем API-запрос
        Call<Double> call = vacationApi.calculateVacationPay(1000.0, 14);
        retrofit2.Response<Double> response = call.execute();

        // Проверяем, что запрос успешен
        assertTrue(response.isSuccessful());

        // Проверяем, что тело ответа совпадает с ожидаемым значением
        assertNotNull(response.body());
        assertEquals(mockResponseBody, response.body(), 0.0);

        // Проверяем правильность URL запроса
        String expectedPath = "/calculate?averageSalary=1000.0&vacationDays=14";
        assertEquals(expectedPath, mockWebServer.takeRequest().getPath());
    }
}