package com.geanbrandao.gean.conlubra.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.geanbrandao.gean.conlubra.Base64Custom;
import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.connection.UserInformation;
import com.geanbrandao.gean.conlubra.connection.ConnectionFirebase;
import com.geanbrandao.gean.conlubra.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoadActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private static final String TAG = "Load";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        mAuth = ConnectionFirebase.getFirebaseAutenticacao();

        if(mAuth.getCurrentUser() != null) {
            // vai carregar o usuario
            FirebaseFirestore db = ConnectionFirebase.getFirebaseFirestore();
            db.collection("usuarios")
                    .document(Base64Custom.codificarBase64(mAuth.getCurrentUser().getEmail()))
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    UserInformation.user = documentSnapshot.toObject(Usuario.class);
                    Log.i(TAG, "Sucesso ao carregar dados do usuário");
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "Falhou ao carregar usuárioa" + e.getMessage());
                }
            });
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }

    }
}
