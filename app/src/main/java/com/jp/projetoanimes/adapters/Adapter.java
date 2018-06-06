package com.jp.projetoanimes.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jp.projetoanimes.activitys.DetailsActivity;
import com.jp.projetoanimes.processes.Codes;
import com.jp.projetoanimes.types.Anime;
import com.jp.projetoanimes.tasks.PesquisaTask;
import com.jp.projetoanimes.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<ViewHolder>{

    private DatabaseReference myRef;

    private HashMap<String, Anime> listCompleta;
    private boolean ordenacao;
    private List<Anime> listAtual;
    private Activity act;

    public Adapter(Activity act, boolean ordenacao) {
        this.act = act;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        myRef = database.getReference(auth.getUid()).child("listaAtu");
        this.listCompleta = new HashMap<>();
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                listCompleta.put(s, dataSnapshot.getValue(Anime.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                listCompleta.put(s, dataSnapshot.getValue(Anime.class));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                listCompleta.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        myRef.addChildEventListener(listener);
        this.listAtual = new ArrayList<>(listCompleta.values());
        this.ordenacao = ordenacao;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;

        if (ordenacao) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_atual_grid, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_atual_linear, parent, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Anime anime = listAtual.get(position);

        holder.titulo.setText(anime.getNome());
        holder.titulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) act.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Nome do anime", anime.getNome());
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(act, "Nome copiado!", Toast.LENGTH_SHORT).show();
            }
        });

        Display getOrient = act.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        getOrient.getSize(size);
        if (ordenacao) {
            if (act.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                holder.img.getLayoutParams().height = ((((size.x) / 3) - 32) * 9) / 16;
            } else {
                holder.img.getLayoutParams().height = ((((size.x) / 2) - 32) * 9) / 16;
            }
        } else {
            holder.img.getLayoutParams().height = ((size.x - 48) * 9) / 16;
        }

        String episodio = "Episodio: " + anime.getEp();
        holder.episodio.setText(episodio);
        holder.btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (anime.getEp() > 1) {
                    anime.mudarEp(-1);
                    myRef.child(anime.getIdentifier()).child("ep").setValue(anime.getEp());
                    notifyDataSetChanged();
                }
            }
        });
        holder.btnMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anime.mudarEp(+1);
                listCompleta.put(anime.getIdentifier(), anime);
                myRef.child(anime.getIdentifier()).child("ep").setValue(anime.getEp());
                notifyDataSetChanged();
            }
        });
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(act, DetailsActivity.class);
                intent.putExtra("anime_detalhe", anime.getIdentifier());
                intent.putExtra("type", 0);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(act,
                                holder.img,
                                ViewCompat.getTransitionName(holder.img));
                act.startActivityForResult(intent, Codes.ANIME_DETAIL, options.toBundle());
            }
        });

        if (!anime.getImage().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(anime.getImage())
                    .into(holder.img);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.anime)
                    .into(holder.img);
        }
    }

    @Override
    public int getItemCount() {
        return listAtual.size();
    }

    public void fazerPesquisa(boolean b, String nome) {
        if (b) {
            new PesquisaTask(this).execute(nome);
        } else {
            listAtual = new ArrayList<>(listCompleta.values());
        }
        notifyDataSetChanged();
    }

    public HashMap<String, Anime> getListCompleta() {
        return listCompleta;
    }

    public void setListAtual(List<Anime> listAtual) {
        this.listAtual = listAtual;
        notifyDataSetChanged();
    }

    public void apagar(final String identifier, final RecyclerView rec) {
        final Anime a = listCompleta.get(identifier);
        myRef.child(a.getIdentifier()).removeValue();
        int pos = -1;
        if (listAtual.contains(a)) {
            pos = listAtual.indexOf(a);
            listAtual.remove(a);
            rec.scrollToPosition(pos);
            notifyItemRemoved(pos);
        }
        final Snackbar snackbar = Snackbar.make(act.findViewById(R.id.btn_fab_add), "ANIME APAGADO", Snackbar.LENGTH_LONG);
        final int finalPos = pos;
        snackbar.setAction("DESFAZER", new View.OnClickListener() {

            private boolean clicou = true;

            @Override
            public void onClick(View view) {
                if (clicou) {
                    clicou = false;
                    snackbar.dismiss();
                    myRef.child(a.getIdentifier()).setValue(a);
                    if (finalPos != -1) {
                        listAtual.add(finalPos, a);
                        rec.scrollToPosition(finalPos);
                        notifyItemInserted(finalPos);
                    }
                }
            }
        });

        View sView = snackbar.getView();
        sView.setBackgroundColor(act.getResources().getColor(R.color.colorPrimary));

        snackbar.show();
    }

    public void atualizarItens() {
        listAtual = new ArrayList<>(listCompleta.values());
        notifyDataSetChanged();
    }
}
