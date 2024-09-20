package com.dt5gen.vacationmoney;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import java.util.Calendar;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.resultTextView);
        Button datePickerButton = findViewById(R.id.datePickerButton);

        // Настройка кнопки выбора дат
        datePickerButton.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        // Получаем текущее время
        Calendar calendar = Calendar.getInstance();

        // Устанавливаем диапазон от 3 лет назад до 2 лет вперед
        long today = calendar.getTimeInMillis();

        // 3 года назад
        calendar.add(Calendar.YEAR, -3);
        long startDate = calendar.getTimeInMillis();

        // Возвращаем текущее время
        calendar.setTimeInMillis(today);

        // 2 года вперед
        calendar.add(Calendar.YEAR, 2);
        long endDate = calendar.getTimeInMillis();

        // Настройка ограничения для календаря (от 3 лет назад до 2 лет вперёд)
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setStart(startDate);
        constraintsBuilder.setEnd(endDate);
        // Убираем DateValidatorPointForward.now(), чтобы разрешить выбор дат в прошлом
        // constraintsBuilder.setValidator(DateValidatorPointForward.now());

        // Создание Date Range Picker
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Выберите период");
        builder.setCalendarConstraints(constraintsBuilder.build());
        MaterialDatePicker<?> datePicker = builder.build();

        // Обработка выбранных дат
        datePicker.addOnPositiveButtonClickListener(selection -> {
            resultTextView.setText(datePicker.getHeaderText());
        });

        // Показываем DatePicker
        datePicker.show(getSupportFragmentManager(), datePicker.toString());
    }
}


