package com.geanbrandao.gean.conlubra.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.connection.ConnectionFirebase;
import com.geanbrandao.gean.conlubra.connection.Operations;
import com.geanbrandao.gean.conlubra.connection.UserInformation;
import com.geanbrandao.gean.conlubra.fragment.RegisterFragment;
import com.geanbrandao.gean.conlubra.fragment.LoginFragment;
import com.geanbrandao.gean.conlubra.model.Usuario;
import com.geanbrandao.gean.conlubra.utils.Base64Custom;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private static final String LOGIN = "Login";
    private static final String REGISTER = "Registre-se";

    private static final int RC_SIGN_IN = 984;

    private ViewPager viewPager;
    private SmartTabLayout smartTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("");

//        viewPager = findViewById(R.id.viewpager);
//        smartTabLayout = findViewById(R.id.viewpagertab);
//
//        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
//                getSupportFragmentManager(), FragmentPagerItems.with(this)
//                .add(LOGIN, LoginFragment.class)
//                .add(REGISTER, RegisterFragment.class)
//                .create()
//        );
//
//        // seta o adapter, recebe os fragments no view pager
//        viewPager.setAdapter(adapter);
//        // configura
//        smartTabLayout.setViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // carrega perfil e cria usuario
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
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
            Operations.criarUsuario(user);
        } else {
            Toast.makeText(this, "Falhou ao fazer login", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth auth = ConnectionFirebase.getFirebaseAutenticacao();
        if (auth.getCurrentUser() != null) {
            Log.i("Login", "O usuario já está logado");
            FirebaseFirestore db = ConnectionFirebase.getFirebaseFirestore();
            db.collection("usuarios")
                    .document(Base64Custom.codificarBase64(auth.getCurrentUser().getEmail()))
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    UserInformation.user = documentSnapshot.toObject(Usuario.class);
                    Log.i("Login", "Sucesso ao carregar dados do usuário");
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("Login", "Falhou ao carregar usuárioa" + e.getMessage());
                }
            });
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setLogo(R.drawable.logo)
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                                    //                        new AuthUI.IdpConfig.FacebookBuilder().build(),
                                    //                        new AuthUI.IdpConfig.TwitterBuilder().build(),
                                    //                        new AuthUI.IdpConfig.GitHubBuilder().build(),
                                    //                        new AuthUI.IdpConfig.PhoneBuilder().build(),
                                    //                        new AuthUI.IdpConfig.AnonymousBuilder().build(),
                                    new AuthUI.IdpConfig.EmailBuilder().build()))
                            .build(),
                    RC_SIGN_IN);
        }
    }
}
