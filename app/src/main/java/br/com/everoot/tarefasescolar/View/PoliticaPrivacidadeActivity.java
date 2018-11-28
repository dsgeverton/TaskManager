package br.com.everoot.tarefasescolar.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import br.com.everoot.tarefasescolar.R;

public class PoliticaPrivacidadeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_privacidade);

        WebView webView = findViewById(R.id.wvPP);

        webView.loadUrl("https://dsgeverton.wordpress.com/jbm-informa-politica-de-privacidade/");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
