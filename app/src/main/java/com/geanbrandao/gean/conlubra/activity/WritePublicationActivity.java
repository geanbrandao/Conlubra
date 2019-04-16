package com.geanbrandao.gean.conlubra.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.connection.UserInformation;
import com.geanbrandao.gean.conlubra.connection.ConnectionFirebase;
import com.geanbrandao.gean.conlubra.connection.Operations;
import com.geanbrandao.gean.conlubra.model.Postagem;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class WritePublicationActivity extends AppCompatActivity {

    private CircleImageView civProfilePicture;
    private TextView tvName;
    private EditText edWritePublication;
    private Button bPublicar, bAddPicture;
    private ImageView ivWritePublication;

    private static final String PASTA1 = "imagens";
    private static final String PASTA2 = "postagens";
    private static final String TAG = "EscPublicacao";

    private Postagem post;
    private String identifierPost;
    private String identifierUser;

    private boolean imgAdd;

    private byte[] dataImg;

    private List<String> listAux;

    // firebase
    private StorageReference SRimgRef, storageReference;


    private static final int SELECAO_GALERIA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escrever_publicacao);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setTitle("");

        Toolbar toolbar = findViewById(R.id.toolbar_write);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // firabse
        storageReference = ConnectionFirebase.getFirebaseStorage();

        int aux = UserInformation.user.getContadorPostagem()+1;
        identifierPost = UserInformation.user.getIdUsuario()+aux; // id do usuario mais a o numero de postagens
        identifierUser = UserInformation.user.getIdUsuario();
        imgAdd = false;
        post = new Postagem();

        civProfilePicture = findViewById(R.id.civ_profile_write);
        tvName = findViewById(R.id.tv_name_write);
        edWritePublication = findViewById(R.id.ed_write);
        bPublicar = findViewById(R.id.b_publish_write);
        bAddPicture = findViewById(R.id.b_add_picture);
        ivWritePublication = findViewById(R.id.iv_picture_write);

        if (UserInformation.user.getImagemPerfilUrl() != null) {
            Glide.with(this)
                    .load(UserInformation.user.getImagemPerfilUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(civProfilePicture);
        }
        tvName.setText(UserInformation.user.getNome());

        bAddPicture.setOnClickListener(new View.OnClickListener() {
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
                post.setFotoAutorPostagem(UserInformation.user.getImagemPerfilUrl());
                post.setNomeAutorPostagem(UserInformation.user.getNome());
                String publicacao = edWritePublication.getText().toString().trim();
                post.setConteudoPostagem(publicacao);
                post.setIdPostagem(identifierPost);
                post.setContadorComentariosPostagem(0);
                post.setContadorLikesPostagem(0);

                listAux = new ArrayList<>();
                if(UserInformation.user.getIdsPostagens() != null) {
                    listAux = UserInformation.user.getIdsPostagens();
                }
                // se tiver adicionado uma imagem precisa fazer o upload da imagem para o firebase
                if(imgAdd) {
                    UploadTask uploadTask = SRimgRef.putBytes(dataImg);
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
                            return SRimgRef.getDownloadUrl();
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
                                    int numPost = UserInformation.user.getContadorPostagem() + 1;
                                    UserInformation.user.setContadorPostagem(numPost);
                                    post.setImagemPostagem(downloadUri.toString());
                                    Operations.gravaPostagem(post);
                                    listAux.add(post.getIdPostagem());
                                    UserInformation.user.setIdsPostagens(listAux);
                                    Operations.updateListaIdsPostagensUsuario(listAux);
                                    Operations.criarUsuario(UserInformation.user);
                                }
                            }
                        }
                    });
                } else {
                    int numPost = UserInformation.user.getContadorPostagem() + 1;
                    UserInformation.user.setContadorPostagem(numPost);
                    Operations.gravaPostagem(post);
                    listAux.add(post.getIdPostagem());
                    UserInformation.user.setIdsPostagens(listAux);
                    Operations.updateListaIdsPostagensUsuario(listAux);
                    Operations.criarUsuario(UserInformation.user);
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
                    ivWritePublication.setVisibility(View.VISIBLE);
                    ivWritePublication.setImageBitmap(imagem);

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                    dataImg = baos.toByteArray();

                    //Cria a referencia para o firebase
                    SRimgRef = storageReference
                            .child(PASTA1)
                            .child(PASTA2)
                            .child( identifierUser )
                            .child(identifierPost + ".jpeg");

                    imgAdd = true;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
