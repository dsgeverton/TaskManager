package br.com.everoot.tarefasescolar.View;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.UUID;
import br.com.everoot.tarefasescolar.Model.Turma;
import br.com.everoot.tarefasescolar.Model.Usuario;
import br.com.everoot.tarefasescolar.R;

public class CreateClassActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewHolder mViewHolder = new ViewHolder();
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        mViewHolder.nomeTurma = findViewById(R.id.edtNomeTurma);
        mViewHolder.numeroTurma = findViewById(R.id.edtNumeroTurma);
        mViewHolder.termosdeCadastro = findViewById(R.id.checkBoxTermosdeCadastro);
        mViewHolder.cadastrarTurma = findViewById(R.id.btnCadastrarTurma);
        mViewHolder.termos = findViewById(R.id.termosDeUso);

//        Obter usuário logado
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

//        Obter referencia do banco de dados
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        mViewHolder.termos.setPaintFlags(mViewHolder.termos.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mViewHolder.termos.setOnClickListener(this);
        mViewHolder.cadastrarTurma.setOnClickListener(CreateClassActivity.this);
        mViewHolder.cadastrarTurma.setClickable(false);

        mViewHolder.termosdeCadastro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mViewHolder.termosdeCadastro.isChecked()){
//                    Toast.makeText(CreateClassActivity.this, "Checked", Toast.LENGTH_SHORT).show();
                    mViewHolder.cadastrarTurma.setClickable(true);
                } else
                    mViewHolder.cadastrarTurma.setClickable(false);
            }
        });


    }

    private boolean emptyFields() {
        //        Verificar campos em branco
        if (mViewHolder.nomeTurma.getText().toString().equals("") || mViewHolder.numeroTurma.getText().toString().equals("")){
            Toast.makeText(CreateClassActivity.this, "Não deixe campos em branco...", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCadastrarTurma:
//                Toast.makeText(CreateClassActivity.this, "Button pressed", Toast.LENGTH_SHORT).show();
                cadastrarTruma();
                break;

            case R.id.termosDeUso:
                new MaterialStyledDialog.Builder(CreateClassActivity.this)
                        .setTitle("Termos de Uso")
                        .setStyle(Style.HEADER_WITH_TITLE)
                        .setDescription("Ao criar essa Turma, dentro desta versão do aplicativo, você se dispõe a ficar responsável por manter todos os dados pertencentes à esta Turma," +
                                " sendo a seu critério organizar e disponibilizar os dados para os demais integrantes que ingressarem na mesma.")
                        .setPositiveText("OK")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Log.d("MaterialStyledDialogs", "Do something!");
                            }})
                        .show();
        }
    }

    private void cadastrarTruma() {
        if (!emptyFields()){
            usuario = new Usuario();

            reference.child("users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null){
                        usuario = dataSnapshot.getValue(Usuario.class);
                        if (usuario != null) {
                            if (usuario.getIdTurma()==null){
                                Turma turma = new Turma();
                                turma.setId(UUID.randomUUID().toString());
                                turma.setTarefas(null);
                                turma.setNome(mViewHolder.nomeTurma.getText().toString());
                                turma.setNumero(mViewHolder.numeroTurma.getText().toString());
                                turma.setIdAdmin(currentUser.getUid());
                                reference.child("turmas").child(turma.getId()).setValue(turma);
                                usuario.setIdTurma(turma.getId());
                                reference.child("users").child(currentUser.getUid()).child("idTurma").setValue(turma.getId());
                                reference.child("users").child(currentUser.getUid()).child("admin").setValue(true);
                                Intent intent = new Intent(CreateClassActivity.this, HomeClassActivity.class);
                                finish();
                                startActivity(intent);
                            } else {
                                Toast.makeText(CreateClassActivity.this, "Este usuário já está vinculado a uma turma", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Log.i("============UPDATING", "USER IDTURMA: "+ usuario.getIdTurma());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, IngressActivity.class);
        finish();
        startActivity(intent);
    }

    public class ViewHolder{

        EditText nomeTurma, numeroTurma;
        CheckBox termosdeCadastro;
        Button cadastrarTurma;
        TextView termos;
    }
}
