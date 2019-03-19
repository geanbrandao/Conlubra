package com.geanbrandao.gean.conlubra.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.conexao.InformacoesUsuario;

import de.hdodenhof.circleimageview.CircleImageView;

public class EscreverPublicacaoActivity extends AppCompatActivity {

    private CircleImageView fotoDePerfil;
    private TextView tvNome;
    private EditText edEscreverPublicacao;
    private Button bPublicar, bAddFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escrever_publicacao);

        fotoDePerfil = findViewById(R.id.civ_foto_perfil_escrever_publicacao);
        tvNome = findViewById(R.id.tv_escrever_publicacao_nome);
        edEscreverPublicacao = findViewById(R.id.ed_escrever_publicacao);
        bPublicar = findViewById(R.id.b_publicar);
        bAddFoto = findViewById(R.id.b_add_foto);

        if (InformacoesUsuario.user.getImagemPerfilUrl() != null) {
            Glide.with(this)
                    .load(InformacoesUsuario.user.getImagemPerfilUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(fotoDePerfil);
        }
        tvNome.setText(InformacoesUsuario.user.getNome());
    }
}
