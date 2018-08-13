package com.jp.projetoanimes.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.jp.projetoanimes.R;
import com.jp.projetoanimes.adapters.AdapterSu;
import com.jp.projetoanimes.types.FirebaseManager;

@SuppressLint("StaticFieldLeak")
public class SugestaoFragment extends Fragment {

    FirebaseAuth auth;

    static private RecyclerView recyclerView;
    static private AdapterSu adapter;

    static private LayoutInflater inf;
    static private Activity act;

    public SugestaoFragment(){
        auth = FirebaseManager.getAuth();
    }

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
        RecyclerView.LayoutManager l = new LinearLayoutManager(inf.getContext());

        recyclerView.setLayoutManager(l);
    }

    private void mudarAdapter(){
        adapter = new AdapterSu(act, auth.getCurrentUser().getUid());

        recyclerView.setAdapter(adapter);
    }

    public static AdapterSu getAdapter() {
        return adapter;
    }
}
