package com.jp.projetoanimes.types;

import android.support.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Anime implements Serializable, Comparable<Anime> {

    private String nome;

    private int ep;

    private String notas;

    private int temp;

    private String image;

    private String link;

    private Date data;

    private String identifier;

    private boolean star;

    private boolean lanc;

    private boolean agend;

    private List<Integer> dias;

    public static String order = "ABC";


    public Anime(String nome, int ep, int temp, String notas, String image, String link, List<Integer> dias , boolean lanc) {
        this.nome = nome;
        this.ep = ep;
        this.notas = notas;
        this.temp = temp;
        this.image = image;
        this.link = link;
        this.data = new Date();
        this.dias = dias;
        this.lanc = lanc;
    }

    public Anime(String nome, int ep, int temp, String notas, String image, String link, Date date, String identifier, List<Integer> dias, boolean lanc, boolean agend) {
        this.nome = nome;
        this.ep = ep;
        this.notas = notas;
        this.temp = temp;
        this.image = image;
        this.link = link;
        this.identifier = identifier;
        this.data = date;
        this.dias = dias;
        this.lanc = lanc;
        this.agend = agend;
    }

    public Anime() {

    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getNome() {
        return nome;
    }

    public int getEp() {
        return ep;
    }

    public String getNotas() {
        return notas;
    }

    public int getTemp() {
        return temp;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEp(int ep) {
        this.ep = ep;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void mudarEp(int i) {
        this.ep += i;
    }

    public void mudarTemp(int i) {
        this.temp += i;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == Anime.class && ((Anime) obj).identifier.equals(identifier);
    }

    @Override
    public int compareTo(@NonNull Anime o) {
        if (order.equals("ABC") || order.equals("CBA")){
            return nome.compareTo(o.nome);
        } else {
            return data.compareTo(o.getData());
        }
    }

    public static void setOrder(String newOrder){
        order = newOrder;
    }

    public void setDias(List<Integer> dias) {
        this.dias = dias;
    }

    public List<Integer> getDias() {
        return dias;
    }

    public void setLanc(boolean lanc) {
        this.lanc = lanc;
    }

    public boolean isLanc() {
        return lanc;
    }

    public boolean isAgend() {
        return agend;
    }

    public void setAgend(boolean agend) {
        this.agend = agend;
    }
}

