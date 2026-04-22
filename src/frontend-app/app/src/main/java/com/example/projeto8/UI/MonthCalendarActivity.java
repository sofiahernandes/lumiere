package com.example.projeto8.UI;

import static com.example.projeto8.UI.CalendarUtils.daysInMonthArray;
import static com.example.projeto8.UI.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto8.R;
import com.example.projeto8.adapter.TaskAdapter;
import com.example.projeto8.model.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class MonthCalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private TextView monthYearText, selectedDateTV;
    private RecyclerView calendarRecyclerView;
    private RecyclerView recyclerTasks;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> tasksParaExibir;

    // Removido declarações duplicadas e não utilizadas para evitar confusão

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        if (CalendarUtils.selectedDate == null) {
            CalendarUtils.selectedDate = LocalDate.now();
        }

        if (CalendarUtils.selectedDate == null) {
            CalendarUtils.selectedDate = LocalDate.now();
        }

        initWidgets();
        setupTaskRecyclerView();
        setMonthView();
        setupMenuClicks();
    }


    private void initWidgets() {
        monthYearText = findViewById(R.id.monthYearTV);
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        recyclerTasks = findViewById(R.id.recyclerTasks);
        selectedDateTV = findViewById(R.id.selectedDateTV);
        // Referência segura para o ícone selecionado
        View menuInclude = findViewById(R.id.menu);
        if (menuInclude != null) {
            ImageView iconCalendar = menuInclude.findViewById(R.id.iconCalendar);
            if (iconCalendar != null) iconCalendar.setSelected(true);
        }
    }

    private void setupTaskRecyclerView() {
        tasksParaExibir = new ArrayList<>();
        taskAdapter = new TaskAdapter(tasksParaExibir, task -> {
            Intent intent = new Intent(MonthCalendarActivity.this, ExercisesActivity.class);
            intent.putExtra("EXERCISE_TITLE", task.getTitle());
            intent.putExtra("EXERCISE_MEDIA_URL", task.getMidiaURL());
            intent.putExtra("EXERCISE_DESC", task.getDescription());
            startActivity(intent);
        });
        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerTasks.setAdapter(taskAdapter);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this, R.drawable.circle_selected);
        // IMPORTANTE: Usar "this" em vez de getApplicationContext() para evitar crashes de layout
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

        carregarDadosMockados();
        updateSelectedDateText();
    }

    public void setupMenuClicks() {
        // Pegamos os layouts dos botões
        View btnCalendar = findViewById(R.id.btnCalendar);
        View btnHome = findViewById(R.id.btnHome);
        View btnProfile = findViewById(R.id.btnProfile);

        // Configura o clique da Agenda
        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            overridePendingTransition(0, 0);
        });

        // Clique na Home (já está nela, não precisa fazer nada ou apenas scroll up)
        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(0, 0);
        });

        // Clique no Perfil
        btnCalendar.setOnClickListener(v -> {
        });
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            CalendarUtils.selectedDate = date;
            setMonthView();
            updateSelectedDateText();
        }
    }

    private void updateSelectedDateText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", new Locale("pt", "BR"));
        String formatted = CalendarUtils.selectedDate.format(formatter);
        // Deixar a primeira letra maiúscula
        selectedDateTV.setText(formatted.substring(0, 1).toUpperCase() + formatted.substring(1));
    }

    private void carregarDadosMockados() {
        tasksParaExibir.clear();
        int diaSema = CalendarUtils.selectedDate.getDayOfWeek().getValue();
        if (diaSema < 6) {
            tasksParaExibir.add(new Task(1L, "Exercício de Agenda A", 3, 12, "https://www.youtube.com/watch?v=aclHkVaku9U", "Descrição Mockada"));
        } else {
            tasksParaExibir.add(new Task(-1L, "Final de semana! Descanso.", 0, 0, "", ""));
        }
        taskAdapter.notifyDataSetChanged();
    }

    public void previousMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }
}