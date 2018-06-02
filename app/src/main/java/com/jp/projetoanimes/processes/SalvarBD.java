package com.jp.projetoanimes.processes;

import android.app.Activity;
import android.util.Log;

import com.jp.projetoanimes.types.Anime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SalvarBD {

    private final String FILENAME1 = "animes_lista";
    private final String FILENAME2 = "conc_lista";
    private final String FILENAME3 = "sugestao_lista";

    private static List<Anime> listaAtual;
    private static List<Anime> listaConc;
    private static List<String> listaSuges;
    private Activity act;

    public SalvarBD(Activity act){
        this.act = act;
        if (listaAtual == null && listaConc == null && listaSuges == null){
            File fileAnime = act.getFileStreamPath(FILENAME1);
            File fileConc = act.getFileStreamPath(FILENAME2);
            File fileSug = act.getFileStreamPath(FILENAME3);
            try{
                FileInputStream fis = new FileInputStream(fileAnime);
                ObjectInputStream ois = new ObjectInputStream(fis);
                listaAtual = (List<Anime>) ois.readObject();
                fis.close();
                ois.close();
            }catch(Exception e) {
                Log.d("ERRO_LOAD", "Erro no load dos animes Atuais");
            }

            try{
                FileInputStream fis = new FileInputStream(fileConc);
                ObjectInputStream ois = new ObjectInputStream(fis);
                listaConc = (List<Anime>) ois.readObject();
                fis.close();
                ois.close();
            }catch(Exception e) {
                Log.d("ERRO_LOAD", "Erro no load dos animes Concluidos");
            }

            try{
                FileInputStream fis = new FileInputStream(fileSug);
                ObjectInputStream ois = new ObjectInputStream(fis);
                listaSuges = (List<String>) ois.readObject();
                fis.close();
                ois.close();
            }catch(Exception e) {
                Log.d("ERRO_LOAD", "Erro no load das sugestões");
            }
        }

        if (listaAtual == null){ listaAtual = new ArrayList<>();}
        if (listaConc == null){ listaConc = new ArrayList<>();}
        if (listaSuges == null){ listaSuges = new ArrayList<>();}
    }

    public List pegaLista(int i){
        switch (i){
            case 0:
                return listaAtual;
            case 1:
                return listaConc;
            case 2:
                return listaSuges;
        }
        return null;
    }

    public void salvaLista(int i, List lista){
        switch (i){
            case 0:
                listaAtual = lista;
                try{
                    File fileAnime = act.getFileStreamPath(FILENAME1);
                    FileOutputStream fos = new FileOutputStream(fileAnime);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(listaAtual);
                    oos.close();
                    fos.close();
                    return;
                }catch(Exception e){
                    return;
                }
            case 1:
                listaConc = lista;
                try{
                    File fileAnime = act.getFileStreamPath(FILENAME2);
                    FileOutputStream fos = new FileOutputStream(fileAnime);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(listaConc);
                    oos.close();
                    fos.close();
                    return;
                }catch(Exception e){
                    return;
                }
            case 2:
                listaSuges = lista;
                try{
                    File fileAnime = act.getFileStreamPath(FILENAME3);
                    FileOutputStream fos = new FileOutputStream(fileAnime);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(listaSuges);
                    oos.close();
                    fos.close();
                    return;
                }catch(Exception e){
                    return;
                }
        }
    }
}
