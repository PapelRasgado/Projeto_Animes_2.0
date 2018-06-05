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
import com.jp.projetoanimes.interfaces.ItemTouchHelperAdapter;
import com.jp.projetoanimes.processes.Codes;
import com.jp.projetoanimes.tasks.PesquisaSuTask;
import com.jp.projetoanimes.tasks.TrocaSuTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterSu extends RecyclerView.Adapter<ViewHolderSu> implements ItemTouchHelperAdapter {

    private FirebaseDatabase database;

    private HashMap<String, String> listCompleta;
    private List<String> listAtual;
    private Activity act;
    private String user;


    @SuppressWarnings("unchecked")
    public AdapterSu(Activity act, String user) {
        this.act = act;
        this.user = user;
        listCompleta = new HashMap<>();
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(this.user + "/listaSug");
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                listCompleta.put(s, dataSnapshot.getValue(String.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                listCompleta.put(s, dataSnapshot.getValue(String.class));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                listCompleta.remove(dataSnapshot.getKey());
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

//        sbd = new SalvarBD(act);
//        this.listCompleta = sbd.pegaLista(2);
        this.listAtual = (ArrayList<String>) listCompleta.values();
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
        final String anime = listAtual.get(position);
        holder.titulo.setText(anime);
        holder.titulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) act.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Nome do anime", anime);
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(act, "Nome copiado!", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete
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
        final String a = listAtual.get(position);
        new AlertDialog.Builder(act, R.style.AlertTheme)
                .setTitle("Deseja enviar esse anime para a tela de edição?")
                .setMessage("Nome: " + a)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent it = new Intent(act, AdicionarActivity.class);
                        it.putExtra("anime_su_nome", a);
                        it.putExtra("type", 0);
                        listCompleta.remove(a);
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

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Integer[] ints = {fromPosition, toPosition};
        new TrocaSuTask(this).execute(ints);
    }

    @Override
    public void onItemDismiss(final int position) {

    }

    private void delete(final int position) {
        final String a = listAtual.get(position);
        new AlertDialog.Builder(act, R.style.AlertTheme)
                .setTitle("Deseja apagar esse anime?")
                .setMessage("Nome: " + a)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notifyItemRemoved(position);
                        int pos = listCompleta.indexOf(a);
                        listCompleta.remove(a);
                        listAtual.remove(a);

                        final Snackbar snackbar = Snackbar.make(act.findViewById(R.id.btn_fab_add), "ANIME APAGADO", Snackbar.LENGTH_LONG);
                        final int finalPos = pos;
                        snackbar.setAction("DESFAZER", new View.OnClickListener() {

                            private boolean clicou = true;

                            @Override
                            public void onClick(View view) {
                                if (clicou) {
                                    clicou = false;
                                    snackbar.dismiss();
                                    listCompleta.add(finalPos, a);
                                    listAtual.add(position, a);
                                    notifyItemInserted(finalPos);
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
                        notifyItemChanged(position);
                    }
                })
                .show();
    }

    public void fazerPesquisa(boolean b, String nome) {
        DatabaseReference myRef = database.getReference(this.user + "/listaSug");
        myRef.orderByChild("nome").
        if (b) {
            new PesquisaSuTask(this).execute(nome);
        } else {
            listAtual = new ArrayList<String>(listCompleta.values());
        }
        notifyDataSetChanged();
    }

    public HashMap<String, String> getListCompleta() {
        return listCompleta;
    }

    public List<String> getListAtual() {
        return listAtual;
    }

    public void setListAtual(List<String> listAtual) {
        this.listAtual = listAtual;
        notifyDataSetChanged();
    }
}
