package br.com.everoot.tarefasescolar.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.everoot.tarefasescolar.Model.Turma;
import br.com.everoot.tarefasescolar.Model.Usuario;
import br.com.everoot.tarefasescolar.R;

public class IngressActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private ViewHolder mViewHolder= new ViewHolder();
    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingress);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("turmas");

        mViewHolder.ir = findViewById(R.id.btnIr);
        mViewHolder.criarTurma = findViewById(R.id.btnCriarTurma);
        mViewHolder.turmaID = findViewById(R.id.edtTurmaID);

        mViewHolder.criarTurma.setOnClickListener(this);
        mViewHolder.ir.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        Log.e("CREATE", "USER FIREBASE:   ############    "+firebaseUser.getDisplayName());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pricipal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.me:
                Intent intent = new Intent(IngressActivity.this, UserDataActivity.class);
                startActivity(intent);
                return true;
            case R.id.help:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firebaseUser == null){
            Log.e("RESUME", "USER FIREBASE:   ############    "+firebaseUser.getDisplayName());
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnIr:
                if (mViewHolder.turmaID.getText().toString().equals("")){
                    Toast.makeText(IngressActivity.this, "Não encontrei nenhum ID...", Toast.LENGTH_SHORT).show();
                } else {
                    verificarTurma(mViewHolder.turmaID.getText().toString());
                }
                break;

            case R.id.btnCriarTurma:

                break;
        }
    }

    private void verificarTurma(final String turmaID) {
        if (myRef == null){
            Toast.makeText(IngressActivity.this, "Database not found", Toast.LENGTH_SHORT).show();
        } else{
            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Turma turma = dataSnapshot.child(turmaID).getValue(Turma.class);
                    if (turma==null){
                        Toast.makeText(IngressActivity.this, "Turma não encontrada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(IngressActivity.this, "Turma encontrada", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "Value is: " + turma.getNome());

                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("TAG", "Failed to read value.", error.toException());
                }
            });
        }
    }

    private class ViewHolder {
        Button ir, criarTurma;
        EditText turmaID;
    }
}
