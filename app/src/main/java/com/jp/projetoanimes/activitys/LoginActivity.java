package com.jp.projetoanimes.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.jp.projetoanimes.R;
import com.jp.projetoanimes.service.NotifyService;
import com.jp.projetoanimes.types.FirebaseManager;
import com.jp.projetoanimes.dialogs.RecoveryDialog;
import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout txtEmail;
    private TextInputLayout txtSenha;
    private AppCompatEditText etEmail;
    private AppCompatEditText etSenha;
    private ActionProcessButton logar;
    private AppCompatButton cadastro;
    private AppCompatButton recovery;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.login_layout);



        txtEmail = findViewById(R.id.txt_login_email);
        txtSenha = findViewById(R.id.txt_login_password);
        etEmail = findViewById(R.id.et_login_email);
        etSenha = findViewById(R.id.et_login_password);
        logar = findViewById(R.id.btn_logar);
        cadastro = findViewById(R.id.btn_cadastro);
        recovery = findViewById(R.id.btn_recovery);
        logar.setMode(ActionProcessButton.Mode.ENDLESS);

        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logar.setProgress(1);
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
                                        if (task.isSuccessful()) {
                                            startService(new Intent(LoginActivity.this, NotifyService.class));
                                            logar.setProgress(100);
                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    finish();
                                                    Intent it = new Intent(LoginActivity.this, MainActivity.class);
                                                    startActivity(it);
                                                }
                                            }, 2000);
                                        } else {
                                            logar.setEnabled(true);
                                            logar.setProgress(-1);
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
                        logar.setProgress(-1);
                    }
                } else {
                    logar.setProgress(-1);
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

        recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RecoveryDialog(LoginActivity.this).show();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseManager.getAuth();
        if (mAuth.getCurrentUser() != null) {
            finish();
            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
        }
    }
}
