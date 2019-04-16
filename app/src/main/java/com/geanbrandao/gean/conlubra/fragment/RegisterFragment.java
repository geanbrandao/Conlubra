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

import com.geanbrandao.gean.conlubra.utils.Base64Custom;
import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.activity.LoginActivity;
import com.geanbrandao.gean.conlubra.connection.Operations;
import com.geanbrandao.gean.conlubra.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private final static String TAG = "CadastrarUsuario";

    private EditText mName, mEmail, mConfirmPassword, mPassword;
    private Button mRegister;
    private FirebaseAuth mAuth;
    private Usuario user;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cadastrar, container, false);

        mAuth = FirebaseAuth.getInstance();

        mName = view.findViewById(R.id.et_nomeCad);
        mPassword = view.findViewById(R.id.et_senhaCad);
        mEmail = view.findViewById(R.id.et_emailCad);
        mConfirmPassword = view.findViewById(R.id.et_confirmarSenhaCad);
        mRegister = view.findViewById(R.id.b_cadastrar);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                novoUsuario();
            }
        });

        return view;
    }

    private void novoUsuario() {
        String email = mEmail.getText().toString().trim();
        String senha = mPassword.getText().toString().trim();
        String nome = mName.getText().toString().trim();
        String confirmarSenha = mConfirmPassword.getText().toString().trim();


        if (email.isEmpty()) {
            mEmail.setError("Digite o seu e-mail.");
            mEmail.requestFocus();
            return;
        }

        if (nome.isEmpty()) {
            mName.setError("Digite o seu nome.");
            mName.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Digite um email valido");
            mEmail.requestFocus();
            return;
        }

        if (senha.isEmpty()) {
            mPassword.setError("Digite a senha.");
            mPassword.requestFocus();
            return;
        }

        if (senha.length() < 6) {
            mPassword.setError("O tamanho minimo da senha é 6");
            mPassword.requestFocus();
            return;
        }


        if (confirmarSenha.isEmpty()) {
            mConfirmPassword.setError("Confirme sua senha por favor");
            mConfirmPassword.requestFocus();
            return;
        }

        if (!confirmarSenha.equals(senha)) {
            mConfirmPassword.setError("As senhas não correpondem");
            mConfirmPassword.requestFocus();
            return;
        }

        user = new Usuario();
        user.setEmail(email);
        user.setNome(nome);
        user.setIdUsuario(Base64Custom.codificarBase64(email));
        user.setImagemPerfilUrl(null);
        user.setContadorPostagem(0);
        user.setContadorComentario(0);
        user.setIdsComentario(null);
        user.setIdsComentarioCurtidos(null);
        user.setIdsItensFavoritos(null);
        user.setIdsPostagemCurtidas(null);
        user.setIdsPostagens(null);


        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "Usuario criado com sucesso ");
                            // precisa ser levado para tela de login
                            Toast.makeText(getContext(), "Usuário criado com sucesso. Por favor confirme o email e faça o login.", Toast.LENGTH_LONG).show();

                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.i("EmailDeVerificacao", "Email enviado com sucesso");
                                        // desloga o usuario para que ele possa fazer login novamente
                                        mAuth.signOut();
                                        Operations.criarUsuario(user);
                                        startActivity(new Intent(getContext(), LoginActivity.class));
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), getContext().getString(R.string.m_cadastrar_falhou), Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "Não conseguiu criar o usuario. " + task.getException().getMessage());
                        }
                    }
                });
    }

}
