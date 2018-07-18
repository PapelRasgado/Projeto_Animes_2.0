package com.jp.projetoanimes.types;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.jp.projetoanimes.R;

@SuppressLint("InflateParams")
public class RecoveryDialog extends AlertDialog.Builder implements
        OnShowListener, OnClickListener {

    private AlertDialog dialog;
    private AppCompatEditText et;
    private TextInputLayout txt;
    private ActionProcessButton btn;

    public RecoveryDialog(Context context) {
        super(context);

        View view = LayoutInflater.from(context).inflate(R.layout.input_dialog, null);
        et =  view.findViewById(R.id.et_input);
        txt = view.findViewById(R.id.txt_input);
        btn = view.findViewById(R.id.btn_send);
        btn.setMode(ActionProcessButton.Mode.ENDLESS);

        txt.setHint("Email...");

        setView(view);

        setTitle("Email para recuperação da senha: ");
//        setPositiveButton(android.R.string.ok, null);
//        setNegativeButton(android.R.string.cancel, null);
    }

    @Override
    public AlertDialog show() {
        dialog = create();
        dialog.setOnShowListener(this);
        dialog.show();
        return dialog;
    }

    @Override
    public void onShow(DialogInterface d) {
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        btn.setProgress(1);
        if(et.getText().toString().isEmpty()){
            btn.setProgress(-1);
            txt.setErrorEnabled(true);
            txt.setError("Digite um email!");
        }else{
            FirebaseManager.getAuth().sendPasswordResetEmail(et.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        btn.setProgress(100);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 2000);
                    } else {
                        btn.setProgress(-1);
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            txt.setErrorEnabled(true);
                            txt.setError("Email invalido!");
                        } catch(Exception e) {
                            Log.e("ERROR", e.toString());
                        }
                    }
                }
            });
        }
    }

}

