package br.com.everoot.tarefasescolar.View;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import br.com.everoot.tarefasescolar.R;

public class UserDataActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ClipboardManager clipboard;
    private ClipData clip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdata);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clip = ClipData.newPlainText("Clip user ID", "Aqui está o meu ID:\n\n"+currentUser.getUid());

        TextView t1 = findViewById(R.id.tvNome);
        TextView t2 = findViewById(R.id.tvUid);
        TextView t3 = findViewById(R.id.tvEmail);
        ImageView imageView = findViewById(R.id.imagem);
        FloatingActionButton fbCopy = findViewById(R.id.fbCopyID);
        fbCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(UserDataActivity.this, "Texto copiado!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        t3.setText(currentUser.getEmail());
        t2.setText(currentUser.getUid());
        t1.setText(currentUser.getDisplayName());
        Picasso.get().load(currentUser.getPhotoUrl()).into(imageView);

        Button signout = findViewById(R.id.btnSignout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finishAffinity();
            }
        });
    }
}
