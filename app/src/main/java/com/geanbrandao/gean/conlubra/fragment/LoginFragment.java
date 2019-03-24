package com.geanbrandao.gean.conlubra.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.activity.LoadActivity;
import com.geanbrandao.gean.conlubra.activity.LoginActivity;
import com.geanbrandao.gean.conlubra.alert.DialogBox;
import com.geanbrandao.gean.conlubra.connection.Operations;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText mEmail, mSenha;
    private Button mLogin;
    private DialogBox dialogo;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entrar, container, false);
        // firebase
        mAuth = FirebaseAuth.getInstance();
        //reference = FirebaseDatabase.getInstance().getReference().child("usuarios");
        dialogo = new DialogBox();
        mEmail = view.findViewById(R.id.et_emailLogin);
        mSenha = view.findViewById(R.id.et_senhaLogin);
        mLogin = view.findViewById(R.id.b_login);
        //mSignInButton = view.findViewById(R.id.login_with_google);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logar();
            }
        });

        /*mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });*/

        // facebook
        /*callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        // If using in a fragment
        loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.i("Facebook", "Profile escolhido com sucesso");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.i("Facebook", "Chamada do botão cancelada");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.i("Facebook", "Erro ao escolher perfil " + exception);
            }
        });*/

        /*
        sharedPreferences = getContext().getSharedPreferences("PREFERENCES", getContext().MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        player = new Jogador();

        // Google
        // Configure Google Sign In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("533087579522-fcltotm1u54hn8gr5fsor235uefbuqmv.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);*/

        //keytool -exportcert -alias androiddebugkey -keystore "C:\Users\karlo\.android\debug.keystore" | "C:\Users\karlo\Downloads\openssl-0.9.8k_X64\bin\openssl" sha1 -binary | "C:\Users\karlo\Downloads\openssl-0.9.8k_X64\bin\openssl" base64
        // a senha padrao é android

        return view;
    }

    private void logar() {
        final String email = mEmail.getText().toString().trim();
        String senha = mSenha.getText().toString().trim();


        if (email.isEmpty()) {
            mEmail.setError("Digite o seu e-mail.");
            mEmail.requestFocus();
            return;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Digite um email valido");
            mEmail.requestFocus();
            return;
        }

        if (senha.isEmpty()) {
            mSenha.setError("Digite a senha.");
            mSenha.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("Login usuario", "Usuario logado");
                            // verifica se o email ja foi verificado
                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                Log.i("EmailDeVerificacao", "Email foi verificado com sucesso");
                                Operations.carregaUsuario(email);
                                startActivity(new Intent(getContext(), LoadActivity.class));
                            } else {
                                // manda o email de verificacao de novo e desloga o usuario
                                dialogo.caixaVerificarEmail(getContext());
                            }

                        } else {
                            Log.i("Login usuario", "Não conseguiu logar");
                            startActivity(new Intent(getContext(), LoginActivity.class));
                            Toast.makeText(getContext(), "Verifique usuário e senha", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
