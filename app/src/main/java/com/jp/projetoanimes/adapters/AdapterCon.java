package com.jp.projetoanimes.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
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
import com.jp.projetoanimes.R;
import com.jp.projetoanimes.activitys.DetailsActivity;
import com.jp.projetoanimes.processes.Codes;
import com.jp.projetoanimes.tasks.PesquisaConTask;
import com.jp.projetoanimes.types.Anime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterCon extends RecyclerView.Adapter<ViewHolderCon>{

    DatabaseReference myRef;
    DatabaseReference usuRef;

    private HashMap<String, Anime> listCompleta;
    private boolean ordenacao;
    private List<Anime> listAtual;
    private Activity act;

    public AdapterCon(Activity act, boolean ordenacao) {
        this.act = act;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        myRef = database.getReference(auth.getUid()).child("listaConc");
        usuRef = database.getReference(auth.getUid());
        this.listCompleta = new HashMap<>();
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                listCompleta.put(s, dataSnapshot.getValue(Anime.class));
                listAtual.add(dataSnapshot.getValue(Anime.class));
                notifyItemInserted(listAtual.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                listCompleta.put(s, dataSnapshot.getValue(Anime.class));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                listCompleta.remove(dataSnapshot.getKey());
                listAtual.remove(dataSnapshot.getValue(Anime.class));
                notifyDataSetChanged();
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
    public ViewHolderCon onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;

        if (ordenacao) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conc_grid, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conc_linear, parent, false);
        }

        return new ViewHolderCon(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderCon holder, int position) {
        final Anime anime = listAtual.get(position);

        holder.titulo.setText(anime.getNome());
        holder.titulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard  = (ClipboardManager) act.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Nome do anime", anime.getNome());
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(act , "Nome copiado!", Toast.LENGTH_SHORT).show();
            }
        });

        Display getOrient = act.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        getOrient.getSize(size);
        if(ordenacao){
            if (act.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                holder.img.getLayoutParams().height = ((((size.x)/3)-32)*9)/16;
            }else{
                holder.img.getLayoutParams().height = ((((size.x)/2)-32)*9)/16;
            }
        } else {
            holder.img.getLayoutParams().height = ((size.x-48)*9)/16;
        }

        holder.undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send(holder.getAdapterPosition());
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(act, R.style.AlertTheme)
                        .setTitle("Deseja apagar esse anime?")
                        .setMessage("Nome: " + anime.getNome())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                apagar(anime.getIdentifier(), null);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(act, DetailsActivity.class);
                intent.putExtra("anime_detalhe", anime.getIdentifier());
                intent.putExtra("type", 1);
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

    private void send(final int position){
        final Anime a = listAtual.get(position);
        new AlertDialog.Builder(act, R.style.AlertTheme)
                .setTitle("Deseja enviar esse anime de volta para a lista de atuais?")
                .setMessage("Nome: " + a.getNome())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myRef.child(a.getIdentifier()).removeValue();
                        usuRef.child("listaAtu").child(a.getIdentifier()).setValue(a);
                        notifyItemRemoved(position);

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                })
                .show();

    }


    public void fazerPesquisa(boolean b, String nome) {

        if (b) {
            new PesquisaConTask(this).execute(nome);
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
            if (rec != null) {
                rec.scrollToPosition(pos);
            }
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
                        if (rec != null) {
                            rec.scrollToPosition(finalPos);
                        }
                        notifyItemInserted(finalPos);
                    }
                }
            }
        });

        View sView = snackbar.getView();
        sView.setBackgroundColor(act.getResources().getColor(R.color.colorPrimary));

        snackbar.show();
    }



    public void atualizarItens(){
        listAtual = new ArrayList<>(listCompleta.values());
        notifyDataSetChanged();
    }
}
