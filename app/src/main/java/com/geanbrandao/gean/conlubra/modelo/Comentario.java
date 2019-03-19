package com.geanbrandao.gean.conlubra.modelo;

import com.google.firebase.firestore.FieldValue;

import java.util.List;

// colecao comentario
public class Comentario {

    private String idComentario; // indide

    private String autorComentario; // idUsuario
    private FieldValue dataComentario;
    private String conteudoComentario;
    private int contadorLikesComentario;
    private List<String> likesComentarioIdUsuario;

    public Comentario() {
    }

    public Comentario(String idComentario, String autorComentario, FieldValue dataComentario, String conteudoComentario, int contadorLikesComentario, List<String> likesComentarioIdUsuario) {
        this.idComentario = idComentario;
        this.autorComentario = autorComentario;
        this.dataComentario = dataComentario;
        this.conteudoComentario = conteudoComentario;
        this.contadorLikesComentario = contadorLikesComentario;
        this.likesComentarioIdUsuario = likesComentarioIdUsuario;
    }

    public String getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(String idComentario) {
        this.idComentario = idComentario;
    }

    public String getAutorComentario() {
        return autorComentario;
    }

    public void setAutorComentario(String autorComentario) {
        this.autorComentario = autorComentario;
    }

    public String getConteudoComentario() {
        return conteudoComentario;
    }

    public void setConteudoComentario(String conteudoComentario) {
        this.conteudoComentario = conteudoComentario;
    }

    public int getContadorLikesComentario() {
        return contadorLikesComentario;
    }

    public void setContadorLikesComentario(int contadorLikesComentario) {
        this.contadorLikesComentario = contadorLikesComentario;
    }

    public List<String> getLikesComentarioIdUsuario() {
        return likesComentarioIdUsuario;
    }

    public void setLikesComentarioIdUsuario(List<String> likesComentarioIdUsuario) {
        this.likesComentarioIdUsuario = likesComentarioIdUsuario;
    }

    public FieldValue getDataComentario() {
        return dataComentario;
    }

    public void setDataComentario(FieldValue dataComentario) {
        this.dataComentario = dataComentario;
    }
}




