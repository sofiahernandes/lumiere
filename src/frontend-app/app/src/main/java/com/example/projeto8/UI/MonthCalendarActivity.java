package com.example.projeto8.UI;

import static com.example.projeto8.UI.CalendarUtils.daysInMonthArray;
import static com.example.projeto8.UI.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import java.util.ArrayList;

public class MonthCalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private RecyclerView recyclerTasks;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> tasksParaExibir;
    private ImageView iconHome, iconCalendar, iconProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        initWidgets();
        setupTaskRecyclerView();

        // Garante que temos uma data selecionada
        if (CalendarUtils.selectedDate == null) {
            CalendarUtils.selectedDate = LocalDate.now();
        }

        setMonthView();
        setupMenuClicks();
    }

    private void initWidgets() {
        monthYearText = findViewById(R.id.monthYearTV);
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        recyclerTasks = findViewById(R.id.recyclerTasks);

        // Referências do menu (dentro do include)
        iconHome = findViewById(R.id.iconHome);
        iconCalendar = findViewById(R.id.iconCalendar);
        iconProfile = findViewById(R.id.iconProfile);

        // Marca o ícone de calendário como selecionado
        iconCalendar.setSelected(true);
    }

    private void setupTaskRecyclerView() {
        tasksParaExibir = new ArrayList<>();
        taskAdapter = new TaskAdapter(tasksParaExibir, task -> {
            // Clique no exercício da lista
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

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

        // Como o servidor está ruim, chamamos o mock toda vez que mudar o dia/mês
        carregarDadosMockados();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            CalendarUtils.selectedDate = date;
            setMonthView(); // Atualiza a visão do mês e a lista de baixo
        }
    }

    private void carregarDadosMockados() {
        tasksParaExibir.clear();
        // Simulação: se for domingo (SAB/DOM), lista vazia, senão mostra treinos
        int diaSema = CalendarUtils.selectedDate.getDayOfWeek().getValue();

        if (diaSema < 6) { // Segunda a Sexta
            tasksParaExibir.add(new Task(1L, "Exercício de Agenda A", 3, 12, "https://www.youtube.com/watch?v=aclHkVaku9U", "Descrição Mockada"));
            tasksParaExibir.add(new Task(2L, "Exercício de Agenda B", 3, 10, "", "Descrição Mockada"));
        } else {
            tasksParaExibir.add(new Task(-1L, "Final de semana! Descanso.", 0, 0, "", ""));
        }

        taskAdapter.notifyDataSetChanged();
    }

    private void setupMenuClicks() {

        // Pegamos os layouts dos botões
        View btnCalendar = findViewById(R.id.btnCalendar);
        View btnHome = findViewById(R.id.btnHome);
        View btnProfile = findViewById(R.id.btnProfile);

        // Configura o clique da Agenda
        btnCalendar.setOnClickListener(v -> {
            recyclerTasks.smoothScrollToPosition(0);
        });

        // Clique na Home (já está nela, não precisa fazer nada ou apenas scroll up)
        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });

        // Clique no Perfil
        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });

    }

    // Métodos disparados pelo onClick no XML das setinhas
    public void previousMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }
}
