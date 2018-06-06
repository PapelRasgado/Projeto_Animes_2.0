package com.jp.projetoanimes.activitys;

import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jp.projetoanimes.processes.Codes;
import com.jp.projetoanimes.types.Anime;
import com.jp.projetoanimes.R;

import java.util.Objects;

public class AdicionarActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private FirebaseAuth auth;

    private int type;

    private TextInputLayout txtNome;
    private AppCompatEditText etNome;
    private AppCompatEditText etEp;
    private AppCompatEditText etTemp;
    private AppCompatEditText etNotas;
    private AppCompatEditText etUrl;
    private AppCompatEditText etLink;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getUid();
        database = FirebaseDatabase.getInstance();

        txtNome = findViewById(R.id.txt_nome_su_add);
        etNome = findViewById(R.id.et_nome);
        etEp = findViewById(R.id.et_ep);
        etTemp = findViewById(R.id.et_temp);
        etNotas = findViewById(R.id.et_notas);
        etUrl = findViewById(R.id.et_url);
        etLink = findViewById(R.id.et_link);

        if (getIntent().getExtras() != null){
            if (getIntent().getExtras().getString("anime_su_nome", null) != null) {
                etNome.setText(getIntent().getExtras().getString("anime_su_nome"));
            }
            if (getIntent().getIntExtra("type", -1) != -1){
                type = getIntent().getIntExtra("type", -1);
            }
        }

        AppCompatButton btn = findViewById(R.id.btn_confirmar);
        btn.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View view) {
                if (etNome.getText().toString().isEmpty()) {
                    txtNome.setErrorEnabled(true);
                    txtNome.setError("Coloque o nome do anime!");
                } else {
                    Anime anime = new Anime(etNome.getText().toString(),
                            etEp.getText().toString().isEmpty() ? 1 : Integer.parseInt(etEp.getText().toString()),
                            etTemp.getText().toString().isEmpty() ? 1 : Integer.parseInt(etTemp.getText().toString()),
                            etNotas.getText().toString(),
                            etUrl.getText().toString(),
                            etLink.getText().toString()

                    );
                    switch (type){
                        case 0:
                            DatabaseReference myRef = database.getReference(user + "/listaAtu").push();
                            anime.setIdentifier(myRef.getKey());
                            myRef.setValue(anime);
                            setResult(Codes.ANIME_ADDED);
                            break;
                        case 1:
                            DatabaseReference myRef2 = database.getReference(user + "/listaConc").push();
                            anime.setIdentifier(myRef2.getKey());
                            myRef2.push().setValue(anime);
                            setResult(Codes.ANIME_ADDED_CONC);
                            break;
                    }

                    finish();
                }
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
