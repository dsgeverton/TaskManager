package br.com.everoot.tarefasescolar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TesteFirebaseActivity extends AppCompatActivity {

    private ViewHolder viewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_firebase);

        viewHolder.edtNomeTurma = findViewById(R.id.edtNomeTurma);
        viewHolder.edtNumeroTurma = findViewById(R.id.edtNumeroTurma);
        viewHolder.btnSalvar = findViewById(R.id.btnSalvarTurma);

        viewHolder.btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add in firebase
            }
        });
    }

    public class ViewHolder{
        EditText edtNomeTurma, edtNumeroTurma;
        Button btnSalvar;
    }
}
