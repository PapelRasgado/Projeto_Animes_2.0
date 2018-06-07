package com.jp.projetoanimes.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.jp.projetoanimes.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout txtEmail;
    private TextInputLayout txtSenha;
    private AppCompatEditText etEmail;
    private AppCompatEditText etSenha;
    private AppCompatButton logar;
    private AppCompatButton cadastro;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        txtEmail = findViewById(R.id.txt_login_email);
        txtSenha = findViewById(R.id.txt_login_password);
        etEmail = findViewById(R.id.et_login_email);
        etSenha = findViewById(R.id.et_login_password);
        logar = findViewById(R.id.btn_logar);
        cadastro = findViewById(R.id.btn_cadastro);

        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etEmail.getText().toString().isEmpty()) {
                    txtEmail.setErrorEnabled(false);
                    if (!etSenha.getText().toString().isEmpty()) {
                        txtSenha.setErrorEnabled(false);

                        etEmail.setEnabled(false);
                        etSenha.setEnabled(false);
                        logar.setEnabled(false);

                        mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etSenha.getText().toString())
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        etEmail.setEnabled(true);
                                        etSenha.setEnabled(true);
                                        logar.setEnabled(true);
                                        if (task.isSuccessful()) {
                                            finish();
                                            Intent it = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(it);
                                        } else {
                                            try {
                                                throw task.getException();
                                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                                txtEmail.setErrorEnabled(true);
                                                txtEmail.setError("Credenciais inválidas!");
                                                txtSenha.setErrorEnabled(true);
                                                txtSenha.setError("Credenciais inválidas!");
                                            } catch (Exception e) {
                                                Log.e("ERROR", e.getMessage());
                                            }
                                        }
                                    }
                                });

                    } else {
                        txtSenha.setErrorEnabled(true);
                        txtSenha.setError("Coloque uma senha!");
                    }
                } else {
                    txtEmail.setErrorEnabled(true);
                    txtEmail.setError("Coloque um email!");
                }
            }
        });

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(it);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            finish();
            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
        }
    }
}
