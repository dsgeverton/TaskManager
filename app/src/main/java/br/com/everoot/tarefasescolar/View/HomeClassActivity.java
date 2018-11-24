package br.com.everoot.tarefasescolar.View;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.everoot.tarefasescolar.Model.Tarefa;
import br.com.everoot.tarefasescolar.Model.Turma;
import br.com.everoot.tarefasescolar.Model.Usuario;
import br.com.everoot.tarefasescolar.R;

public class HomeClassActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewHolder mViewHolder = new ViewHolder();
    // Firebase user
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    // Firebase DB
    private FirebaseDatabase database;
    private DatabaseReference databaseReferenceTurma;
    private DatabaseReference databaseReferenceTarefa;
    private Intent intent;
    private Turma turma = new Turma();
    private Usuario usuario = new Usuario();

    private ClipboardManager clipboard;
    private ClipData clip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        INSTÂNCIAS DO BD E USUÁRIO
        //        Obter usuário logado
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        inicializarDatabase();
        obterUsuario();

        Log.i("==============  USER: ", currentUser.getDisplayName());

        mViewHolder.nome_turma = findViewById(R.id.textView11NomeTurma);
        mViewHolder.numero_turma = findViewById(R.id.textView11NumeroTurma);

        mViewHolder.fab = findViewById(R.id.fab);
        mViewHolder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!usuario.isAdmin()){
                    Snackbar.make(view, "Apenas o Administrador pode criar novas tarefas!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(view, "Bixão", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    intent = new Intent(HomeClassActivity.this, CreateTaskActivity.class);
                    intent.putExtra("turmaID", turma.getId());
                    startActivity(intent);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        obterUsuario();
    }

    private void inicializarDatabase() {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        databaseReferenceTurma = database.getReference();
    }

    private void obterUsuario() {
        databaseReferenceTurma.child("users").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(Usuario.class);
                if (usuario!=null){
//                    Toast.makeText(HomeClassActivity.this, "Recebeu "+usuario.getDescricao(), Toast.LENGTH_SHORT).show();
                    obterTurma(usuario);
                } else{
                    Toast.makeText(HomeClassActivity.this, "Não recebeu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void obterTurma(Usuario usuario) {
        databaseReferenceTurma.child("turmas").child(usuario.getIdTurma()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null){

                    turma = dataSnapshot.getValue(Turma.class);
                    Log.d("TAG","Nome da Turma: "+turma.getNome());
                    Log.d("TAG","Numero da Turma: "+turma.getNumero());

                    if (turma.getTarefas() != null) {
                        Iterator it = turma.getTarefas().entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Log.d("TAG","Tarefa: "+pair.getKey());
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                    }
                    if (turma!=null){
                        mViewHolder.nome_turma.setText(turma.getNome());
                        mViewHolder.numero_turma.setText(turma.getNumero());
    //                    Toast.makeText(HomeClassActivity.this, "Recebeu "+turma.getDescricao(), Toast.LENGTH_SHORT).show();
                    } else{
    //                    Toast.makeText(HomeClassActivity.this, "Não recebeu", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pricipal, menu);
        mViewHolder.avatar = findViewById(R.id.ivUser);
        mViewHolder.user_name = findViewById(R.id.tvUserNome);
        mViewHolder.user_email = (TextView)  findViewById(R.id.tvUserEmail);

        Log.i("---------TESTE", "------------------------------------------"+usuario.getNome());
        mViewHolder.user_name.setText(currentUser.getDisplayName());
        Picasso.get().load(currentUser.getPhotoUrl()).into(mViewHolder.avatar);
        mViewHolder.user_email.setText(currentUser.getEmail());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.me:
                intent = new Intent(HomeClassActivity.this, UserDataActivity.class);
                startActivity(intent);
                return true;
            case R.id.help:
                intent = new Intent(HomeClassActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
//          Passar o ID da Turma
            sendIntent.putExtra(Intent.EXTRA_TEXT, turma.getId());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.nav_copy) {
            clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clip = ClipData.newPlainText("Clip user ID", "Aqui está o meu ID:\n\n"+currentUser.getUid());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(HomeClassActivity.this, "ID da turma copiado: "+turma.getId(), Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class ViewHolder{
        ImageView avatar;
        TextView user_name, user_email, nome_turma, numero_turma;
        FloatingActionButton fab;
    }
}
