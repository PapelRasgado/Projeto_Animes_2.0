package com.jp.projetoanimes.activitys;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.jp.projetoanimes.R;
import com.jp.projetoanimes.processes.Codes;
import com.jp.projetoanimes.processes.SalvarBD;
import com.jp.projetoanimes.types.Anime;

import java.util.List;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    private int type;

    private SalvarBD sbd;
    private int animeP;
    private Anime anime;

    private TextInputLayout txtNome;
    private AppCompatEditText etNome;
    private AppCompatEditText etEp;
    private AppCompatEditText etTemp;
    private AppCompatEditText etNotas;
    private AppCompatEditText etUrl;
    private AppCompatEditText etLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Editar Anime");
        setSupportActionBar(toolbar);

        sbd = new SalvarBD(this);

        animeP = getIntent().getIntExtra("anime_detalhe", -1) ;
        type = getIntent().getIntExtra("type", -1);
        anime = (Anime) sbd.pegaLista(type).get(animeP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        }

        txtNome = findViewById(R.id.txt_nome_su_add);
        etNome = findViewById(R.id.et_nome);
        etNome.setText(anime.getNome());

        etEp = findViewById(R.id.et_ep);
        etEp.setText(String.valueOf(anime.getEp()));

        etTemp = findViewById(R.id.et_temp);
        etTemp.setText(String.valueOf(anime.getTemp()));

        etNotas = findViewById(R.id.et_notas);
        etNotas.setText(anime.getNotas());

        etUrl = findViewById(R.id.et_url);
        etUrl.setText(anime.getImage());

        etLink = findViewById(R.id.et_link);
        etLink.setText(anime.getLink());

        AppCompatButton btn = findViewById(R.id.btn_confirmar);
        btn.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View view) {
                if (etNome.getText().toString().isEmpty()) {
                    txtNome.setErrorEnabled(true);
                    txtNome.setError("Coloque o nome do anime!");
                } else {
                    anime = new Anime(etNome.getText().toString(),
                            etEp.getText().toString().isEmpty() ? 1 : Integer.parseInt(etEp.getText().toString()),
                            etTemp.getText().toString().isEmpty() ? 1 : Integer.parseInt(etTemp.getText().toString()),
                            etNotas.getText().toString(),
                            etUrl.getText().toString(),
                            etLink.getText().toString()

                    );
                    List<Anime> lista = sbd.pegaLista(type);
                    lista.set(animeP, anime);
                    sbd.salvaLista(type, lista);
                    setResult(Codes.ANIME_MODIFY);
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
