package com.dt5gen.vacationmoney;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.util.Pair;

import com.dt5gen.vacationmoney.api.VacationApi;
import com.dt5gen.vacationmoney.ui.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30)  // Указываем версию SDK
public class MainActivityTest {

    private MainActivity activity;
    private TextView resultMoneyTextView;
    private EditText salaryInput;
    private Button calculateButton;

    private VacationApi mockVacationApi;

    @Before
    public void setup() {
        // Создаем тестовую активность
        activity = Robolectric.buildActivity(MainActivity.class).create().start().resume().get();

        // Инициализируем UI-элементы
        resultMoneyTextView = activity.findViewById(R.id.resultMoneyTextView);
        salaryInput = activity.findViewById(R.id.salaryInput);
        calculateButton = activity.findViewById(R.id.calculateButton);

        // Мокаем VacationApi и создаем VacationCalculator
        mockVacationApi = mock(VacationApi.class);
    }

    @Test
    public void testCalculateVacationPay_Success() {
        // Устанавливаем данные в поля
        salaryInput.setText("1000");
        activity.selectedDates = new Pair<>(1625097600000L, 1625952000000L); // Пример выбранных дат

        // Мокаем успешный вызов API
        Call<Double> mockCall = mock(Call.class);
        when(mockVacationApi.calculateVacationPay(1000.0, 12)).thenReturn(mockCall);

        // Обрабатываем успешный ответ
        doAnswer(invocation -> {
            Callback<Double> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, Response.success(1500.0));
            return null;
        }).when(mockCall).enqueue(any(Callback.class));

        // Нажимаем кнопку расчета
        calculateButton.performClick();

        // Проверяем, что результат отображен корректно
        assertEquals("Отпускные: 1500.00 у.е.", resultMoneyTextView.getText().toString());
    }

    @Test
    public void testCalculateVacationPay_Error() {
        // Устанавливаем данные в поля
        salaryInput.setText("1000");
        activity.selectedDates = new Pair<>(1625097600000L, 1625952000000L);

        // Мокаем вызов API с ошибкой
        Call<Double> mockCall = mock(Call.class);
        when(mockVacationApi.calculateVacationPay(1000.0, 12)).thenReturn(mockCall);

        // Обрабатываем ошибочный ответ
        doAnswer(invocation -> {
            Callback<Double> callback = invocation.getArgument(0);
            callback.onFailure(mockCall, new Throwable("Ошибка сети"));
            return null;
        }).when(mockCall).enqueue(any(Callback.class));

        // Нажимаем кнопку расчета
        calculateButton.performClick();

        // Проверяем, что ошибка отображена корректно
        assertEquals("Ошибка сети: Ошибка сети", resultMoneyTextView.getText().toString());
    }

    @Test
    public void testCalculateVacationPay_NoSalary() {
        // Очищаем поле зарплаты
        salaryInput.setText("");

        // Нажимаем кнопку расчета
        calculateButton.performClick();

        // Проверяем, что отображается сообщение о необходимости ввода зарплаты
        assertEquals("Введите зарплату!", resultMoneyTextView.getText().toString());
    }

    @Test
    public void testCalculateVacationPay_NoDates() {
        // Устанавливаем зарплату, но не выбираем даты
        salaryInput.setText("1000");
        // Нажимаем кнопку расчета
        calculateButton.performClick();

        // Проверяем, что отображается сообщение о необходимости выбора дат
        assertEquals("Выберите период!", resultMoneyTextView.getText().toString());
    }
}