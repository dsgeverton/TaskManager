package br.com.everoot.tarefasescolar.View;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import br.com.everoot.tarefasescolar.R;

public class HomeClassActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Intent intent;

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
        if (currentUser ==null)
            Toast.makeText(this, "Fudeu", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Não fudeu", Toast.LENGTH_SHORT).show();

        Log.i("==============  USER: ", currentUser.getDisplayName());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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


        Picasso.get().load(currentUser.getPhotoUrl()).into(mViewHolder.avatar);
        mViewHolder.user_name.setText(currentUser.getDisplayName());
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
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class ViewHolder{
        ImageView avatar;
        TextView user_name, user_email;
    }
}
