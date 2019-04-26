package com.geanbrandao.gean.conlubra2.fragment;

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

import com.geanbrandao.gean.conlubra2.R;
import com.geanbrandao.gean.conlubra2.activity.LoginActivity;
import com.geanbrandao.gean.conlubra2.alert.DialogBox;
import com.geanbrandao.gean.conlubra2.connection.Operations;
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
                                //startActivity(new Intent(getContext(), LoadActivity.class));
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
