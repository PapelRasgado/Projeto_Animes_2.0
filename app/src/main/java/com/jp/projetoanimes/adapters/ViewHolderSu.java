package com.jp.projetoanimes.adapters;

import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jp.projetoanimes.R;

public class ViewHolderSu extends RecyclerView.ViewHolder {

    public TextView titulo;
    public AppCompatImageButton btnSend;
    public AppCompatImageButton btnDelete;

    ViewHolderSu(View itemView) {
        super(itemView);

        titulo = itemView.findViewById(R.id.titulo_anime_su);
        btnSend = itemView.findViewById(R.id.btn_su_send);
        btnDelete = itemView.findViewById(R.id.btn_su_delete);

    }
}

