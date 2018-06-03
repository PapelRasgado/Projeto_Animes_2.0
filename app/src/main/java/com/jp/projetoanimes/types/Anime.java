package com.jp.projetoanimes.types;

import java.io.Serializable;
import java.util.Date;

public class Anime implements Serializable {

    private String nome;

    private int ep;

    private String notas;

    private int temp;

    private String image;

    private String link;

    private Date data;


    public Anime(String nome, int ep, int temp, String notas, String image, String link) {
        this.nome = nome;
        this.ep = ep;
        this.notas = notas;
        this.temp = temp;
        this.image = image;
        this.link = link;
        this.data = new Date();
    }

    public Anime() {
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

    @Override
    public String toString() {
        return nome;
    }
}

