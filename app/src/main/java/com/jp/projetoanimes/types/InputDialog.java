package com.jp.projetoanimes.types;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.graphics.Point;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.Display;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.jp.projetoanimes.R;
import com.jp.projetoanimes.fragments.SugestaoFragment;

public class InputDialog extends AlertDialog.Builder implements
        OnShowListener, OnClickListener {

    private AlertDialog dialog;
    private AppCompatEditText et;
    private TextInputLayout txt;
    private SugestaoFragment suges;

    public InputDialog(Context context, SugestaoFragment suges) {
        super(context);
        this.suges = suges;

        View view = LayoutInflater.from(context).inflate(R.layout.input_dialog, null);
        et =  view.findViewById(R.id.et_nome_su_add);
        txt = view.findViewById(R.id.txt_nome_su_add);

        setView(view);

        setTitle("Adicionar sugestão: ");
        setPositiveButton(android.R.string.ok, null);
        setNegativeButton(android.R.string.cancel, null);
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
        Button b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        b.setId(DialogInterface.BUTTON_POSITIVE);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(et.getText().toString().isEmpty()){
            txt.setErrorEnabled(true);
            txt.setError("Coloque o nome do anime!");
        }else{
            suges.adiciona(et.getText().toString());
            dialog.dismiss();
        }
    }

}
