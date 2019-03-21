package com.geanbrandao.gean.conlubra.conexao;


import android.support.annotation.NonNull;
import android.util.Log;

import com.geanbrandao.gean.conlubra.Base64Custom;
import com.geanbrandao.gean.conlubra.modelo.Postagem;
import com.geanbrandao.gean.conlubra.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Field;
import java.util.List;

// classe para lidar apenas com o firestore
public class Operacoes {

    private static final String USER = "usuarios";
    private static final String POST = "postagens";
    private static final String COMENTARIO = "comentarios";

    private String postagemAddId;

    /**
     * No construtor precisa fazer a conexao com o banco e pegar a instancia do firestore
     */
    private static final String TAG = "OperacoesFirestore";

    public static void criarUsuario(Usuario user) {
        FirebaseFirestore db = InstanciasFirebase.getFirebaseFirestore();
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
        FirebaseFirestore db = InstanciasFirebase.getFirebaseFirestore();
        db.collection(USER).document(Base64Custom.codificarBase64(email))
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                InformacoesUsuario.user = documentSnapshot.toObject(Usuario.class);
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
        FirebaseFirestore db = InstanciasFirebase.getFirebaseFirestore();
        db.collection(USER).document(Base64Custom.codificarBase64(InformacoesUsuario.user.getEmail()))
                .update("imagemPerfilUrl", url)
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
        FirebaseFirestore db = InstanciasFirebase.getFirebaseFirestore();
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
        FirebaseFirestore db = InstanciasFirebase.getFirebaseFirestore();
        db.collection(USER).document(InformacoesUsuario.user.getIdUsuario())
                .update("idsPostagens", idPost) // atualiiza lista de ids das postagens feitas
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
        FirebaseFirestore db = InstanciasFirebase.getFirebaseFirestore();
        db.collection(POST).document(post.getIdPostagem())
                .update("likesPostagemIdUsuarios", post.getLikesPostagemIdUsuarios())
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
        FirebaseFirestore db = InstanciasFirebase.getFirebaseFirestore();
        db.collection(USER).document(InformacoesUsuario.user.getIdUsuario())
                .update("idsPostagemCurtidas", idsPostagensCurtidas)
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
        FirebaseFirestore db = InstanciasFirebase.getFirebaseFirestore();
        db.collection(POST).document(post.getIdPostagem())
                .update("contadorLikesPostagem", post.getContadorLikesPostagem())
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

}
