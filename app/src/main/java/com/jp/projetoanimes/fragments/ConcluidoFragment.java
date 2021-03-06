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
import com.jp.projetoanimes.adapters.AdapterCon;
import com.jp.projetoanimes.processes.TouchHelperCallbackCon;

@SuppressLint("StaticFieldLeak")
public class ConcluidoFragment extends Fragment {

    static private RecyclerView recyclerView;
    static private AdapterCon adapter;
    static private ItemTouchHelper touchHelper;

    static private boolean ordenacao;
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
        mudarCallBack();
    }

    @Override
    public void onStop() {
        adapter.salvaLista();
        super.onStop();
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
        adapter = new AdapterCon(act, ordenacao);

        recyclerView.setAdapter(adapter);
    }

    public void mudarOrdenacao(){
        ordenacao = !ordenacao;
        mudarManager();
        mudarAdapter();
        mudarCallBack();
    }

    public void mudarCallBack(){
        if (touchHelper != null){
            touchHelper.attachToRecyclerView(null);
        }
        TouchHelperCallbackCon callback = new TouchHelperCallbackCon(adapter, ordenacao);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    public void fazerPesquisa(boolean b, String nome){
        if (adapter != null){
            adapter.fazerPesquisa(b, nome);
        }
    }

    public void apagar(int position){
        adapter.apagar(position, recyclerView);
    }

    public  void atualizar() {
        adapter.atualizarItens();
    }



}
