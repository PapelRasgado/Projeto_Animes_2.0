package com.jp.projetoanimes.types;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Sugestao implements Serializable, Comparable<Sugestao> {

    private String nome;

    private Date data;

    private String identifier;

    private int prioridade;

    public Sugestao(String nome, Date data, String identifier, int prioridade) {
        this(nome,prioridade);
        this.data = data;
        this.identifier = identifier;
    }

    public Sugestao(String nome, int prioridade) {
        this.nome = nome;
        this.data = new Date();
        this.prioridade = prioridade;
    }

    public  Sugestao(){}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    @Override
    public int compareTo(@NonNull Sugestao o) {
        switch (Codes.order) {
            case "ABC":
                return nome.compareTo(o.nome);
            case "CBA":
                return nome.compareTo(o.nome) * -1;
            case "123":
                return data.compareTo(o.getData());
            default:
                return data.compareTo(o.getData()) * -1;
        }
    }
}
