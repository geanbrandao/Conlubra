package com.geanbrandao.gean.conlubra2.connection;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.geanbrandao.gean.conlubra2.alert.MensagemAviso;
import com.geanbrandao.gean.conlubra2.model.Postagem;
import com.geanbrandao.gean.conlubra2.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

// classe para lidar apenas com o authentication
public class UserInformation {
    private final static String TAG = "UserInformation";

    private FirebaseAuth mAuth;
    private String email;
    private String name;
    private String urlProfilePicture;
    public static Usuario user;
    public static Postagem post;

    public UserInformation() {
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getUserAtual() {
        return mAuth.getCurrentUser();
    }

    public void cadastroComEmail(final String email, String senha, final Context context) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            MensagemAviso.usuarioCriado(context, true);
                            Log.i(TAG, "Usuario criado com sucesso");
                        } else {
                            MensagemAviso.usuarioCriado(context, false);
                            Log.i(TAG, "Falha ao criar novo usuario.");
                        }
                    }
                });
    }

    public void mandarEmailVerificacao(final Context context) {
        mAuth.getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            MensagemAviso.emailVerificacao(context, true);
                            Log.i(TAG, "Email de verificacao enviado");
                        } else {
                            MensagemAviso.usuarioCriado(context, false);
                            Log.i(TAG, "Falhou ao enviar o email de verificacao");
                        }
                    }
                });
    }

    public boolean verificaEmailVerficado(Context context) {
        if (mAuth.getCurrentUser().isEmailVerified()) {
            return true;
        } else {
            return false;
        }
    }

    public void atualizaFotoDePerfilUsuario(String url) {

        try {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(url))
                    .build();

            mAuth.getCurrentUser().updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.i(TAG, "Erro ao atualizar foto de perfil");
                    } else {
                        Log.i(TAG, "Sucesso ao atualizar foto de perfil");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recuperarSenha(final Context context) {
        mAuth.sendPasswordResetEmail(getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    MensagemAviso.enviarRecuperarSenha(context, true);
                    Log.i(TAG, "Email de recuperação de senha enviado");
                } else {
                    MensagemAviso.enviarRecuperarSenha(context, false);
                    Log.i(TAG, "Falhou ao enviar email de recuperação de senha");
                }
            }
        });
    }

    public FirebaseUser getUsuarioAtual() {
        return mAuth.getCurrentUser();
    }

    public String getEmail() {
        email = mAuth.getCurrentUser().getEmail();
        return email;
    }

    public String getNome() {
        name = mAuth.getCurrentUser().getDisplayName();
        return name;
    }

    public String getUrlFotoPerfil() {
        urlProfilePicture = mAuth.getCurrentUser().getPhotoUrl().toString();
        return urlProfilePicture;
    }

    public void signOut() {
        mAuth.signOut();
    }

}
