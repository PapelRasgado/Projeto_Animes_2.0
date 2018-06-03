package com.jp.projetoanimes.activitys;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jp.projetoanimes.R;
import com.jp.projetoanimes.processes.Codes;
import com.jp.projetoanimes.processes.SalvarBD;
import com.jp.projetoanimes.types.Anime;

import java.util.List;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    private Anime anime;
    private int animeP;
    private boolean mod;
    private int type;

    private ImageView img;
    private AppCompatTextView txtName;
    private AppCompatTextView txtEp;
    private AppCompatTextView txtTemp;
    private AppCompatTextView txtNotas;
    private AppCompatTextView txtImg;
    private AppCompatTextView txtLink;

    private AppCompatImageButton copyName;
    private AppCompatImageButton copyImage;
    private AppCompatImageButton copyLink;

    private AppCompatButton maisEp;
    private AppCompatButton menosEp;
    private AppCompatButton maisTemp;
    private AppCompatButton menosTemp;

    private FloatingActionButton link;

    private ClipboardManager clipboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        clipboard  = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        img = findViewById(R.id.img_detail);

        txtName = findViewById(R.id.txt_name);
        txtEp = findViewById(R.id.txt_ep);
        txtTemp =  findViewById(R.id.txt_temp);
        txtNotas = findViewById(R.id.txt_note);
        txtImg = findViewById(R.id.txt_img);
        txtLink = findViewById(R.id.txt_link);

        copyName = findViewById(R.id.btn_copy_name);
        copyImage = findViewById(R.id.btn_copy_img);
        copyLink = findViewById(R.id.btn_copy_link);

        maisEp = findViewById(R.id.btn_detail_mais_ep);
        menosEp = findViewById(R.id.btn_detail_menos_ep);

        maisTemp = findViewById(R.id.btn_detail_mais_temp);
        menosTemp = findViewById(R.id.btn_detail_menos_temp);

        link = findViewById(R.id.btn_fab_open);

        animeP = getIntent().getIntExtra("anime_detalhe", -1) ;
        type = getIntent().getIntExtra("type", -1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        }

        mudarDados();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);

        return true;
    }

    public void mudarDados(){
        anime = (Anime) new SalvarBD(this).pegaLista(type).get(animeP);


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            Display getOrient = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            getOrient.getSize(size);
            img.getLayoutParams().height = ((size.x)*9)/16;
        }


        if (!anime.getImage().isEmpty()){
            Glide.with(this).load(anime.getImage()).into(img);
        }

        txtName.setText(anime.getNome());

        copyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipData clip = ClipData.newPlainText("Nome do anime", anime.getNome());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext() , "Nome copiado!", Toast.LENGTH_SHORT).show();
            }
        });

        txtEp.setText(String.valueOf(anime.getEp()));

        txtTemp.setText(String.valueOf(anime.getTemp()));

        if (!anime.getNotas().isEmpty()){
            txtNotas.setText(anime.getNotas());
        } else {
            txtNotas.setVisibility(View.GONE);
        }

        if (!anime.getImage().isEmpty()){
            txtImg.setText(anime.getImage());
            txtImg.setPaintFlags(txtImg.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
            copyImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipData clip = ClipData.newPlainText("Link da imagem", anime.getImage());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext() , "Imagem copiada!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            txtImg.setVisibility(View.GONE);
            copyImage.setVisibility(View.GONE);
        }

        if (!anime.getLink().isEmpty()){
            txtLink.setText(anime.getLink());
            txtLink.setPaintFlags(txtLink.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
            copyLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipData clip = ClipData.newPlainText("Link do anime", anime.getLink());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext() , "Link copiado!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            txtLink.setVisibility(View.GONE);
            copyLink.setVisibility(View.GONE);
        }

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (anime.getLink().isEmpty()){
                    Toast.makeText(getApplicationContext() , "Não possui link salvo!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(anime.getLink()));
                    try {
                        getApplicationContext().startActivity(it);
                    } catch (Exception e){
                        Toast.makeText(getApplicationContext() , "Link Invalido!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        maisEp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 0) {
                    setResult(Codes.ANIME_MODIFY);
                } else {
                    setResult(Codes.ANIME_MODIFY_CONC);
                }
                mod = true;
                anime.mudarEp(1);
                txtEp.setText(String.valueOf(anime.getEp()));
            }
        });

        menosEp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (anime.getEp() > 1){
                    mod = true;
                    if (type == 0) {
                        setResult(Codes.ANIME_MODIFY);
                    } else {
                        setResult(Codes.ANIME_MODIFY_CONC);
                    }
                    anime.mudarEp(-1);
                    txtEp.setText(String.valueOf(anime.getEp()));
                }
            }
        });

        maisTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mod = true;
                if (type == 0) {
                    setResult(Codes.ANIME_MODIFY);
                } else {
                    setResult(Codes.ANIME_MODIFY_CONC);
                }
                anime.mudarTemp(1);
                txtTemp.setText(String.valueOf(anime.getTemp()));
            }
        });

        menosTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (anime.getTemp() > 1){
                    mod = true;
                    if (type == 0) {
                        setResult(Codes.ANIME_MODIFY);
                    } else {
                        setResult(Codes.ANIME_MODIFY_CONC);
                    }
                    anime.mudarTemp(-1);
                    txtTemp.setText(String.valueOf(anime.getTemp()));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_detail_del:
                new AlertDialog.Builder(DetailsActivity.this, R.style.AlertTheme)
                        .setTitle("Deseja apagar esse anime?")
                        .setMessage("Nome: " + anime.getNome())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent it = new Intent();
                                it.putExtra("apagar", animeP);
                                if (type == 0){
                                    setResult(Codes.ANIME_DELETE, it);
                                } else {
                                    setResult(Codes.ANIME_DELETE_CONC, it);
                                }
                                onBackPressed();

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
                break;
            case R.id.menu_detail_edit:
                Intent it = new Intent(DetailsActivity.this, EditActivity.class);
                it.putExtra("anime_detalhe", animeP);
                it.putExtra("type", type);
                startActivityForResult(it, Codes.ANIME_EDIT);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_detail_send:
                if (type == 0){
                    new AlertDialog.Builder(DetailsActivity.this, R.style.AlertTheme)
                            .setTitle("Deseja mandar esse anime pra lista de concluidos?")
                            .setMessage("Nome: " + anime.getNome())
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new SalvarBD(DetailsActivity.this).change(0,1, anime);
                                    setResult(Codes.ANIME_MODIFY);
                                    onBackPressed();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                } else {
                    new AlertDialog.Builder(DetailsActivity.this, R.style.AlertTheme)
                            .setTitle("Deseja enviar esse anime de volta para a lista de atuais?")
                            .setMessage("Nome: " + anime.getNome())
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new SalvarBD(DetailsActivity.this).change(1,0, anime);
                                    setResult(Codes.ANIME_MODIFY_CONC);
                                    onBackPressed();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        link.setVisibility(View.GONE);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Codes.ANIME_EDIT && resultCode == Codes.ANIME_MODIFY){
            if (type == 0) {
                setResult(Codes.ANIME_MODIFY);
            } else {
                setResult(Codes.ANIME_MODIFY_CONC);
            }
            mod = true;
            mudarDados();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onStop() {
        if (mod) {
            List<Anime> list = new SalvarBD(this).pegaLista(type);
            list.set(animeP, anime);
            new SalvarBD(this).salvaLista(type, list);
        }
        super.onStop();
    }


}
