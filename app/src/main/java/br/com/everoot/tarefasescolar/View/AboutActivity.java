package br.com.everoot.tarefasescolar.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

import br.com.everoot.tarefasescolar.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide() ;
        setContentView(R.layout.activity_about);
    }
}
