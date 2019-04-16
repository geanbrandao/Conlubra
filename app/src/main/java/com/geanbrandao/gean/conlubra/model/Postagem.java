package com.geanbrandao.gean.conlubra.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

// colecao postagem
public class Postagem {

    private String idPostagem; // indice

    private String fotoAutorPostagem;
    private String nomeAutorPostagem;

    @ServerTimestamp
    private Date dataPostagem;

    private String conteudoPostagem;
    private String imagemPostagem;
    private int contadorLikesPostagem;
    private int contadorComentariosPostagem;
    private List<String> likesPostagemIdUsuarios;
    private List<Comentario> comentariosPostagem;

    public Postagem() {
    }

    public Postagem(String idPostagem, String fotoAutorPostagem, String nomeAutorPostagem, Date dataPostagem, String conteudoPostagem, String imagemPostagem, int contadorLikesPostagem, int contadorComentariosPostagem, List<String> likesPostagemIdUsuarios, List<Comentario> comentariosPostagem) {
        this.idPostagem = idPostagem;
        this.fotoAutorPostagem = fotoAutorPostagem;
        this.nomeAutorPostagem = nomeAutorPostagem;
        this.dataPostagem = dataPostagem;
        this.conteudoPostagem = conteudoPostagem;
        this.imagemPostagem = imagemPostagem;
        this.contadorLikesPostagem = contadorLikesPostagem;
        this.contadorComentariosPostagem = contadorComentariosPostagem;
        this.likesPostagemIdUsuarios = likesPostagemIdUsuarios;
        this.comentariosPostagem = comentariosPostagem;
    }

    public String getFotoAutorPostagem() {
        return fotoAutorPostagem;
    }

    public void setFotoAutorPostagem(String fotoAutorPostagem) {
        this.fotoAutorPostagem = fotoAutorPostagem;
    }

    public String getNomeAutorPostagem() {
        return nomeAutorPostagem;
    }

    public void setNomeAutorPostagem(String nomeAutorPostagem) {
        this.nomeAutorPostagem = nomeAutorPostagem;
    }

    public Date getDataPostagem() {
        return dataPostagem;
    }

    public void setDataPostagem(Date dataPostagem) {
        this.dataPostagem = dataPostagem;
    }

    public String getIdPostagem() {
        return idPostagem;
    }

    public void setIdPostagem(String idPostagem) {
        this.idPostagem = idPostagem;
    }

    public String getConteudoPostagem() {
        return conteudoPostagem;
    }

    public void setConteudoPostagem(String conteudoPostagem) {
        this.conteudoPostagem = conteudoPostagem;
    }

    public String getImagemPostagem() {
        return imagemPostagem;
    }

    public void setImagemPostagem(String imagemPostagem) {
        this.imagemPostagem = imagemPostagem;
    }

    public int getContadorLikesPostagem() {
        return contadorLikesPostagem;
    }

    public void setContadorLikesPostagem(int contadorLikesPostagem) {
        this.contadorLikesPostagem = contadorLikesPostagem;
    }

    public int getContadorComentariosPostagem() {
        return contadorComentariosPostagem;
    }

    public void setContadorComentariosPostagem(int contadorComentariosPostagem) {
        this.contadorComentariosPostagem = contadorComentariosPostagem;
    }

    public List<String> getLikesPostagemIdUsuarios() {
        return likesPostagemIdUsuarios;
    }

    public void setLikesPostagemIdUsuarios(List<String> likesPostagemIdUsuarios) {
        this.likesPostagemIdUsuarios = likesPostagemIdUsuarios;
    }

    public List<Comentario> getComentariosPostagem() {
        return comentariosPostagem;
    }

    public void setComentariosPostagem(List<Comentario> comentariosPostagem) {
        this.comentariosPostagem = comentariosPostagem;
    }
}
