package com.jp.projetoanimes.tasks;


import android.os.AsyncTask;

import com.jp.projetoanimes.types.Anime;
import com.jp.projetoanimes.adapters.Adapter;

import java.util.ArrayList;
import java.util.List;

public class PesquisaTask extends AsyncTask<String, Void, List<Anime>> {

    private Adapter adapter;

    public PesquisaTask(Adapter adapter){
        this.adapter = adapter;
    }

    @Override
    protected List<Anime> doInBackground(String... strings) {
        List<Anime> listaC = (List<Anime>) adapter.getListCompleta().values();
        List<Anime> listaA = new ArrayList<>();
        for (Anime a: listaC) {
            if(a.getNome().toLowerCase().contains(strings[0].toLowerCase())){
                listaA.add(a);
            }
        }

        return listaA;
    }

    @Override
    protected void onPostExecute(List<Anime> lista) {
        adapter.setListAtual(lista);
    }
}
