package com.geanbrandao.gean.conlubra.modelo;

import com.google.firebase.firestore.FieldValue;

import java.util.List;

// colecao postagem
public class Postagem {

    private String idPostagem; // indice

    private String autorPostagem; // id do usuario
    private FieldValue dataPostagem;
    private String conteudoPostagem;
    private String imagemPostagem;
    private int contadorLikesPostagem;
    private int contadorComentariosPostagem;
    private List<String> likesPostagemIdUsuarios;
    private List<Comentario> comentariosPostagem;


    public Postagem() {
    }

    public Postagem(String idPostagem, String autorPostagem, FieldValue dataPostagem, String conteudoPostagem, String imagemPostagem, int contadorLikesPostagem, int contadorComentariosPostagem, List<String> likesPostagemIdUsuarios, List<Comentario> comentariosPostagem) {
        this.idPostagem = idPostagem;
        this.autorPostagem = autorPostagem;
        this.dataPostagem = dataPostagem;
        this.conteudoPostagem = conteudoPostagem;
        this.imagemPostagem = imagemPostagem;
        this.contadorLikesPostagem = contadorLikesPostagem;
        this.contadorComentariosPostagem = contadorComentariosPostagem;
        this.likesPostagemIdUsuarios = likesPostagemIdUsuarios;
        this.comentariosPostagem = comentariosPostagem;
    }

    public String getIdPostagem() {
        return idPostagem;
    }

    public void setIdPostagem(String idPostagem) {
        this.idPostagem = idPostagem;
    }

    public String getAutorPostagem() {
        return autorPostagem;
    }

    public void setAutorPostagem(String autorPostagem) {
        this.autorPostagem = autorPostagem;
    }

    public FieldValue getDataPostagem() {
        return dataPostagem;
    }

    public void setDataPostagem(FieldValue dataPostagem) {
        this.dataPostagem = dataPostagem;
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
