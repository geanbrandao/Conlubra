package com.geanbrandao.gean.conlubra.alerta;


import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.conexao.InformacoesUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

// classe responsavel por criar e mostras mensagem para o usuario
public class CaixasDialogo {

    private final static String TAG = "CaixasDialogo";

    private AlertDialog.Builder verificarEmail;
    private InformacoesUsuario iu;

    public CaixasDialogo() {
        iu = new InformacoesUsuario();
    }

    public void caixaVerificarEmail(Context context){
        CriacaixaVericarEmail(context);
        verificarEmail.show();
    }

    private void CriacaixaVericarEmail(Context context) {
        verificarEmail = new AlertDialog.Builder(context);
        verificarEmail.setMessage(R.string.d_verificar_email);
        verificarEmail.setPositiveButton(R.string.d_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i(TAG, "Botão sim - Positivo");
                iu.getUserAtual().sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i(TAG, "Email enviado com sucesso");
                                    // desloga o usuario para que ele possa fazer login novamente
                                    iu.signOut();
                                }
                            }
                        });
            }
        })
                .setNegativeButton(R.string.d_nao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG, "Botão não - Positivo");
                        //Toast.makeText(getContext(), "Tente fazer login novamente", Toast.LENGTH_SHORT);

                        iu.signOut();
                    }
                });
        verificarEmail.create();
    }

}
