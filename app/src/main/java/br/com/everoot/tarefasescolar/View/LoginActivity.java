package br.com.everoot.tarefasescolar.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;
import br.com.everoot.tarefasescolar.Model.Usuario;
import br.com.everoot.tarefasescolar.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    // Write a message to the database
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("users");
    private Intent signInIntent;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private static final String TAG = "TESTE";
    private static final int RC_SIGN_IN = 9001;
    private ViewHolder mViewHolder = new ViewHolder();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mViewHolder.sign_in = findViewById(R.id.sign_in_button);
        mViewHolder.sign_in.setOnClickListener(this);

        // obter a instancia do Firebase
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void updateUI(final FirebaseUser account) {

        if (account != null){
            Log.i("GOOGLE SIGN IN ID:", account.getUid());
            Log.i("GOOGLE SIGN IN NAME:", account.getDisplayName());
            Log.i("GOOGLE SIGN IN EMAIL:", account.getEmail());
            Log.i("GOOGLE SIGN IN PHOTO:", account.getPhotoUrl().toString());


            myRef.child(account.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            Usuario usuario = new Usuario();
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    usuario = dataSnapshot.getValue(Usuario.class);

                    if (usuario == null){
                        // cadastrar o usuário no database
                        usuario = new Usuario();
                        usuario.setId(account.getUid());
                        usuario.setAdmin(false);
                        usuario.setEmail(account.getEmail());
                        usuario.setIdTurma(null);
                        usuario.setToken(account.getIdToken(true).toString());
                        usuario.setNome(account.getDisplayName());
                        Log.i("======== LOGIN", "BUSCANDO NO DB O USUARIO: "+ usuario.getNome());
                        myRef.child(usuario.getId()).setValue(usuario);
                    }

                    if (usuario.getIdTurma()!=null){
                        intent = new Intent(LoginActivity.this, HomeClassActivity.class);
                        finish();
                        startActivity(intent);
                    } else {
                        intent = new Intent(LoginActivity.this, IngressActivity.class);
                        finish();
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("======== LOGIN", "ERRO AO BUSCAR O USUARIO: " + databaseError);
                }
            });
        }
    }


    private void signIn() {
        signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(LoginActivity.this, "Login com o Google falhou...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.sign_in_button), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.sign_in_button){
//            Toast.makeText(LoginActivity.this, "Button pressed...", Toast.LENGTH_SHORT).show();
            signIn();
        }
        else{
            Toast.makeText(LoginActivity.this, "Login com o Google falhou...", Toast.LENGTH_SHORT).show();
        }
    }

    public static class ViewHolder{
        SignInButton sign_in;
        ProgressBar progressBar;
    }

}
