package com.example.projeto8.UI;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto8.R;

import java.time.LocalDate;
import java.util.ArrayList;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;
    private int selectedBackgroundResource;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener, int selectedBackgroundResource)
    {
        this.days = days;
        this.onItemListener = onItemListener;
        this.selectedBackgroundResource = selectedBackgroundResource;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if (days.size() <= 7) {
            // CASO 1: MAIN ACTIVITY (Semanal)
            // Usamos um valor fixo alto para o retângulo ficar esticado para baixo
            layoutParams.height = 180;
        } else {
            // CASO 2: MONTH CALENDAR (Mensal)
            // Tentamos pegar a altura do pai, mas se for 0, usamos um valor padrão (ex: 120)
            // para os números não sumirem!
            int parentHeight = parent.getHeight();
            if (parentHeight > 0) {
                layoutParams.height = (int) (parentHeight / 6.0);
            } else {
                layoutParams.height = 120; // Valor de segurança para o mensal aparecer
            }
        }

        return new CalendarViewHolder(view, onItemListener, days);
    }




    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        LocalDate date = days.get(position);

        if (date != null) {
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
            ViewGroup.LayoutParams params = holder.dayOfMonth.getLayoutParams();

            // --- AJUSTE DE POSIÇÃO DINÂMICO ---
            if (days.size() <= 7) {
                // Caso 1: MAIN (Retângulo)
                holder.dayOfMonth.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                // Dá um espaço do topo para não colar na borda (ajuste o 20 conforme o gosto)
                holder.dayOfMonth.setPadding(0, 10, 0, 0);

                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = 250; // Altura do retângulo (aumente se quiser mais alto)
            } else {
                // Caso 2: MONTH (Círculo/Oval)
                // Força o número a ficar EXATAMENTE no meio do círculo
                holder.dayOfMonth.setGravity(Gravity.CENTER);
                // Remove qualquer padding que possa entortar o círculo
                holder.dayOfMonth.setPadding(0, 0, 0, 0);

                params.width = 110;  // Aumentei o círculo (era perto de 100)
                params.height = 110; // Mesma medida para não ficar oval
            }

            // --- LÓGICA DE CORES E BACKGROUND ---
            if (date.equals(CalendarUtils.selectedDate)) {
                holder.dayOfMonth.setBackgroundResource(selectedBackgroundResource);
                holder.dayOfMonth.setTextColor(Color.WHITE);
            } else {
                holder.dayOfMonth.setBackgroundResource(0);
                holder.dayOfMonth.setTextColor(Color.BLACK);
            }
        } else {
            holder.dayOfMonth.setText("");
        }
    }

    @Override
    public int getItemCount()
    {
        return days.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, LocalDate date);
    }
}