    package com.geanbrandao.gean.conlubra2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.geanbrandao.gean.conlubra2.R;
import com.geanbrandao.gean.conlubra2.connection.ConnectionFirebase;
import com.geanbrandao.gean.conlubra2.connection.UserInformation;
import com.geanbrandao.gean.conlubra2.model.Usuario;
import com.geanbrandao.gean.conlubra2.utils.Base64Custom;
import com.geanbrandao.gean.conlubra2.utils.Help;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private static final String LOGIN = "Login";
    private static final String REGISTER = "Registre-se";

    private static final int RC_SIGN_IN = 984;

    private ViewPager viewPager;
    private SmartTabLayout smartTabLayout;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("");
        Toolbar toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null) {
            Log.i("Login", "Nao tem usuario logado no momento");
            signIn();
        } else {
            Log.i("Login", "ja esta logado");
            startActivity(new Intent(this, MainActivity.class));
        }


    }

    private void signIn() {
        Log.i("Login", "chama tela de login");
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.logo)
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.FacebookBuilder().build(),
                                //                        new AuthUI.IdpConfig.TwitterBuilder().build(),
                                //                        new AuthUI.IdpConfig.GitHubBuilder().build(),
                                //                        new AuthUI.IdpConfig.PhoneBuilder().build(),
                                //                        new AuthUI.IdpConfig.AnonymousBuilder().build(),
                                new AuthUI.IdpConfig.EmailBuilder().build()))
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Login", "OnActivityResult");
        if(resultCode == RESULT_OK && requestCode == RC_SIGN_IN) {
            Log.i("Login", "Result ok");
            if (auth.getCurrentUser() != null) {
                // precisa verificar se o usuario já existe na base de dados.
                if (Help.primeiroLogin(auth)) {
                    Log.i("Login", "Primeira vez que é logado");
                    // cria perfil
                    Usuario user = new Usuario();
                    user.setEmail(auth.getCurrentUser().getEmail());
                    user.setNome(auth.getCurrentUser().getDisplayName());
                    user.setIdUsuario(Base64Custom.codificarBase64(auth.getCurrentUser().getEmail()));
                    user.setImagemPerfilUrl(auth.getCurrentUser().getPhotoUrl().toString());
                    user.setContadorPostagem(0);
                    user.setContadorComentario(0);
                    user.setIdsComentario(null);
                    user.setIdsComentarioCurtidos(null);
                    user.setIdsItensFavoritos(null);
                    user.setIdsPostagemCurtidas(null);
                    user.setIdsPostagens(null);
                    FirebaseFirestore db = ConnectionFirebase.getFirebaseFirestore();
                    db.collection("usuarios").document(Base64Custom.codificarBase64(user.getEmail()))
                            .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i("login", "USUARIO - Usuário criado com sucesso");
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Log.i("login", "USUARIO - Falhou ao criar usuário" + task.getException());
                                Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else { // se já foi logado
                    Log.i("Login", "Já foi logado uma vez");
                    // carrega o perfil do usuario
                    FirebaseFirestore db = ConnectionFirebase.getFirebaseFirestore();
                    db.collection("usuarios").document(Base64Custom.codificarBase64(auth.getCurrentUser().getEmail()))
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UserInformation.user = documentSnapshot.toObject(Usuario.class);
                            Log.i("Login", "USUARIO - Sucesso ao carregar dados do usuário");
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("Login", "USUARIO - Falhou ao carregar usuárioa" + e.getMessage());
                            Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(this, "Falhou ao fazer login", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i("Login", "outro retorno do activityResult");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
