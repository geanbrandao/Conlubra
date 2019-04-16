package com.geanbrandao.gean.conlubra.connection;


import android.support.annotation.NonNull;
import android.util.Log;

import com.geanbrandao.gean.conlubra.utils.Base64Custom;
import com.geanbrandao.gean.conlubra.model.Comentario;
import com.geanbrandao.gean.conlubra.model.Postagem;
import com.geanbrandao.gean.conlubra.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

// classe para lidar apenas com o firestore
public class Operations {

    private static final String USER = "usuarios";
    private static final String POST = "postagens";
    private static final String COMMENT = "comentarios";
    private static final String UPDATE_IDS_POSTS = "idsPostagens";
    private static final String UPDATE_IMG_PROFILE_URL = "imagemPerfilUrl";
    private static final String UPDATE_LIKES_POST_ID_USER = "likesPostagemIdUsuarios";
    private static final String UPDATE_IDS_POST_LIKED = "idsPostagemCurtidas";
    private static final String UPDATE_COUNT_LIKE_POST = "contadorLikesPostagem";

    private String postagemAddId;

    /**
     * No construtor precisa fazer a conexao com o banco e pegar a instancia do firestore
     */
    private static final String TAG = "OperacoesFirestore";

    public static void criarUsuario(Usuario user) {
        FirebaseFirestore db = ConnectionFirebase.getFirebaseFirestore();
        db.collection(USER).document(Base64Custom.codificarBase64(user.getEmail()))
                .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "USUARIO - Usuário criado com sucesso");
                } else {
                    Log.i(TAG, "USUARIO - Falhou ao criar usuário" + task.getException());
                }
            }
        });
    }

    public static void carregaUsuario(String email) {
        FirebaseFirestore db = ConnectionFirebase.getFirebaseFirestore();
        db.collection(USER).document(Base64Custom.codificarBase64(email))
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserInformation.user = documentSnapshot.toObject(Usuario.class);
                Log.i(TAG, "USUARIO - Sucesso ao carregar dados do usuário");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "USUARIO - Falhou ao carregar usuárioa" + e.getMessage());
            }
        });
    }

    public static void updateFotoPerfilUrl(String url) {
        FirebaseFirestore db = ConnectionFirebase.getFirebaseFirestore();
        db.collection(USER).document(Base64Custom.codificarBase64(UserInformation.user.getEmail()))
                .update(UPDATE_IMG_PROFILE_URL, url)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "USUARIO - Url da foto de perfil atualizada");
                        } else {
                            Log.i(TAG, "USUARIO - Falhou ao atualizar a Url da foto de perfil");
                        }
                    }
                });
    }

    public static void gravaPostagem(Postagem post) {
        FirebaseFirestore db = ConnectionFirebase.getFirebaseFirestore();
        db.collection(POST).document(post.getIdPostagem())
                .set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "POSTAGEM - Postagem inserida com sucesso ");
                } else {
                    Log.i(TAG, "POSTAGEM - ao inserir a postagem" + task.getException());
                }
            }
        });

    }

    public static  void updateListaIdsPostagensUsuario(List<String> idPost){
        FirebaseFirestore db = ConnectionFirebase.getFirebaseFirestore();
        db.collection(USER).document(UserInformation.user.getIdUsuario())
                .update(UPDATE_IDS_POSTS, idPost) // atualiiza lista de ids das postagens feitas
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            if (task.isSuccessful()) {
                                Log.i(TAG, "USUARIO - Sucesso ao atualizar a lista com ids das postagens");
                            } else {
                                Log.i(TAG, "USUARIO - Falhou ao atualizar a lista com ids das postagens");
                            }
                        }
                    }
                });
    }

    public static void updateListaLikesPostagem(Postagem post) {
        FirebaseFirestore db = ConnectionFirebase.getFirebaseFirestore();
        db.collection(POST).document(post.getIdPostagem())
                .update(UPDATE_LIKES_POST_ID_USER, post.getLikesPostagemIdUsuarios())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "POSTAGEM - Lista de likes da postagem atualizada");
                        } else {
                            Log.i(TAG, "POSTAGEM - Falhou ao atualizar lista de likes da postagem" + task.getException());
                        }
                    }
                });
    }

    public static void updateListaPostagensCurtidas(List<String> idsPostagensCurtidas) {
        FirebaseFirestore db = ConnectionFirebase.getFirebaseFirestore();
        db.collection(USER).document(UserInformation.user.getIdUsuario())
                .update(UPDATE_IDS_POST_LIKED, idsPostagensCurtidas)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            Log.i(TAG, "USUARIO - Lista de Postagens curtidas atualizada");
                        } else {
                            Log.i(TAG, "USUARIO - Falhou ao atualizar a lista de postagens curtidas");
                        }
                    }
                });
    }

    public static void updateContadorLikes(Postagem post){
        FirebaseFirestore db = ConnectionFirebase.getFirebaseFirestore();
        db.collection(POST).document(post.getIdPostagem())
                .update(UPDATE_COUNT_LIKE_POST, post.getContadorLikesPostagem())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            Log.i(TAG, "POSTAGEM - Sucesso ao atualizar contador de likes");
                        } else {
                            Log.i(TAG, "POSTEGEM - Falhou ao atualizar contador de likes");
                        }
                    }
                });
    }

    public static void addComment(List<Comentario> list) {
        FirebaseFirestore db = ConnectionFirebase.getFirebaseFirestore();
        db.collection(POST).document(UserInformation.post.getIdPostagem())
                .update("comentariosPostagem", list)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Log.i(TAG, "COMENTARIO - Sucesso ao salvar comentario");
                } else {
                    Log.i(TAG, "COMENTARIO - Falhou ao salvar comentario");
                }
            }
        });
    }

    public static void updateContadorComentarios(int i){
        FirebaseFirestore db = ConnectionFirebase.getFirebaseFirestore();
        db.collection(POST).document(UserInformation.post.getIdPostagem())
                .update("contadorComentariosPostagem", i)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.i(TAG, "sucesso ao atualizar o contador de comentarios da postagem");
                        } else {
                            Log.i(TAG, "Falhou ao atualizar o contador de comentarios da postagem");
                        }
                    }
                });
    }

}
