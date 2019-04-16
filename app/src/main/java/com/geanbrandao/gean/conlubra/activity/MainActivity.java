package com.geanbrandao.gean.conlubra.activity;

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
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.geanbrandao.gean.conlubra.InformationsFragment;
import com.geanbrandao.gean.conlubra.LocationFragment;
import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.connection.UserInformation;
import com.geanbrandao.gean.conlubra.connection.ConnectionFirebase;
import com.geanbrandao.gean.conlubra.connection.Operations;
import com.geanbrandao.gean.conlubra.fragment.PatrocinioFragment;
import com.geanbrandao.gean.conlubra.fragment.RegisterFragment;
import com.geanbrandao.gean.conlubra.fragment.LoginFragment;
import com.geanbrandao.gean.conlubra.fragment.FeedFragment;
import com.geanbrandao.gean.conlubra.fragment.Profile;
import com.geanbrandao.gean.conlubra.fragment.ProgrammingFragment;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String PROFILE = "Perfil";
    private static final String FEED = "Linha do Tempo";
    private static final String PROGRAMMING = "Programação";


    private RegisterFragment registerFragment;
    private LoginFragment loginFragment;
    private Profile profileFragment;
    private FeedFragment feedFragment;
    private LocationFragment locationFragment;
    private ProgrammingFragment programmingFragment;
    private InformationsFragment informationsFragment;
    private PatrocinioFragment patrocinioFragment;

    private FirebaseAuth mAuth;

    private CircleImageView headerProfilePicture;
    private TextView headerName, headerEmail;
    private Button editarPerfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profileFragment = new Profile();
        feedFragment = new FeedFragment();
        programmingFragment = new ProgrammingFragment();
        locationFragment = new LocationFragment();
        informationsFragment = new InformationsFragment();
        patrocinioFragment = new PatrocinioFragment();

        setTitle("");
        mAuth = ConnectionFirebase.getFirebaseAutenticacao();
        //mAuth.signOut();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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


        if (mAuth.getCurrentUser() != null) {
            Operations.carregaUsuario(mAuth.getCurrentUser().getEmail());

            if (UserInformation.user.getImagemPerfilUrl() != null) {
                Glide.with(this)
                        .load(UserInformation.user.getImagemPerfilUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(headerProfilePicture);
            }
            Log.i("Mainss", ""+ UserInformation.user.getEmail());
            headerEmail.setText(UserInformation.user.getEmail());
            headerName.setText(UserInformation.user.getNome());

        }

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    onResume();
                }
            }
        });

        //setTitle(FEED);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.frameConteudo, feedFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
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
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {

        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));

        }

        super.onResume();
    }
}
