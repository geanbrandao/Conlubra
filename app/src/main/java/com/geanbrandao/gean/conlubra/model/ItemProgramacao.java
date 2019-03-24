package com.geanbrandao.gean.conlubra.model;

public class ItemProgramacao {

    private String id;

    private String hora;
    private String nome;
    private String local;
    private String detalhes;
    //private List<String> favoritadoPor;

    public ItemProgramacao() {
    }

    public ItemProgramacao(String id, String hora, String nome, String local, String detalhes) {
        this.id = id;
        this.hora = hora;
        this.nome = nome;
        this.local = local;
        this.detalhes = detalhes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }
}
