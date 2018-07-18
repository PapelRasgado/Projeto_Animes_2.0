package com.jp.projetoanimes.activitys;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jp.projetoanimes.R;
import com.jp.projetoanimes.adapters.TabsAdapter;
import com.jp.projetoanimes.fragments.AtualFragment;
import com.jp.projetoanimes.fragments.ConcluidoFragment;
import com.jp.projetoanimes.fragments.SugestaoFragment;
import com.jp.projetoanimes.types.Anime;
import com.jp.projetoanimes.types.Codes;
import com.jp.projetoanimes.types.FirebaseManager;
import com.jp.projetoanimes.types.InputDialog;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity {


    private FloatingActionButton fab;
    private AtualFragment atual;
    private SugestaoFragment suges;
    private ConcluidoFragment conc;
    private MaterialSearchView mSearch;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    static private int tabAtual = -1;

    static private boolean ordenacao;

    private FirebaseDatabase database;

    static private MenuItem reorder;
    static private MenuItem ordering;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        database = FirebaseManager.getDatabase();
        Fabric.with(this, new Crashlytics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());

        suges = new SugestaoFragment();
        adapter.add(suges, "Sugestões");

        atual = new AtualFragment();
        adapter.add(atual, "Atual");

        conc = new ConcluidoFragment();
        adapter.add(conc, "Concluidos");

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mSearch = findViewById(R.id.search_bar);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mSearch.isSearchOpen()) {
                    mSearch.closeSearch();
                }
                tabAtual = tab.getPosition();
                switch (tabAtual) {
                    case 1:
                        AtualFragment.getAdapter().atualizarItens();
                        break;
                    case 2:
                        ConcluidoFragment.getAdapter().atualizarItens();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        selectPage();
        tabAtual = 1;

        fab = findViewById(R.id.btn_fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (tabAtual) {
                    case 0:
                        new InputDialog(MainActivity.this, suges).show();
                        break;
                    case 1:
                        Intent it = new Intent(MainActivity.this, AdicionarActivity.class);
                        it.putExtra("type", 0);
                        startActivityForResult(it, Codes.ANIME_ADD);
                        break;
                    case 2:
                        Intent ite = new Intent(MainActivity.this, AdicionarActivity.class);
                        ite.putExtra("type", 1);
                        startActivityForResult(ite, Codes.ANIME_ADD_CONC);
                }
            }
        });

        mSearch.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                switch (tabAtual) {
                    case 0:
                        suges.fazerPesquisa(true, newText);
                        break;
                    case 1:
                        atual.fazerPesquisa(true, newText);
                        break;
                    case 2:
                        conc.fazerPesquisa(true, newText);
                        break;
                }
                return true;
            }
        });

        mSearch.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                fab.setVisibility(View.GONE);
            }

            @Override
            public void onSearchViewClosed() {
                fab.setVisibility(View.VISIBLE);
                switch (tabAtual) {
                    case 0:
                        suges.fazerPesquisa(false, null);
                        break;
                    case 1:
                        atual.fazerPesquisa(false, null);
                        break;
                    case 2:
                        conc.fazerPesquisa(false, null);
                        break;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.search);
        mSearch.setMenuItem(item);

        reorder = menu.findItem(R.id.list);
        ordering = menu.findItem(R.id.order);

        database.getReference(FirebaseManager.getAuth().getUid()).child("order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Anime.setOrder(dataSnapshot.getValue(String.class));
                if (Anime.order.equals("ABC") || Anime.order.equals("CBA")) {
                    ordering.setIcon(R.drawable.ic_alphabetical);
                } else {
                    ordering.setIcon(R.drawable.ic_timer);
                }
                atual.getAdapter().atualizarItens();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        database.getReference(FirebaseManager.getAuth().getUid()).child("ordenacao").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    ordenacao = dataSnapshot.getValue(Boolean.class);
                    if (ordenacao) {
                        reorder.setIcon(R.drawable.ic_grid);
                        atual.mudarOrdenacao(ordenacao);
                        conc.mudarOrdenacao(ordenacao);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list:
                if (!ordenacao) {
                    item.setIcon(R.drawable.ic_grid);
                    ordenacao = true;

                } else {
                    item.setIcon(R.drawable.ic_linear);
                    ordenacao = false;
                }

                database.getReference(FirebaseManager.getAuth().getUid()).child("ordenacao").setValue(ordenacao);

                atual.mudarOrdenacao(ordenacao);
                conc.mudarOrdenacao(ordenacao);
                break;
            case R.id.config:
                Intent it = new Intent(MainActivity.this, ConfigActivity.class);
                startActivityForResult(it, Codes.CONFIG_OPEN);
                break;
            case R.id.about:
                break;
            case R.id.order:
                switch (Anime.order){
                    case "ABC":
                        Anime.setOrder("CBA");
                        Toast.makeText(this, "Ordenado por nome decrescente!", Toast.LENGTH_SHORT).show();
                        break;
                    case "CBA":
                        Anime.setOrder("123");
                        Toast.makeText(this, "Ordenado por data crescente!", Toast.LENGTH_SHORT).show();
                        break;
                    case "123":
                        Anime.setOrder("321");
                        Toast.makeText(this, "Ordenado por data decrescente!", Toast.LENGTH_SHORT).show();
                        break;
                    case "321":
                        Anime.setOrder("ABC");
                        Toast.makeText(this, "Ordenado por nome crescente!", Toast.LENGTH_SHORT).show();
                        break;
                }
                AtualFragment.getAdapter().atualizarItens();
                ConcluidoFragment.getAdapter().atualizarItens();
                if (Anime.order.equals("ABC") || Anime.order.equals("CBA")) {
                    item.setIcon(R.drawable.ic_alphabetical);
                } else {
                    item.setIcon(R.drawable.ic_timer);
                }

                database.getReference(FirebaseManager.getAuth().getUid()).child("order").setValue(Anime.order);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Codes.ANIME_DETAIL && resultCode == Codes.ANIME_DELETE) {
            atual.apagar(data.getStringExtra("apagar"));
        } else if (requestCode == Codes.ANIME_DETAIL && resultCode == Codes.ANIME_DELETE_CONC) {
            conc.apagar(data.getStringExtra("apagar"));
        } else if (requestCode == Codes.ANIME_DETAIL && resultCode == Codes.ANIME_MODIFY) {
            if (mSearch.isSearchOpen()) {
                mSearch.closeSearch();
            }
            AtualFragment.getAdapter().atualizarItens();
        } else if (requestCode == Codes.ANIME_DETAIL && resultCode == Codes.ANIME_MODIFY_CONC) {
            if (mSearch.isSearchOpen()) {
                mSearch.closeSearch();
            }
            ConcluidoFragment.getAdapter().atualizarItens();
        } else if (requestCode == Codes.ANIME_ADD && resultCode == Codes.ANIME_ADDED) {
            if (mSearch.isSearchOpen()) {
                mSearch.closeSearch();
            }
        } else if (requestCode == Codes.ANIME_ADD_CONC && resultCode == Codes.ANIME_ADDED_CONC) {
            if (mSearch.isSearchOpen()) {
                mSearch.closeSearch();
            }
        } else if (requestCode == Codes.CONFIG_OPEN && resultCode == Codes.CONFIG_LOGOUT) {
            finish();
            Intent it = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(it);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (mSearch.isSearchOpen()) {
            mSearch.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    void selectPage() {
        tabLayout.setScrollPosition(1, 0f, true);
        viewPager.setCurrentItem(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
