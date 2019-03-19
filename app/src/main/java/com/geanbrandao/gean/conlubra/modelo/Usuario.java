package com.geanbrandao.gean.conlubra.modelo;

import java.util.List;

// colecao usuario
public class Usuario {

    private String idUsuario; // indice - email

    private String Nome;
    private String email;
    private String imagemPerfilUrl;
    private int contadorPostagem;
    private int contadorComentario;
    private List<String> idsPostagens;
    private List<String> idsPostagemCurtidas;
    private List<String> idsItensFavoritos;
    private List<String> idsComentario;
    private List<String> idsComentarioCurtidos;

    public Usuario(String idUsuario, String nome, String email, String imagemPerfilUrl, int contadorPostagem, int contadorComentario, List<String> idsPostagens, List<String> idsPostagemCurtidas, List<String> idsItensFavoritos, List<String> idsComentario, List<String> idsComentarioCurtidos) {
        this.idUsuario = idUsuario;
        Nome = nome;
        this.email = email;
        this.imagemPerfilUrl = imagemPerfilUrl;
        this.contadorPostagem = contadorPostagem;
        this.contadorComentario = contadorComentario;
        this.idsPostagens = idsPostagens;
        this.idsPostagemCurtidas = idsPostagemCurtidas;
        this.idsItensFavoritos = idsItensFavoritos;
        this.idsComentario = idsComentario;
        this.idsComentarioCurtidos = idsComentarioCurtidos;
    }

    public int getContadorComentario() {
        return contadorComentario;
    }

    public void setContadorComentario(int contadorComentario) {
        this.contadorComentario = contadorComentario;
    }

    public List<String> getIdsPostagens() {
        return idsPostagens;
    }

    public void setIdsPostagens(List<String> idsPostagens) {
        this.idsPostagens = idsPostagens;
    }

    public List<String> getIdsPostagemCurtidas() {
        return idsPostagemCurtidas;
    }

    public void setIdsPostagemCurtidas(List<String> idsPostagemCurtidas) {
        this.idsPostagemCurtidas = idsPostagemCurtidas;
    }

    public List<String> getIdsItensFavoritos() {
        return idsItensFavoritos;
    }

    public void setIdsItensFavoritos(List<String> idsItensFavoritos) {
        this.idsItensFavoritos = idsItensFavoritos;
    }

    public List<String> getIdsComentario() {
        return idsComentario;
    }

    public void setIdsComentario(List<String> idsComentario) {
        this.idsComentario = idsComentario;
    }

    public List<String> getIdsComentarioCurtidos() {
        return idsComentarioCurtidos;
    }

    public void setIdsComentarioCurtidos(List<String> idsComentarioCurtidos) {
        this.idsComentarioCurtidos = idsComentarioCurtidos;
    }

    public Usuario() {
    }



    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagemPerfilUrl() {
        return imagemPerfilUrl;
    }

    public void setImagemPerfilUrl(String imagemPerfilUrl) {
        this.imagemPerfilUrl = imagemPerfilUrl;
    }

    public int getContadorPostagem() {
        return contadorPostagem;
    }

    public void setContadorPostagem(int contadorPostagem) {
        this.contadorPostagem = contadorPostagem;
    }

}
