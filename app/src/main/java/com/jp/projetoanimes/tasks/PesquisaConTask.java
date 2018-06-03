package com.jp.projetoanimes.tasks;

import android.os.AsyncTask;

import com.jp.projetoanimes.adapters.AdapterCon;
import com.jp.projetoanimes.types.Anime;

import java.util.ArrayList;
import java.util.List;

public class PesquisaConTask extends AsyncTask<String, Void, List<Anime>> {

    private AdapterCon adapter;

    public PesquisaConTask(AdapterCon adapter){
        this.adapter = adapter;
    }

    @Override
    protected List<Anime> doInBackground(String... strings) {
        List<Anime> listaC = adapter.getListCompleta();
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