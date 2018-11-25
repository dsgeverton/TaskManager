package br.com.everoot.tarefasescolar.View;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import br.com.everoot.tarefasescolar.Model.Tarefa;
import br.com.everoot.tarefasescolar.Model.Turma;
import br.com.everoot.tarefasescolar.R;

public class CreateTaskActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, RewardedVideoAdListener  {

    private ViewHolder mViewHolder = new ViewHolder();
    private String dataFormatada, problemas = "OPS!\n\n";
    private int hora, minuto;
    private SimpleDateFormat formataData;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Turma turma;
    private Tarefa tarefa;
    private Intent getIdTurma;

    // ADS
    private RewardedVideoAd mRewardedVideoAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.create_task_toolbar);
        setSupportActionBar(toolbar);

        // INICIALIZE ADS
        MobileAds.initialize(this, "ca-app-pub-1382026910941878~8813684086");

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        formataData = new SimpleDateFormat("dd/MM/yyyy");

        getIdTurma = getIntent();

        mViewHolder.calendarView = findViewById(R.id.calendarView);
        mViewHolder.descricao = findViewById(R.id.editTextDescricaoTarefa);
        mViewHolder.local = findViewById(R.id.editTextLocalTarefa);
        mViewHolder.hora = findViewById(R.id.tvSetHora);
        mViewHolder.conteudo = findViewById(R.id.editTextConteudo);
        mViewHolder.setHora = findViewById(R.id.buttonHora);
        mViewHolder.salvarTarefa = findViewById(R.id.buttonSalvarTarefa);

        mViewHolder.setHora.setOnClickListener(new View.OnClickListener() {
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
                    salvarTarefa();
                }
            }
        });
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-1382026910941878/5146488547",
                new AdRequest.Builder().build());
    }

    private void salvarTarefa() {
        turma = new Turma();

        databaseReference.child("turmas").child(getIdTurma.getStringExtra("turmaID")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    turma = dataSnapshot.getValue(Turma.class);
                    if (turma != null){
                        tarefa = new Tarefa();
                        tarefa.setId(UUID.randomUUID().toString());
                        tarefa.setData(dataFormatada);
                        tarefa.setHora(mViewHolder.hora.getText().toString());
                        tarefa.setLocal(mViewHolder.local.getText().toString());
                        tarefa.setDescricao(mViewHolder.descricao.getText().toString());
                        tarefa.setConteudo(mViewHolder.conteudo.getText().toString());
                        tarefa.setTurmaID(turma.getId());
                        databaseReference.child("tarefas").child(tarefa.getId()).setValue(tarefa);
                        databaseReference.child("turmas").child(turma.getId()).child("tarefas").child(tarefa.getId()).setValue("");
                        limparCampos();
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void limparCampos() {
        mViewHolder.descricao.setText("");
        mViewHolder.local.setText("");
        mViewHolder.hora.setText("");
    }

    private boolean validarCampos() {

        if (mViewHolder.descricao.getText().toString().equals("") || mViewHolder.local.getText().toString().equals("") || mViewHolder.hora.getText().toString().equals("")
                || mViewHolder.hora.getText().toString().equals("")){
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
        mViewHolder.hora.setText(timePicker.getHour()+":"+timePicker.getMinute());
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Log.i("ADS LOADED", "------------------------- LOADED");
        mRewardedVideoAd.show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.i("ADS OPENED", "------------------------- OPENED");
    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    private class ViewHolder{
        EditText descricao, local, conteudo;
        TextView hora;
        CalendarView calendarView;
        Button salvarTarefa, setHora;
    }
}
