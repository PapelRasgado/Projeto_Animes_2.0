package com.jp.projetoanimes.activitys;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jp.projetoanimes.R;
import com.jp.projetoanimes.adapters.TabsAdapter;
import com.jp.projetoanimes.fragments.AtualFragment;
import com.jp.projetoanimes.fragments.SugestaoFragment;
import com.jp.projetoanimes.processes.Codes;
import com.jp.projetoanimes.types.InputDialog;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private AtualFragment atual;
    private SugestaoFragment suges;
    private MaterialSearchView mSearch;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private int tabAtual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());

        suges = new SugestaoFragment();
        adapter.add(suges, "Sugestões");

        atual = new AtualFragment();
        adapter.add(atual, "Atual");


        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);


        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        selectPage();
        tabAtual = 1;

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mSearch.isSearchOpen()){
                    mSearch.closeSearch();
                }
                tabAtual = tab.getPosition();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        fab = findViewById(R.id.btn_fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (tabAtual){
                    case 0:
                        new InputDialog(MainActivity.this, suges).show();
                        break;
                    case 1:
                        Intent it = new Intent(MainActivity.this, AdicionarActivity.class);
                        startActivityForResult(it, Codes.ANIME_ADD);
                        break;
                }
            }
        });

        mSearch = findViewById(R.id.search_bar);
        mSearch.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                switch (tabAtual){
                    case 0:
                        suges.fazerPesquisa(true, newText);
                        break;
                    case 1:
                        atual.fazerPesquisa(true, newText);
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
                switch (tabAtual){
                    case 0:
                        suges.fazerPesquisa(false, null);
                        break;
                    case 1:
                        atual.fazerPesquisa(false, null);
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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reorder:
                atual.mudarOrdenacao();
                break;
            case R.id.config:
                break;
            case R.id.about:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Codes.ANIME_DETAIL && resultCode == Codes.ANIME_DELETE) {
            atual.apagar(data.getIntExtra("apagar", -1));
        } else if (requestCode == Codes.ANIME_DETAIL && resultCode == Codes.ANIME_MODIFY){
            atual.modificar();
        } else if (requestCode == Codes.ANIME_ADD && resultCode == Codes.ANIME_ADDED){
            atual.adiciona();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (mSearch.isSearchOpen()){
            mSearch.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    void selectPage(){
        tabLayout.setScrollPosition(1,0f,true);
        viewPager.setCurrentItem(1);
    }
}
