package br.com.everoot.tarefasescolar.View;

import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.everoot.tarefasescolar.R;

public class CreateTaskActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private String dataFormatada, problemas = "OPS!\n\n";
    private int hora, minuto;
    private SimpleDateFormat formataData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        formataData = new SimpleDateFormat("dd/MM/yyyy");

        mViewHolder.calendarView = findViewById(R.id.calendarView);
        mViewHolder.descricao = findViewById(R.id.editTextDescricaoTarefa);
        mViewHolder.local = findViewById(R.id.editTextLocalTarefa);
        mViewHolder.hora = findViewById(R.id.editTextHoraTarefa);
        mViewHolder.salvarTarefa = findViewById(R.id.buttonSalvarTarefa);

        mViewHolder.hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateTaskActivity.this, CreateTaskActivity.this, 00,00,true);
                timePickerDialog.show();
            }
        });

        mViewHolder.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                dataFormatada = i2+"/"+(i1 + 1)+"/"+i;
            }
        });

        mViewHolder.salvarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!vidalidarData() || !validarCampos()){
                    Snackbar snackbar =  Snackbar.make(view, problemas, Snackbar.LENGTH_LONG);
                    ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setSingleLine(false);
                    snackbar.show();
                    problemas = "OPS!\n\n";
                } else {
                }
            }
        });
    }

    private boolean validarCampos() {

        if (mViewHolder.descricao.getText().toString().equals("") || mViewHolder.local.getText().toString().equals("") || mViewHolder.hora.getText().toString().equals("")){
            problemas += "Existem campos em branco!";
            Log.i("--- DATAS ---", "-----------------------------------------ENTROUUUUUUUUUUUUUUUUU IF");
            return false;
        }
            Log.i("--- DATAS ---", "------ENTROUUUUUUUUUUUUUUUUU ELSE\n"+mViewHolder.descricao.getText()+mViewHolder.local.getText()+mViewHolder.hora.getText());
        return true;
    }

    private boolean vidalidarData() {

        if (dataFormatada!=null){

        Calendar currentCalendar = Calendar.getInstance();
        Calendar calendarSelected = Calendar.getInstance();

        Date dataAtual = new Date();
        String dataAtualFormatada = formataData.format(dataAtual);

        dataAtual = null;
        try {
            dataAtual = formataData.parse(dataAtualFormatada);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date date2 = null;
        try {
            date2 = formataData.parse(dataFormatada);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        currentCalendar.setTime(dataAtual);
        calendarSelected.setTime(date2);

        Log.i("--- DATAS ---", "DATA ATUAL: "+dataAtual+"\nDATA SELECIONADA: "+date2);

        if (calendarSelected.compareTo(currentCalendar) < 1) {
//            Toast.makeText(CreateTaskActivity.this, "A tarefa só pode ser agendada numa data superior à hoje!", Toast.LENGTH_SHORT).show();
            problemas += "A tarefa só pode ser agendada numa data superior à hoje!\n";
            return false;
        }
        return true;
        }
        problemas += "A tarefa só pode ser agendada numa data superior à hoje!\n";
        return false;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hora = i;
        minuto = i1;
        if (minuto < 10)
            minuto = 00;
        mViewHolder.hora.setText(hora+":"+minuto);
    }

    private class ViewHolder{
        EditText descricao, local, hora;
        CalendarView calendarView;
        Button salvarTarefa;
    }
}
