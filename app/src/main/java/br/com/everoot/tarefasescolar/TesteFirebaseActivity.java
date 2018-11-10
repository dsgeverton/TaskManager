package br.com.everoot.tarefasescolar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import br.com.everoot.tarefasescolar.Crud.CrudTruma;
import br.com.everoot.tarefasescolar.Model.Turma;

public class TesteFirebaseActivity extends AppCompatActivity {

    private ViewHolder viewHolder = new ViewHolder();
    // Write a message to the database
    private static FirebaseDatabase database;
    private static DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_firebase);

        initFirebase();

        viewHolder.edtNomeTurma = findViewById(R.id.edtNomeTurma);
        viewHolder.edtNumeroTurma = findViewById(R.id.edtNumeroTurma);
        viewHolder.btnSalvar = findViewById(R.id.btnSalvarTurma);
        viewHolder.btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add in firebase
                incluirTurma();
                limparCampos();
            }
        });
    }

    private void limparCampos() {
        viewHolder.edtNumeroTurma.setText("");
        viewHolder.edtNomeTurma.setText("");
    }

    private void incluirTurma() {
        Turma turma = new Turma();
        turma.setId(UUID.randomUUID().toString());
        turma.setNome(viewHolder.edtNomeTurma.getText().toString());
        turma.setNumero(viewHolder.edtNumeroTurma.getText().toString());
        turma.setIdAdmin(UUID.randomUUID().toString());
        CrudTruma crudTruma = new CrudTruma();
        crudTruma.inserirTurma(turma);
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(TesteFirebaseActivity.this);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    public class ViewHolder{
        EditText edtNomeTurma, edtNumeroTurma;
        Button btnSalvar;
    }
}
