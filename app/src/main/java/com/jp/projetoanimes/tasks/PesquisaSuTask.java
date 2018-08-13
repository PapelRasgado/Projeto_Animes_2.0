package com.jp.projetoanimes.tasks;

import android.os.AsyncTask;

import com.jp.projetoanimes.adapters.AdapterSu;
import com.jp.projetoanimes.types.Sugestao;

import java.util.ArrayList;
import java.util.List;

public class PesquisaSuTask extends AsyncTask<String, Void, List<Sugestao>> {

    private AdapterSu adapter;

    public PesquisaSuTask(AdapterSu adapter){
        this.adapter = adapter;
    }

    @Override
    protected List<Sugestao> doInBackground(String... strings) {
        List<Sugestao> listaC = new ArrayList<>(adapter.getListCompleta().values());
        List<Sugestao> listaA = new ArrayList<>();
        for (Sugestao s: listaC) {
            if(s.getNome().toLowerCase().contains(strings[0].toLowerCase())){
                listaA.add(s);
            }
        }
        return listaA;
    }

    @Override
    protected void onPostExecute(List<Sugestao> lista) {
        adapter.setListAtual(lista);
    }
}