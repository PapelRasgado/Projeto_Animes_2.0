package com.jp.projetoanimes.activitys;

import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jp.projetoanimes.types.Codes;
import com.jp.projetoanimes.types.Anime;
import com.jp.projetoanimes.R;
import com.jp.projetoanimes.types.FirebaseManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.fabric.sdk.android.Fabric;

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
    private AppCompatCheckBox lanc;
    private LinearLayout dias;
    private AppCompatTextView diasTxt;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        }

        auth = FirebaseManager.getAuth();
        user = auth.getUid();
        database = FirebaseManager.getDatabase();

        txtNome = findViewById(R.id.txt_input);
        etNome = findViewById(R.id.et_nome);
        etEp = findViewById(R.id.et_ep);
        etTemp = findViewById(R.id.et_temp);
        etNotas = findViewById(R.id.et_notas);
        etUrl = findViewById(R.id.et_url);
        etLink = findViewById(R.id.et_link);
        lanc = findViewById(R.id.check_lance);
        dias = findViewById(R.id.dias);
        diasTxt = findViewById(R.id.txt_view_dias);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString("anime_su_nome", null) != null) {
                etNome.setText(getIntent().getExtras().getString("anime_su_nome"));
            }
            if (getIntent().getIntExtra("type", -1) != -1) {
                type = getIntent().getIntExtra("type", -1);
            }
        }

        lanc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dias.setVisibility(View.VISIBLE);
                    diasTxt.setVisibility(View.VISIBLE);
                } else {
                    dias.setVisibility(View.GONE);
                    diasTxt.setVisibility(View.GONE);
                }
            }
        });

        AppCompatButton btn = findViewById(R.id.btn_confirmar);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (etNome.getText().toString().isEmpty()) {
                    txtNome.setErrorEnabled(true);
                    txtNome.setError("Coloque o nome do anime!");
                } else {
                    List<Integer> diass = new ArrayList<>();
                    if (lanc.isChecked()) {
                        ToggleButton btnSunday = findViewById(R.id.sunday_toggle);
                        ToggleButton btnMonday = findViewById(R.id.monday_toggle);
                        ToggleButton btnTuesday = findViewById(R.id.tuesday_toggle);
                        ToggleButton btnWednsday = findViewById(R.id.wednesday_toggle);
                        ToggleButton btnThursday = findViewById(R.id.thursday_toggle);
                        ToggleButton btnFriday = findViewById(R.id.friday_toggle);
                        ToggleButton btnSaturday = findViewById(R.id.saturday_toggle);

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
                    Anime anime = new Anime(etNome.getText().toString(),
                            etEp.getText().toString().isEmpty() ? 1 : Integer.parseInt(etEp.getText().toString()),
                            etTemp.getText().toString().isEmpty() ? 1 : Integer.parseInt(etTemp.getText().toString()),
                            etNotas.getText().toString(),
                            etUrl.getText().toString(),
                            etLink.getText().toString(),
                            diass,
                            lanc.isChecked()
                    );
                    switch (type) {
                        case 0:
                            DatabaseReference myRef = database.getReference(user + "/listaAtu").push();
                            anime.setIdentifier(myRef.getKey());
                            myRef.setValue(anime);
                            setResult(Codes.ANIME_ADDED);
                            break;
                        case 1:
                            DatabaseReference myRef2 = database.getReference(user + "/listaConc").push();
                            anime.setIdentifier(myRef2.getKey());
                            myRef2.setValue(anime);
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
