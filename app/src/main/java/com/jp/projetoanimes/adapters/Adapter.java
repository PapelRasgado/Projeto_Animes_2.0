package com.jp.projetoanimes.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.annotation.NonNull;
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
import com.jp.projetoanimes.activitys.DetailsActivity;
import com.jp.projetoanimes.processes.Codes;
import com.jp.projetoanimes.tasks.TrocaTask;
import com.jp.projetoanimes.types.Anime;
import com.jp.projetoanimes.interfaces.ItemTouchHelperAdapter;
import com.jp.projetoanimes.tasks.PesquisaTask;
import com.jp.projetoanimes.R;
import com.jp.projetoanimes.processes.SalvarBD;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class Adapter extends RecyclerView.Adapter<ViewHolder> implements ItemTouchHelperAdapter {

    private List<Anime> listCompleta;
    private SalvarBD sbd;
    private boolean ordenacao;
    private List<Anime> listAtual;
    private Activity act;

    public Adapter(Activity act, boolean ordenacao) {
        this.act = act;
        sbd = new SalvarBD(act);
        this.listCompleta = sbd.pegaLista(0);
        this.listAtual = new ArrayList<>(listCompleta);
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
                    notifyDataSetChanged();
                }
            }
        });
        holder.btnMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anime.mudarEp(+1);
                notifyDataSetChanged();
            }
        });
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(act, DetailsActivity.class);
                intent.putExtra("anime_detalhe", listCompleta.indexOf(anime));
                intent.putExtra("type", 0);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(act,
                                holder.img,
                                ViewCompat.getTransitionName(holder.img));
                act.startActivityForResult(intent, Codes.ANIME_DETAIL, options.toBundle());
            }
        });

        if (!anime.getImage().

                isEmpty())

        {
            Glide.with(holder.itemView.getContext())
                    .load(anime.getImage())
                    .into(holder.img);
        } else

        {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.anime)
                    .into(holder.img);
        }
    }

    @Override
    public int getItemCount() {
        return listAtual.size();
    }

    public void salvaLista() {
        sbd.salvaLista(0, listCompleta);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Integer[] ints = {fromPosition, toPosition};
        new TrocaTask(this).execute(ints);
    }

    @Override
    public void onItemDismiss(final int position) {
    }


    public void fazerPesquisa(boolean b, String nome) {

        if (b) {
            new PesquisaTask(this).execute(nome);
        } else {
            listAtual = new ArrayList<>(listCompleta);
        }
        notifyDataSetChanged();
    }

    public List<Anime> getListCompleta() {
        return listCompleta;
    }

    public List<Anime> getListAtual() {
        return listAtual;
    }

    public void setListAtual(List<Anime> listAtual) {
        this.listAtual = listAtual;
        notifyDataSetChanged();
    }

    public void apagar(final int position, final RecyclerView rec) {
        final Anime a = listCompleta.remove(position);
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
                    listCompleta.add(position, a);
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
        listCompleta = sbd.pegaLista(0);
        listAtual = new ArrayList<>(listCompleta);
        notifyDataSetChanged();
    }
}
