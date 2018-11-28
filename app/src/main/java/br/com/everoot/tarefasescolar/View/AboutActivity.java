package br.com.everoot.tarefasescolar.View;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import br.com.everoot.tarefasescolar.BuildConfig;
import br.com.everoot.tarefasescolar.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        String versionName = BuildConfig.VERSION_NAME;

        TextView versao = findViewById(R.id.textViewVersao);
        versao.setText(String.format("versão: %s", versionName));
    }
}
