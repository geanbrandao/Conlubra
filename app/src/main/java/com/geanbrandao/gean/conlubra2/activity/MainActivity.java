package com.geanbrandao.gean.conlubra2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.geanbrandao.gean.conlubra2.fragment.AboutAppFragment;
import com.geanbrandao.gean.conlubra2.fragment.InformationsFragment;
import com.geanbrandao.gean.conlubra2.fragment.LocationFragment;
import com.geanbrandao.gean.conlubra2.R;
import com.geanbrandao.gean.conlubra2.connection.UserInformation;
import com.geanbrandao.gean.conlubra2.connection.ConnectionFirebase;
import com.geanbrandao.gean.conlubra2.fragment.FAQFragment;
import com.geanbrandao.gean.conlubra2.fragment.MiniCursosFragment;
import com.geanbrandao.gean.conlubra2.fragment.PatrocinioFragment;
import com.geanbrandao.gean.conlubra2.fragment.FeedFragment;
import com.geanbrandao.gean.conlubra2.fragment.Profile;
import com.geanbrandao.gean.conlubra2.fragment.ProgrammingFragment;
import com.geanbrandao.gean.conlubra2.model.Usuario;
import com.geanbrandao.gean.conlubra2.utils.Base64Custom;
import com.geanbrandao.gean.conlubra2.utils.DownloadImageTask;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_SIGN_IN = 984;


    private Profile profileFragment;
    private FeedFragment feedFragment;
    private LocationFragment locationFragment;
    private ProgrammingFragment programmingFragment;
    private InformationsFragment informationsFragment;
    private PatrocinioFragment patrocinioFragment;
    private FAQFragment faqFragment;
    private MiniCursosFragment miniCursosFragment;
    private AboutAppFragment aboutAppFragment;


    private CircleImageView headerProfilePicture;
    private TextView headerName, headerEmail;
    private ImageView editarPerfil;

    private DrawerLayout drawer;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        drawer = findViewById(R.id.drawer_layout);
        profileFragment = new Profile();
        feedFragment = new FeedFragment();
        programmingFragment = new ProgrammingFragment();
        locationFragment = new LocationFragment();
        informationsFragment = new InformationsFragment();
        patrocinioFragment = new PatrocinioFragment();
        faqFragment = new FAQFragment();
        miniCursosFragment = new MiniCursosFragment();
        aboutAppFragment = new AboutAppFragment();


        auth = FirebaseAuth.getInstance();


        carregaUser(new MyCallback() {
            @Override
            public void onCallback(Usuario user) {
                UserInformation.user = user;
                //setTitle(FEED);
                if (UserInformation.user.getImagemPerfilUrl() != null) {
                    new DownloadImageTask(headerProfilePicture)
                            .execute(UserInformation.user.getImagemPerfilUrl());
                }
                headerEmail.setText(UserInformation.user.getEmail());
                headerName.setText(UserInformation.user.getNome());
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frameConteudo, feedFragment);
                fragmentTransaction.commit();
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        headerEmail = view.findViewById(R.id.tv_email_usuario_header);
        headerName = view.findViewById(R.id.tv_nome_usuario_header);
        headerProfilePicture = view.findViewById(R.id.civ_foto_perfil_header);
        editarPerfil = view.findViewById(R.id.editar_perfil_header);


//        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (firebaseAuth.getCurrentUser() == null) {
//                    onResume();
//                }
//            }
//        });

        editarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frameConteudo, profileFragment);
                fragmentTransaction.commit();

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feed) {
            //setTitle(FEED);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frameConteudo, feedFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_perfil) {
            //setTitle(PROFILE);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frameConteudo, profileFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_programacao) {
            //setTitle(PROGRAMMING);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frameConteudo, programmingFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_mapa) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frameConteudo, locationFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_informacoes) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frameConteudo, informationsFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_patrocinio) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frameConteudo, patrocinioFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_minicursos) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frameConteudo, miniCursosFragment);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_faq) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frameConteudo, faqFragment);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_sobre_app) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frameConteudo, aboutAppFragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("login", "onResume");
        if (auth.getCurrentUser() == null) {
        Log.i("login", "usuario nullo");
            startActivity(new Intent(this, LoginActivity.class));
        }

    }

    public interface MyCallback {
        void onCallback(Usuario user);
    }

    public void carregaUser(final MyCallback myCallback){
        Log.i("Login", "Email do usuario "+auth.getCurrentUser().getEmail());
        FirebaseFirestore db = ConnectionFirebase.getFirebaseFirestore();
        db.collection("usuarios")
            .document(Base64Custom.codificarBase64(auth.getCurrentUser().getEmail()))
            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usuario u = documentSnapshot.toObject(Usuario.class);
                Log.i("Login", ""+documentSnapshot.toObject(Usuario.class));
                Log.i("Login", "Sucesso ao carregar dados do usuário");
                myCallback.onCallback(u);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Login", "Falhou ao carregar usuário" + e.getMessage());
            }
        });
    }


}
