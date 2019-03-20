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
import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.conexao.InformacoesUsuario;
import com.geanbrandao.gean.conlubra.conexao.InstanciasFirebase;
import com.geanbrandao.gean.conlubra.conexao.Operacoes;
import com.geanbrandao.gean.conlubra.fragment.CadastrarFragment;
import com.geanbrandao.gean.conlubra.fragment.EntrarFragment;
import com.geanbrandao.gean.conlubra.fragment.FeedFragment;
import com.geanbrandao.gean.conlubra.fragment.Perfil;
import com.geanbrandao.gean.conlubra.fragment.ProgramacaoFragment;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CadastrarFragment cadastrarFragment;
    private EntrarFragment entrarFragment;
    private Perfil perfilFragment;
    private FeedFragment feedFragment;
    private ProgramacaoFragment programacaoFragment;
    private FirebaseAuth mAuth;

    private CircleImageView headerfotoPerfil;
    private TextView headerNome, headerEmail;
    private Button editarPerfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        perfilFragment = new Perfil();
        feedFragment = new FeedFragment();
        programacaoFragment = new ProgramacaoFragment();

        mAuth = InstanciasFirebase.getFirebaseAutenticacao();
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
        headerNome = view.findViewById(R.id.tv_nome_usuario_header);
        headerfotoPerfil = view.findViewById(R.id.civ_foto_perfil_header);


        if (mAuth.getCurrentUser() != null) {
            Operacoes.carregaUsuario(mAuth.getCurrentUser().getEmail());

            if (InformacoesUsuario.user.getImagemPerfilUrl() != null) {
                Glide.with(this)
                        .load(InformacoesUsuario.user.getImagemPerfilUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(headerfotoPerfil);
            }
            Log.i("Mainss", ""+InformacoesUsuario.user.getEmail());
            headerEmail.setText(InformacoesUsuario.user.getEmail());
            headerNome.setText(InformacoesUsuario.user.getNome());

        }

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    onResume();
                }
            }
        });

        setTitle("Linha do Tempo");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.frameConteudo, feedFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            setTitle("Linha do Tempo");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frameConteudo, feedFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_perfil) {
            setTitle("Perfil");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frameConteudo, perfilFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_programacao) {
            setTitle("Programção");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frameConteudo, programacaoFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
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
