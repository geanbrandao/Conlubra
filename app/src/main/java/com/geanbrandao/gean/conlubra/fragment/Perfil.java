package com.geanbrandao.gean.conlubra.fragment;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.geanbrandao.gean.conlubra.DownloadImageTask;
import com.geanbrandao.gean.conlubra.Permissao;
import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.conexao.InformacoesUsuario;
import com.geanbrandao.gean.conlubra.conexao.InstanciasFirebase;
import com.geanbrandao.gean.conlubra.conexao.Operacoes;
import com.geanbrandao.gean.conlubra.modelo.Usuario;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Perfil extends Fragment {

    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private Usuario usuarioAux;
    private InformacoesUsuario iu;
    private String identificadorUsuario;
    private StorageReference storageReference, imagemRef;

    private FloatingActionButton fabSalvarPerfil;
    private EditText edNome, edEmail, edInstituicao, edCargo;
    private CircleImageView civFotoPerfilPerfil;
    private Button bGaleria, bCamera;

    public Perfil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        usuarioAux = new Usuario();
        iu = new InformacoesUsuario();
        identificadorUsuario = InformacoesUsuario.user.getIdUsuario();
        storageReference = InstanciasFirebase.getFirebaseStorage();
        imagemRef = InstanciasFirebase.getFirebaseStorage();

        fabSalvarPerfil = view.findViewById(R.id.fab_salvar_perfil);
        edNome = view.findViewById(R.id.edNome);
        edEmail = view.findViewById(R.id.edEmail);
        edCargo = view.findViewById(R.id.edCargo);
        edInstituicao = view.findViewById(R.id.edInstituicao);
        civFotoPerfilPerfil = view.findViewById(R.id.civFotoPerfilFeed);
        bGaleria = view.findViewById(R.id.bGaleria);
        bCamera = view.findViewById(R.id.bCamera);

        edNome.setText(InformacoesUsuario.user.getNome());
        edInstituicao.setText(InformacoesUsuario.user.getInstituicao());
        edEmail.setText(InformacoesUsuario.user.getEmail());
        edCargo.setText(InformacoesUsuario.user.getCargo());

        //Validar permissões
        Permissao.validarPermissoes(permissoesNecessarias, getActivity(), 1);

        if (InformacoesUsuario.user.getImagemPerfilUrl() != null) {
            new DownloadImageTask(civFotoPerfilPerfil)
                    .execute(InformacoesUsuario.user.getImagemPerfilUrl());
        }


        fabSalvarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                informacoesPerfil();
            }
        });

        bGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });

        bCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivityForResult(i, SELECAO_CAMERA);
                }
            }
        });

        return view;
    }

    private void informacoesPerfil() {
        String nome = edNome.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String instituicao = edInstituicao.getText().toString().trim();
        String cargo = edCargo.getText().toString().trim();

        if (nome.isEmpty()) {
            edNome.setError("Digite um nome");
            edNome.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edEmail.setError("Digite um nome");
            edEmail.requestFocus();
            return;
        }

        usuarioAux.setNome(nome);
        usuarioAux.setEmail(email);
        usuarioAux.setCargo(cargo);
        usuarioAux.setInstituicao(instituicao);

        InformacoesUsuario.user.setNome(usuarioAux.getNome());
        InformacoesUsuario.user.setEmail(usuarioAux.getEmail());
        InformacoesUsuario.user.setCargo(usuarioAux.getCargo());
        InformacoesUsuario.user.setInstituicao(usuarioAux.getInstituicao());

        Operacoes.criarUsuario(InformacoesUsuario.user);
        getFragmentManager().popBackStack();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {

                switch (requestCode) {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), localImagemSelecionada);
                        break;
                }

                if (imagem != null) {

                    civFotoPerfilPerfil.setImageBitmap(imagem);

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Salvar imagem no firebase
                    imagemRef = storageReference
                            .child("imagens")
                            .child("perfil")
                            .child( identificadorUsuario )
                            .child(identificadorUsuario + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {

                            } else {
                                Log.i("Updateimagem", "task is not successful");
                            }
                        }
                    });

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                Log.i("Updateimagem", "continueWithTask is not successful");
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return imagemRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Log.i("Updateimagem", "continueWithTask onComplete is successful");
                                Uri downloadUri = task.getResult();
                                if (downloadUri == null) {
                                    Log.i("Updateimagem", "Uri nulla");
                                    return;
                                } else {
                                    Log.i("Updateimagem", "Uri não nulla");
                                    InformacoesUsuario.user.setImagemPerfilUrl(downloadUri.toString());
                                    iu.atualizaFotoDePerfilUsuario(downloadUri.toString());
                                    Operacoes.updateFotoPerfilUrl(downloadUri.toString());
                                }
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
