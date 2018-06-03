package com.jp.projetoanimes.adapters;


import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jp.projetoanimes.R;

public class ViewHolder extends RecyclerView.ViewHolder{

    public ImageView img;
    public TextView titulo;
    public TextView episodio;
    public AppCompatButton btnMais;
    public AppCompatButton btnMenos;

    ViewHolder(View itemView) {
        super(itemView);

        this.img = itemView.findViewById(R.id.image);
        this.titulo = itemView.findViewById(R.id.titulo_anime);
        this.episodio = itemView.findViewById(R.id.episodio);
        this.btnMais = itemView.findViewById(R.id.btn_mais);
        this.btnMenos = itemView.findViewById(R.id.btn_menos);

    }
}
