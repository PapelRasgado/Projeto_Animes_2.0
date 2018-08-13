package com.jp.projetoanimes.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jp.projetoanimes.R;
import com.jp.projetoanimes.activitys.AdicionarActivity;
import com.jp.projetoanimes.tasks.PesquisaSuTask;
import com.jp.projetoanimes.types.Codes;
import com.jp.projetoanimes.types.FirebaseManager;
import com.jp.projetoanimes.types.Sugestao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AdapterSu extends RecyclerView.Adapter<ViewHolderSu> {

    private DatabaseReference myRef;

    private HashMap<String, Sugestao> listCompleta;
    private List<Sugestao> listAtual;
    private Activity act;


    public AdapterSu(Activity act, String user) {
        this.act = act;

        listCompleta = new HashMap<>();
        FirebaseDatabase database = FirebaseManager.getDatabase();
        myRef = database.getReference(user + "/listaSuge");
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                listCompleta.put(s, dataSnapshot.getValue(Sugestao.class));
                listAtual.add(dataSnapshot.getValue(Sugestao.class));
                notifyItemInserted(listAtual.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Sugestao sug = dataSnapshot.getValue(Sugestao.class);
                listCompleta.put(sug.getIdentifier(), sug);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                listCompleta.remove(dataSnapshot.getKey());
                listAtual.remove(dataSnapshot.getValue(Sugestao.class));
                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //faz nada
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //faz nada
            }
        };

        myRef.addChildEventListener(listener);

        this.listAtual = new ArrayList<>(listCompleta.values());
    }

    @NonNull
    @Override
    public ViewHolderSu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_su_linear, parent, false);

        return new ViewHolderSu(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderSu holder, int position) {
        final Sugestao sug = listAtual.get(position);
        holder.titulo.setText(sug.getNome());

        switch (sug.getPrioridade()) {
            case 0:
                holder.background.setBackgroundColor(act.getResources().getColor(R.color.red));
                break;
            case 1:
                holder.background.setBackgroundColor(act.getResources().getColor(R.color.yellow_red));
                break;
            case 2:
                holder.background.setBackgroundColor(act.getResources().getColor(R.color.yellow));
                break;
            case 3:
                holder.background.setBackgroundColor(act.getResources().getColor(R.color.green_yellow));
                break;
            case 4:
                holder.background.setBackgroundColor(act.getResources().getColor(R.color.green));
                break;
        }

        holder.titulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) act.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Nome do anime", sug.getNome());
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(act, "Nome copiado!", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(sug);
            }
        });
        holder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listAtual.size();
    }

    private void send(final int position) {
        final String a = listAtual.get(position).getNome();
        new AlertDialog.Builder(act, R.style.AlertTheme)
                .setTitle("Deseja enviar esse anime para a tela de edição?")
                .setMessage("Nome: " + a)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent it = new Intent(act, AdicionarActivity.class);
                        it.putExtra("anime_su_nome", a);
                        it.putExtra("type", 0);
                        myRef.child(a).removeValue();
                        listAtual.remove(a);
                        notifyItemRemoved(position);
                        act.startActivityForResult(it, Codes.ANIME_ADD);

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


    private void delete(final Sugestao sug) {
        new AlertDialog.Builder(act, R.style.AlertTheme)
                .setTitle("Deseja apagar esse anime?")
                .setMessage("Nome: " + sug.getNome())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final int index = listAtual.indexOf(sug);
                        notifyItemRemoved(index);
                        myRef.child(sug.getIdentifier()).removeValue();
                        listAtual.remove(sug);

                        final Snackbar snackbar = Snackbar.make(act.findViewById(R.id.btn_fab_add), "ANIME APAGADO", Snackbar.LENGTH_LONG);
                        snackbar.setAction("DESFAZER", new View.OnClickListener() {

                            private boolean clicou = true;

                            @Override
                            public void onClick(View view) {
                                if (clicou) {
                                    clicou = false;
                                    snackbar.dismiss();
                                    myRef.child(sug.getIdentifier()).setValue(sug);
                                    notifyItemInserted(index);
                                }
                            }
                        });

                        View sView = snackbar.getView();
                        sView.setBackgroundColor(act.getResources().getColor(R.color.colorPrimary));

                        snackbar.show();

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //
                    }
                })
                .show();
    }

    public void adicionar(Sugestao sug) {

        DatabaseReference data = myRef.push();
        sug.setIdentifier(data.getKey());
        data.setValue(sug);
    }

    public void fazerPesquisa(boolean b, String nome) {

        if (b) {
            new PesquisaSuTask(this).execute(nome);
        } else {
            listAtual = new ArrayList<>(listCompleta.values());
            mudarOrder();
        }
        notifyDataSetChanged();
    }

    public HashMap<String, Sugestao> getListCompleta() {
        return listCompleta;
    }


    public void setListAtual(List<Sugestao> listAtual) {
        this.listAtual = listAtual;
        mudarOrder();
        notifyDataSetChanged();
    }

    private void mudarOrder() {
        Collections.sort(listAtual);
        notifyDataSetChanged();
    }

    public void atualizarItens() {
        listAtual = new ArrayList<>(listCompleta.values());
        mudarOrder();
    }
}
