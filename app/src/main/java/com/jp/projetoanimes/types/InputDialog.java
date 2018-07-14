package com.jp.projetoanimes.types;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.dd.processbutton.iml.ActionProcessButton;
import com.jp.projetoanimes.R;
import com.jp.projetoanimes.fragments.SugestaoFragment;

@SuppressLint("InflateParams")
public class InputDialog extends AlertDialog.Builder implements
        OnShowListener, OnClickListener {

    private AlertDialog dialog;
    private AppCompatEditText et;
    private TextInputLayout txt;
    private ActionProcessButton btn;
    private SugestaoFragment suges;

    public InputDialog(Context context, SugestaoFragment suges) {
        super(context);
        this.suges = suges;

        View view = LayoutInflater.from(context).inflate(R.layout.input_dialog, null);
        et =  view.findViewById(R.id.et_input);
        txt = view.findViewById(R.id.txt_input);
        btn = view.findViewById(R.id.btn_send);
        btn.setMode(ActionProcessButton.Mode.ENDLESS);

        setView(view);

        setTitle("Adicionar sugestão: ");
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
            txt.setError("Coloque o nome do anime!");
        }else{
            btn.setProgress(100);
            suges.adiciona(et.getText().toString());
            dialog.dismiss();
        }
    }

}
