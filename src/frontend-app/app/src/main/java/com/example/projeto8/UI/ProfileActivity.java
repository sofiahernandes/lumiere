package com.example.projeto8.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto8.R;
import com.example.projeto8.api.patient.PatientService;
import com.example.projeto8.model.Patient;
import com.example.projeto8.remote.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtName, txtStatus, txtEmail, txtCpf, txtBirthDate,
            txtGender, txtHeight, txtWeight;
    private LinearLayout btnExcluir;

    private View btnCalendar, btnHome, btnProfile;
    private View containerCalendar, containerHome, containerProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        initWidgets();
        setupMenuClicks();
    }

    private void initWidgets() {

        txtName = findViewById(R.id.txtName);
        txtStatus = findViewById(R.id.txtStatus);
        txtEmail = findViewById(R.id.txtEmail);
        txtCpf = findViewById(R.id.txtCpf);
        txtBirthDate = findViewById(R.id.txtBirthDate);
        txtGender = findViewById(R.id.txtGender);
        txtHeight = findViewById(R.id.txtHeight);
        txtWeight = findViewById(R.id.txtWeight);
        btnExcluir = findViewById(R.id.btnExcluirConta);

        View menuInclude = findViewById(R.id.menu);
        if (menuInclude != null) {
            btnHome = menuInclude.findViewById(R.id.btnHome);
            btnCalendar = menuInclude.findViewById(R.id.btnCalendar);
            btnProfile = menuInclude.findViewById(R.id.btnProfile);

            containerHome = menuInclude.findViewById(R.id.containerHomeSelect);
            containerCalendar = menuInclude.findViewById(R.id.containerCalendarSelect);
            containerProfile = menuInclude.findViewById(R.id.containerProfileSelect);

            if (btnProfile != null) {
                btnProfile.setSelected(true);
                if (containerProfile != null) {
                    containerProfile.setBackgroundResource(R.drawable.selected_item_bg);
                }
            }
        }


        btnExcluir.setOnClickListener(v -> showDeleteDialog());


        SharedPreferences prefs = getSharedPreferences("STORAGE", MODE_PRIVATE);
        String name = prefs.getString("patient_name", null);
        if (name != null) {
            loadPatient(name);
        }

    }

    public void setupMenuClicks() {
        // Clique no Perfil (Já está nele)
        btnProfile.setOnClickListener(v -> {
            updateMenuSelection(btnProfile, containerProfile, btnHome, containerHome, btnCalendar, containerCalendar);
        });

        // Clique na Home
        btnHome.setOnClickListener(v -> {
            updateMenuSelection(btnHome, containerHome, btnProfile, containerProfile, btnCalendar, containerCalendar);
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });

        // Clique na Agenda
        btnCalendar.setOnClickListener(v -> {
            updateMenuSelection(btnCalendar, containerCalendar, btnHome, containerHome, btnProfile, containerProfile);
            startActivity(new Intent(this, MonthCalendarActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
    }

    private void updateMenuSelection(View selectedBtn, View selectedContainer, View... others) {
        for (View view : others) {
            if (view != null) {
                view.setSelected(false);
                // Limpa o fundo dos containers inativos
                if (view == containerHome || view == containerCalendar || view == containerProfile) {
                    view.setBackground(null);
                }
            }
        }

        if (selectedBtn != null) selectedBtn.setSelected(true);
        if (selectedContainer != null) {
            selectedContainer.setBackgroundResource(R.drawable.selected_item_bg);
        }
    }

    private void showDeleteDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_delete, null);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(view);

        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        view.findViewById(R.id.btnCancel).setOnClickListener(v1 -> dialog.dismiss());
        view.findViewById(R.id.btnDelete).setOnClickListener(v1 -> {
            // Lógica de deletar aqui
            dialog.dismiss();
        });

        dialog.show();
        view.findViewById(R.id.cardContent).setClipToOutline(true);
    }

    private void loadPatient(String name) {
        PatientService service = RetrofitClient.getPatientService();
        service.getPatientByFullName(name, null).enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Patient p = response.body().get(0);
                    txtName.setText(p.getName() + " " + p.getSurname());
                    txtStatus.setText("Status: " + p.getStatus());
                    txtEmail.setText(p.getEmail());
                    txtCpf.setText(p.getCpf());
                    txtBirthDate.setText(p.getBirthDate());
                    txtGender.setText(p.getGender());
                    txtHeight.setText(p.getHeight());
                    txtWeight.setText(p.getWeight());
                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                Log.e("API_ERROR", "Erro: " + t.getMessage());
            }
        });
    }
}