package com.example.projeto8.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto8.R;
import com.example.projeto8.api.patient.PatientDTO.PatientLoginRequestDTO;
import com.example.projeto8.api.patient.PatientDTO.PatientLoginResponseDTO;
import com.example.projeto8.api.patient.PatientService;
import com.example.projeto8.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText Email;
    EditText Password;
    TextView forgotPassword;

    TextView mensagePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        RetrofitClient.init(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_activity);

         Email = findViewById(R.id.Email);
         Password = findViewById(R.id.Password);
         forgotPassword = findViewById(R.id.forgotPassword);
         mensagePassword = findViewById(R.id.mensagePassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            patientLogIn();
        });

        forgotPassword.setOnClickListener( v -> {
            forgotPatientPassword();
        });
    }

    public void patientLogIn() {
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        //Validar se o paciente preencheu os campos
        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        PatientLoginRequestDTO loginInfo = new PatientLoginRequestDTO(email, password);
        PatientService service = RetrofitClient.getPatientService();

        service.login(loginInfo).enqueue(new Callback<PatientLoginResponseDTO>() {
            @Override
            public void onResponse(Call<PatientLoginResponseDTO> call, Response<PatientLoginResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null){
                    String tokenGerado = response.body().getToken();
                    getSharedPreferences("STORAGE", MODE_PRIVATE)
                            .edit()
                            .putString("token", tokenGerado)
                            .apply();

                    Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                   /* if (response.code() == 401) {
                        Toast.makeText(LoginActivity.this, "E-mail ou senha incorretos.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Erro no servidor: " + response.message(), Toast.LENGTH_SHORT).show();
                    } */
                        // Tenta ler o corpo do erro para saber o motivo real do 403
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("API_DEBUG", errorBody);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (response.code() == 403) {
                            //Toast.makeText(LoginActivity.this, "Acesso Negado (403). Verifique as permissões do servidor.", Toast.LENGTH_LONG).show();
                            Log.e("API_DEBUG", "Código: " + response.code());
                            Log.e("API_DEBUG", "Mensagem: " + response.message());
                        }
                }
            }

            @Override
            public void onFailure(Call<PatientLoginResponseDTO> call, Throwable t) {
                Log.e("API_ERROR", "Falha na conexão: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Não foi possível conectar ao servidor.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void forgotPatientPassword() {
        mensagePassword.setVisibility(View.VISIBLE);
    }

}

