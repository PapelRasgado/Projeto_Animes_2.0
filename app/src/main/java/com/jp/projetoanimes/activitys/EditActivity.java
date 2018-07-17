package com.jp.projetoanimes.activitys;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jp.projetoanimes.R;
import com.jp.projetoanimes.processes.Codes;
import com.jp.projetoanimes.types.Anime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.fabric.sdk.android.Fabric;

public class EditActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private String user;

    private int type;

    private Anime anime;

    private TextInputLayout txtNome;
    private AppCompatEditText etNome;
    private AppCompatEditText etEp;
    private AppCompatEditText etTemp;
    private AppCompatEditText etNotas;
    private AppCompatEditText etUrl;
    private AppCompatEditText etLink;
    private AppCompatCheckBox lanc;
    private LinearLayout dias;
    private AppCompatTextView diasTxt;

    private ToggleButton btnSunday;
    private ToggleButton btnMonday;
    private ToggleButton btnTuesday;
    private ToggleButton btnWednsday;
    private ToggleButton btnThursday;
    private ToggleButton btnFriday;
    private ToggleButton btnSaturday;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Editar Anime");
        setSupportActionBar(toolbar);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getUid();
        database = FirebaseDatabase.getInstance();

        String animeI = getIntent().getStringExtra("anime_detalhe");
        type = getIntent().getIntExtra("type", -1);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                anime = dataSnapshot.getValue(Anime.class);
                mudarDados();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        switch (type){
            case 0:
                database.getReference(user).child("listaAtu").child(animeI).addListenerForSingleValueEvent(listener);
                break;
            case 1:
                database.getReference(user).child("listaConc").child(animeI).addListenerForSingleValueEvent(listener);
                break;
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        }

    }

    private void mudarDados(){
        txtNome = findViewById(R.id.txt_input);
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

        lanc = findViewById(R.id.check_lance);
        dias = findViewById(R.id.dias);
        diasTxt = findViewById(R.id.txt_view_dias);

        lanc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    dias.setVisibility(View.VISIBLE);
                    diasTxt.setVisibility(View.VISIBLE);
                } else {
                    dias.setVisibility(View.GONE);
                    diasTxt.setVisibility(View.GONE);
                }
            }
        });

        lanc.setChecked(anime.isLanc());

        btnSunday = findViewById(R.id.sunday_toggle);
        btnMonday = findViewById(R.id.monday_toggle);
        btnTuesday = findViewById(R.id.tuesday_toggle);
        btnWednsday = findViewById(R.id.wednesday_toggle);
        btnThursday = findViewById(R.id.thursday_toggle);
        btnFriday = findViewById(R.id.friday_toggle);
        btnSaturday = findViewById(R.id.saturday_toggle);

        if (anime.isLanc()){
            if (anime.getDias().contains(1)){
                btnSunday.setChecked(true);
            }
            if (anime.getDias().contains(2)){
                btnMonday.setChecked(true);
            }
            if (anime.getDias().contains(3)){
                btnTuesday.setChecked(true);
            }
            if (anime.getDias().contains(4)){
                btnWednsday.setChecked(true);
            }
            if (anime.getDias().contains(5)){
                btnThursday.setChecked(true);
            }
            if (anime.getDias().contains(6)){
                btnFriday.setChecked(true);
            }
            if (anime.getDias().contains(7)){
                btnSaturday.setChecked(true);
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
                    List<Integer> diass = new ArrayList<>();
                    if (lanc.isChecked()) {
                        if (btnSunday.isChecked() ) {
                            diass.add(1);
                        }
                        if (btnMonday.isChecked()) {
                            diass.add(2);
                        }
                        if (btnTuesday.isChecked()) {
                            diass.add(3);
                        }
                        if (btnWednsday.isChecked()) {
                            diass.add(4);
                        }
                        if (btnThursday.isChecked()) {
                            diass.add(5);
                        }
                        if (btnFriday.isChecked()) {
                            diass.add(6);
                        }
                        if (btnSaturday.isChecked()) {
                            diass.add(7);
                        }
                    }

                    anime = new Anime(etNome.getText().toString(),
                            etEp.getText().toString().isEmpty() ? 1 : Integer.parseInt(etEp.getText().toString()),
                            etTemp.getText().toString().isEmpty() ? 1 : Integer.parseInt(etTemp.getText().toString()),
                            etNotas.getText().toString(),
                            etUrl.getText().toString(),
                            etLink.getText().toString(),
                            anime.getData(),
                            anime.getIdentifier(),
                            diass,
                            lanc.isChecked(),
                            anime.isAgend()
                    );
                    switch (type){
                        case 0:
                            database.getReference(user).child("listaAtu").child(anime.getIdentifier()).setValue(anime);
                            break;
                        case 1:
                            database.getReference(user).child("listaConc").child(anime.getIdentifier()).setValue(anime);
                            break;
                    }
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
