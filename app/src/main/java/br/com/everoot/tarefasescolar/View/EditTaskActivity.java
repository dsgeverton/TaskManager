package br.com.everoot.tarefasescolar.View;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
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

public class EditTaskActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, RewardedVideoAdListener  {

    private ViewHolder mViewHolder = new ViewHolder();
    private String dataFormatada, problemas = "OPS!\n\n";
    private SimpleDateFormat formataData;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Tarefa tarefa;
    private Intent getIdTarefa;

    // ADS
    private RewardedVideoAd mRewardedVideoAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_task_toolbar);
        setSupportActionBar(toolbar);

        // INICIALIZE ADS
        MobileAds.initialize(this, "ca-app-pub-1382026910941878~8813684086");

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        formataData = new SimpleDateFormat("dd/MM/yyyy");

        getIdTarefa = getIntent();

        mViewHolder.calendarView = findViewById(R.id.calendarViewEdit);
        mViewHolder.descricao = findViewById(R.id.editTextDescricaoTarefaEdit);
        mViewHolder.local = findViewById(R.id.editTextLocalTarefaEdit);
        mViewHolder.hora = findViewById(R.id.tvSetHoraEdit);
        mViewHolder.conteudo = findViewById(R.id.editTextConteudoEdit);
        mViewHolder.setHora = findViewById(R.id.buttonHoraEdit);
        mViewHolder.salvarTarefa = findViewById(R.id.buttonSalvarTarefaEdit);

        mViewHolder.setHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditTaskActivity.this, EditTaskActivity.this, 00,00,true);
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

        carregarDados();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edittask_options, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_deletar:
                // DELETAR TAREFA DO BANCO
                databaseReference.child("tarefas").child(getIdTarefa.getStringExtra("tarefaID")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null){
                            tarefa = dataSnapshot.getValue(Tarefa.class);
                            if (tarefa != null){
                                new MaterialStyledDialog.Builder(EditTaskActivity.this)
                                        .setTitle("Atenção!")
                                        .setDescription("Você deseja realmente excluir esta tarefa?")
                                        .setIcon(R.drawable.ic_alert)
//                                        .setHeaderDrawable(R.drawable.background)
                                        .setPositiveText("Confirmar")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                Log.d("MaterialStyledDialogs", "Do something!");
                                                finish();
                                                databaseReference.child("tarefas").child(getIdTarefa.getStringExtra("tarefaID")).removeValue();
                                            }})
                                        .setNegativeText("Calcelar")
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                Log.d("MaterialStyledDialogs", "Do something!");
                                            }})
                                        .show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-1382026910941878/5146488547",
                new AdRequest.Builder().build());
    }

    private void carregarDados(){
        databaseReference.child("tarefas").child(getIdTarefa.getStringExtra("tarefaID")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    tarefa = dataSnapshot.getValue(Tarefa.class);
                    if (tarefa != null){
                        mViewHolder.descricao.setText(tarefa.getDescricao());
                        mViewHolder.conteudo.setText(tarefa.getConteudo());
                        mViewHolder.local.setText(tarefa.getLocal());
                        mViewHolder.hora.setText(tarefa.getHora());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void salvarTarefa() {

        databaseReference.child("tarefas").child(getIdTarefa.getStringExtra("tarefaID")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    tarefa = dataSnapshot.getValue(Tarefa.class);
                    if (tarefa != null){
                        tarefa.setId(tarefa.getId());
                        tarefa.setData(dataFormatada);
                        tarefa.setHora(mViewHolder.hora.getText().toString());
                        tarefa.setLocal(mViewHolder.local.getText().toString());
                        tarefa.setDescricao(mViewHolder.descricao.getText().toString());
                        tarefa.setConteudo(mViewHolder.conteudo.getText().toString());
                        tarefa.setTurmaID(getIdTarefa.getStringExtra("turmaID"));
                        databaseReference.child("tarefas").child(tarefa.getId()).setValue(tarefa);
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
