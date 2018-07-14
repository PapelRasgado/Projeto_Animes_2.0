package com.jp.projetoanimes.activitys;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.jp.projetoanimes.R;

import java.util.Objects;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout txtEmail;
    private TextInputLayout txtSenha;
    private TextInputLayout txtRepet;
    private AppCompatEditText etEmail;
    private AppCompatEditText etSenha;
    private AppCompatEditText etRepet;
    private ActionProcessButton cadastrar;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        }

        mAuth = FirebaseAuth.getInstance();

        txtEmail = findViewById(R.id.txt_cadastro_email);
        txtSenha = findViewById(R.id.txt_cadastro_password);
        txtRepet = findViewById(R.id.txt_cadastro_repeat_password);
        etEmail = findViewById(R.id.et_cadastro_email);
        etSenha = findViewById(R.id.et_cadastro_password);
        etRepet = findViewById(R.id.et_cadastro_repeat_password);
        cadastrar = findViewById(R.id.btn_cadastrar);
        cadastrar.setMode(ActionProcessButton.Mode.ENDLESS);


        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar.setProgress(1);
                txtEmail.setErrorEnabled(false);
                txtSenha.setErrorEnabled(false);
                txtRepet.setErrorEnabled(false);
                if (!etEmail.getText().toString().isEmpty()) {
                    if (!etSenha.getText().toString().isEmpty()) {
                        if (!etRepet.getText().toString().isEmpty()) {
                            if (etSenha.getText().toString().equals(etRepet.getText().toString())){

                                etEmail.setEnabled(false);
                                etSenha.setEnabled(false);
                                etRepet.setEnabled(false);
                                cadastrar.setEnabled(false);

                                mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etSenha.getText().toString())
                                        .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                etEmail.setEnabled(true);
                                                etSenha.setEnabled(true);
                                                etRepet.setEnabled(true);
                                                cadastrar.setEnabled(true);

                                                if (task.isSuccessful()){
                                                    cadastrar.setProgress(100);
                                                    mAuth.signOut();
                                                    final Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            finish();
                                                        }
                                                    }, 2000);

                                                } else {
                                                    cadastrar.setProgress(-1);
                                                    try {
                                                        throw task.getException();
                                                    } catch(FirebaseAuthWeakPasswordException e) {
                                                        txtSenha.setErrorEnabled(true);
                                                        txtSenha.setError("A senha deve conter pelo menos 6 digitos!");
                                                    } catch(FirebaseAuthUserCollisionException e) {
                                                        txtEmail.setErrorEnabled(true);
                                                        txtEmail.setError("Email já está em uso!");
                                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                                        txtEmail.setErrorEnabled(true);
                                                        txtEmail.setError("Email invalido!");
                                                    } catch(Exception e) {
                                                        Log.e("ERROR", e.toString());
                                                    }
                                                }
                                            }
                                        });
                            } else {
                                cadastrar.setProgress(-1);
                                txtSenha.setErrorEnabled(true);
                                txtSenha.setError("As senhas não combinam!");
                                txtRepet.setErrorEnabled(true);
                                txtRepet.setError("As senhas não combinam!");
                            }

                        } else {
                            cadastrar.setProgress(-1);
                            txtRepet.setErrorEnabled(true);
                            txtRepet.setError("Repita sua senha!");
                        }

                    } else {
                        cadastrar.setProgress(-1);
                        txtSenha.setErrorEnabled(true);
                        txtSenha.setError("Coloque uma senha!");
                    }
                } else {
                    cadastrar.setProgress(-1);
                    txtEmail.setErrorEnabled(true);
                    txtEmail.setError("Coloque um email!");
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
