package com.jp.projetoanimes.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jp.projetoanimes.R;
import com.jp.projetoanimes.adapters.Adapter;

@SuppressLint("StaticFieldLeak")
public class AtualFragment extends Fragment {

    static private RecyclerView recyclerView;
    static private Adapter adapter;

    static private boolean ordenacao;
    static private boolean order;
    static private LayoutInflater inf;
    static private Activity act;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.layout_aba, container, false);

        recyclerView = v.findViewById(R.id.recycler);
        inf = inflater;

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null){
            act = getActivity();
        }
        mudarManager();
        mudarAdapter();
    }

    private void mudarManager(){
        RecyclerView.LayoutManager l;
        if (ordenacao){
            if (act.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                l = new GridLayoutManager(inf.getContext(), 3);
            }else{
                l = new GridLayoutManager(inf.getContext(), 2);
            }
        } else {
            l = new LinearLayoutManager(inf.getContext());
        }
        recyclerView.setLayoutManager(l);
    }

    private void mudarAdapter(){
        adapter = new Adapter(act, ordenacao);

        recyclerView.setAdapter(adapter);
    }

    public void mudarOrdenacao(){
        ordenacao = !ordenacao;
        mudarManager();
        mudarAdapter();
    }


    public void fazerPesquisa(boolean b, String nome){
        if (adapter != null){
            adapter.fazerPesquisa(b, nome);
        }
    }

    public void apagar(String identifier){
        adapter.apagar(identifier, recyclerView);
    }

    public  void atualizar() {
        if (adapter != null){
            adapter.atualizarItens();
        }
    }



}
