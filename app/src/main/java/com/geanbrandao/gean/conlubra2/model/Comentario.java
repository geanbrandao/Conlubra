package com.geanbrandao.gean.conlubra2.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

// colecao comentario
public class Comentario {

    private String idComentario; // indice

    private String nomeAutorComentario; // idUsuario
    private String fotoAutorPostagem;
    @ServerTimestamp
    private Date dataComentario;
    private String conteudoComentario;
    private int contadorLikesComentario;
    private List<String> likesComentarioIdUsuario;

    public Comentario() {
    }

    public Comentario(String idComentario, String nomeAutorComentario, String fotoAutorPostagem, Date dataComentario, String conteudoComentario, int contadorLikesComentario, List<String> likesComentarioIdUsuario) {
        this.idComentario = idComentario;
        this.nomeAutorComentario = nomeAutorComentario;
        this.fotoAutorPostagem = fotoAutorPostagem;
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

    public String getNomeAutorComentario() {
        return nomeAutorComentario;
    }

    public void setNomeAutorComentario(String nomeAutorComentario) {
        this.nomeAutorComentario = nomeAutorComentario;
    }

    public String getFotoAutorPostagem() {
        return fotoAutorPostagem;
    }

    public void setFotoAutorPostagem(String fotoAutorPostagem) {
        this.fotoAutorPostagem = fotoAutorPostagem;
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

    public Date getDataComentario() {
        return dataComentario;
    }

    public void setDataComentario(Date dataComentario) {
        this.dataComentario = dataComentario;
    }
}




