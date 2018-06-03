package com.jp.projetoanimes.adapters;

import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jp.projetoanimes.R;

public class ViewHolderCon extends RecyclerView.ViewHolder{

    public ImageView img;
    public TextView titulo;
    public AppCompatImageButton undo;
    public AppCompatImageButton delete;

    ViewHolderCon(View itemView) {
        super(itemView);

        this.img = itemView.findViewById(R.id.image_conc);
        this.titulo = itemView.findViewById(R.id.titulo_anime_conc);
        this.undo = itemView.findViewById(R.id.btn_undo_conc);
        this.delete = itemView.findViewById(R.id.btn_delete_conc);

    }




}