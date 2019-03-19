package com.geanbrandao.gean.conlubra.alerta;


import android.content.Context;
import android.widget.Toast;

import com.geanbrandao.gean.conlubra.R;

// classe responsavel por mostrar os toast para o usuario
public class MensagemAviso {


    public static void usuarioCriado(Context context, boolean b) {
        if (b) {
            Toast.makeText(context, context.getString(R.string.m_usuario_sucesso), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString(R.string.m_usuario_falhou), Toast.LENGTH_SHORT).show();
        }
    }

    public static void emailVerificacao(Context context, boolean b) {
        if (b) {
            Toast.makeText(context, context.getString(R.string.m_email_verificacao_enviado), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString(R.string.m_email_verificacao_falhou), Toast.LENGTH_SHORT).show();
        }
    }

    public static void enviarRecuperarSenha(Context context, boolean b) {
        if (b) {
            Toast.makeText(context, context.getString(R.string.m_recuperar_senha_sucesso), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString(R.string.m_recuperar_senha_falhou), Toast.LENGTH_SHORT).show();
        }

    }

    public static void loginNovemente(Context context) {
        Toast.makeText(context, context.getString(R.string.m_login_novamente), Toast.LENGTH_SHORT).show();
    }


}
