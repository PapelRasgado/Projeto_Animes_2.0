package com.jp.projetoanimes.tasks;

import android.os.AsyncTask;

import com.jp.projetoanimes.adapters.AdapterSu;

import java.util.ArrayList;
import java.util.List;

public class PesquisaSuTask extends AsyncTask<String, Void, List<String>> {

    private AdapterSu adapter;

    public PesquisaSuTask(AdapterSu adapter){
        this.adapter = adapter;
    }

    @Override
    protected List<String> doInBackground(String... strings) {
        List<String> listaC = adapter.getListCompleta();
        List<String> listaA = new ArrayList<>();
        for (String s: listaC) {
            if(s.toLowerCase().contains(strings[0].toLowerCase())){
                listaA.add(s);
            }
        }
        return listaA;
    }

    @Override
    protected void onPostExecute(List<String> lista) {
        adapter.setListAtual(lista);
    }
}