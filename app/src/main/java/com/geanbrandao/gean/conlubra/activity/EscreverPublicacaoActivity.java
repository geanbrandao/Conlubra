package com.geanbrandao.gean.conlubra.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.conexao.InformacoesUsuario;
import com.geanbrandao.gean.conlubra.conexao.InstanciasFirebase;
import com.geanbrandao.gean.conlubra.conexao.Operacoes;
import com.geanbrandao.gean.conlubra.modelo.Postagem;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EscreverPublicacaoActivity extends AppCompatActivity {

    private CircleImageView fotoDePerfil;
    private TextView tvNome;
    private EditText edEscreverPublicacao;
    private Button bPublicar, bAddFoto;
    private ImageView ivEscreverPublicacao;

    private static final String PASTA1 = "imagens";
    private static final String PASTA2 = "postagens";
    private static final String TAG = "EscPublicacao";

    private Postagem post;
    private String identificadorPostagem;
    private String identificadorUsuario;

    private boolean imagemAdd;

    private byte[] dadosImagem;

    // firebase
    private StorageReference imagemRef, storageReference;


    private static final int SELECAO_GALERIA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escrever_publicacao);

        // firabse
        storageReference = InstanciasFirebase.getFirebaseStorage();

        int aux = InformacoesUsuario.user.getContadorPostagem()+1;
        identificadorPostagem = InformacoesUsuario.user.getIdUsuario()+aux; // id do usuario mais a o numero de postagens
        identificadorUsuario = InformacoesUsuario.user.getIdUsuario();
        imagemAdd = false;
        post = new Postagem();

        fotoDePerfil = findViewById(R.id.civ_foto_perfil_escrever_publicacao);
        tvNome = findViewById(R.id.tv_escrever_publicacao_nome);
        edEscreverPublicacao = findViewById(R.id.ed_escrever_publicacao);
        bPublicar = findViewById(R.id.b_publicar);
        bAddFoto = findViewById(R.id.b_add_foto);
        ivEscreverPublicacao = findViewById(R.id.ivEscreverPublicacao);

        if (InformacoesUsuario.user.getImagemPerfilUrl() != null) {
            Glide.with(this)
                    .load(InformacoesUsuario.user.getImagemPerfilUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(fotoDePerfil);
        }
        tvNome.setText(InformacoesUsuario.user.getNome());

        bAddFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });

        bPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // pegar as informacoes do post
                post.setFotoAutorPostagem(InformacoesUsuario.user.getImagemPerfilUrl());
                post.setNomeAutorPostagem(InformacoesUsuario.user.getNome());
                String publicacao = edEscreverPublicacao.getText().toString().trim();
                post.setConteudoPostagem(publicacao);
                post.setIdPostagem(identificadorPostagem);
                post.setContadorComentariosPostagem(0);
                post.setContadorLikesPostagem(0);

                // se tiver adicionado uma imagem precisa fazer o upload da imagem para o firebase
                if(imagemAdd) {
                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {

                            } else {
                                Log.i(TAG, "task is not successful");
                            }
                        }
                    });

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                Log.i(TAG, "continueWithTask is not successful");
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return imagemRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Log.i(TAG, "continueWithTask onComplete is successful");
                                Uri downloadUri = task.getResult();
                                if (downloadUri == null) {
                                    Log.i(TAG, "Uri nulla");
                                    return;
                                } else {
                                    Log.i(TAG, "Uri não nulla");
                                    int numPost = InformacoesUsuario.user.getContadorPostagem() + 1;
                                    InformacoesUsuario.user.setContadorPostagem(numPost);
                                    post.setImagemPostagem(downloadUri.toString());
                                    Operacoes.gravaPostagem(post);
                                    Operacoes.criarUsuario(InformacoesUsuario.user);
                                }
                            }
                        }
                    });
                } else {
                    int numPost = InformacoesUsuario.user.getContadorPostagem() + 1;
                    InformacoesUsuario.user.setContadorPostagem(numPost);
                    Operacoes.gravaPostagem(post);
                    Operacoes.criarUsuario(InformacoesUsuario.user);
                }
                Log.i(TAG, "Publicacao postada");
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {

                switch (requestCode) {
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(this.getContentResolver(), localImagemSelecionada);
                        break;
                }

                if (imagem != null) {
                    Log.i(TAG, "imagem escolhida da galeria");
                    ivEscreverPublicacao.setImageBitmap(imagem);

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                    dadosImagem = baos.toByteArray();

                    //Cria a referencia para o firebase
                    imagemRef = storageReference
                            .child(PASTA1)
                            .child(PASTA2)
                            .child( identificadorUsuario )
                            .child(identificadorPostagem + ".jpeg");

                    imagemAdd = true;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
