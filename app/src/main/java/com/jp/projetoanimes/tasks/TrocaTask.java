package com.jp.projetoanimes.tasks;

import android.os.AsyncTask;

import com.jp.projetoanimes.adapters.Adapter;
import com.jp.projetoanimes.types.Anime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrocaTask extends AsyncTask<Integer, Void, Void> {

    private Adapter adapter;
    private int fromPosition;
    private int toPosition;

    public TrocaTask(Adapter adapter){
        this.adapter = adapter;
    }

    @Override
    protected Void doInBackground(Integer... ints) {
        fromPosition = ints[0];
        toPosition = ints[1];
        List<Anime> listAtual = adapter.getListAtual();
        List<Anime> listCompleta = adapter.getListCompleta();

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(listAtual, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(listAtual, i, i - 1);
            }
        }
        int com1 = listCompleta.indexOf(listAtual.get(fromPosition));
        int com2 = listCompleta.indexOf(listAtual.get(toPosition));
        if (com1 < com2) {
            for (int i = com1; i < com2; i++) {
                Collections.swap(listCompleta, i, i + 1);
            }
        } else {
            for (int i = com1; i > com2; i--) {
                Collections.swap(listCompleta, i, i - 1);
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        adapter.notifyItemMoved(fromPosition, toPosition);;
    }
}
