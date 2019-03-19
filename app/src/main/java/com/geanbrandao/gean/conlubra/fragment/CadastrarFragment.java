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

import com.geanbrandao.gean.conlubra.Base64Custom;
import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.activity.LoginActivity;
import com.geanbrandao.gean.conlubra.conexao.Operacoes;
import com.geanbrandao.gean.conlubra.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarFragment extends Fragment {

    private final static String TAG = "CadastrarUsuario";

    private EditText mNome, mEmail, mConfirmarSenha, mSenha;
    private Button mCadastrar;
    private FirebaseAuth mAuth;
    private Usuario usuario;

    public CadastrarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cadastrar, container, false);

        mAuth = FirebaseAuth.getInstance();

        mNome = view.findViewById(R.id.et_nomeCad);
        mSenha = view.findViewById(R.id.et_senhaCad);
        mEmail = view.findViewById(R.id.et_emailCad);
        mConfirmarSenha = view.findViewById(R.id.et_confirmarSenhaCad);
        mCadastrar = view.findViewById(R.id.b_cadastrar);

        mCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                novoUsuario();
            }
        });

        return view;
    }

    private void novoUsuario() {
        String email = mEmail.getText().toString().trim();
        String senha = mSenha.getText().toString().trim();
        String nome = mNome.getText().toString().trim();
        String confirmarSenha = mConfirmarSenha.getText().toString().trim();


        if (email.isEmpty()) {
            mEmail.setError("Digite o seu e-mail.");
            mEmail.requestFocus();
            return;
        }

        if (nome.isEmpty()) {
            mNome.setError("Digite o seu nome.");
            mNome.requestFocus();
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

        if (senha.length() < 6) {
            mSenha.setError("O tamanho minimo da senha é 6");
            mSenha.requestFocus();
            return;
        }


        if (confirmarSenha.isEmpty()) {
            mConfirmarSenha.setError("Confirme sua senha por favor");
            mConfirmarSenha.requestFocus();
            return;
        }

        if (!confirmarSenha.equals(senha)) {
            mConfirmarSenha.setError("As senhas não correpondem");
            mConfirmarSenha.requestFocus();
            return;
        }

        usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setNome(nome);
        usuario.setIdUsuario(Base64Custom.codificarBase64(email));
        usuario.setImagemPerfilUrl(null);
        usuario.setContadorPostagem(0);
        usuario.setContadorComentario(0);
        usuario.setIdsComentario(null);
        usuario.setIdsComentarioCurtidos(null);
        usuario.setIdsItensFavoritos(null);
        usuario.setIdsPostagemCurtidas(null);
        usuario.setIdsPostagens(null);


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
                                        Operacoes.criarUsuario(usuario);
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
