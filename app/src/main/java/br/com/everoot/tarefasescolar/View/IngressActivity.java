package br.com.everoot.tarefasescolar.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import br.com.everoot.tarefasescolar.Model.Turma;
import br.com.everoot.tarefasescolar.Model.Usuario;
import br.com.everoot.tarefasescolar.R;

public class IngressActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private ViewHolder mViewHolder= new ViewHolder();
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Intent intent;
    private Usuario usuario;

    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingress);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("turmas");
        reference = database.getReference();

        mViewHolder.ir = findViewById(R.id.btnIr);
        mViewHolder.criarTurma = findViewById(R.id.btnCriarTurma);
        mViewHolder.turmaID = findViewById(R.id.edtTurmaID);

        mViewHolder.criarTurma.setOnClickListener(this);
        mViewHolder.ir.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        Log.e("CREATE", "USER FIREBASE:   ############    "+ currentUser.getDisplayName());

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
                intent = new Intent(IngressActivity.this, UserDataActivity.class);
                startActivity(intent);
                return true;
            case R.id.help:
                intent = new Intent(IngressActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentUser == null){
            Log.e("RESUME", "USER FIREBASE:   ############    "+ currentUser.getDisplayName());
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
                Intent intent = new Intent(IngressActivity.this, CreateClassActivity.class);
                finish();
                startActivity(intent);
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
//                        Toast.makeText(IngressActivity.this, "Turma não encontrada", Toast.LENGTH_SHORT).show();
                        new MaterialStyledDialog.Builder(IngressActivity.this)
                                .setTitle("Turma não encontrada!\n \uD83D\uDE1E")
                                .setStyle(Style.HEADER_WITH_TITLE)
                                .setDescription("Talvez você tenha errado o ID... \uD83D\uDE1E")
                                .setPositiveText("OK")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Log.d("MaterialStyledDialogs", "Do something!");
                                    }})
                                .show();

                    } else {
//                        Toast.makeText(IngressActivity.this, "Turma encontrada", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "Value is: " + turma.getNome());
                        new MaterialStyledDialog.Builder(IngressActivity.this)
                                .setTitle("Turma encontrada!")
                                .setDescription("A turma que você está prestes a ingressar é: \n\n"+ turma.toString())
                                .setIcon(R.drawable.me)
                                .setHeaderDrawable(R.drawable.background)
//                                .withDarkerOverlay(true)
                                .setPositiveText("Entrar")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Log.d("MaterialStyledDialogs", "Do something!");
                                        ingressarUsuario();
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

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("TAG", "Failed to read value.", error.toException());
                }
            });
        }
    }

    private void ingressarUsuario() {
        reference.child("users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuario = new Usuario();
                usuario = dataSnapshot.getValue(Usuario.class);
                if (usuario != null) {
                    if (usuario.getIdTurma()==null){
                        reference.child("users").child(currentUser.getUid()).child("idTurma").setValue(mViewHolder.turmaID.getText().toString());
                        reference.child("users").child(currentUser.getUid()).child("admin").setValue(false);
                        Intent intent = new Intent(IngressActivity.this, HomeClassActivity.class);
                        finish();
                        startActivity(intent);

                    } else {
                        Toast.makeText(IngressActivity.this, "Este usuário já está vinculado a uma turma", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        Log.i("============UPDATING", "USER IDTURMA: "+ usuario.getIdTurma());

    }

    private class ViewHolder {
        Button ir, criarTurma;
        EditText turmaID;
    }
}

