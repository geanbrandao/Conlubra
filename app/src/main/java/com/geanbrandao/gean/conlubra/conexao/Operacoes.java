package com.geanbrandao.gean.conlubra.conexao;


import android.support.annotation.NonNull;
import android.util.Log;

import com.geanbrandao.gean.conlubra.Base64Custom;
import com.geanbrandao.gean.conlubra.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
                    Log.i(TAG, "Usuário criado com sucesso");
                } else {
                    Log.i(TAG, "Falhou ao criar usuário" + task.getException());
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
                Log.i(TAG, "Sucesso ao carregar dados do usuário");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "Falhou ao carregar usuárioa" + e.getMessage());
            }
        });
    }

}
