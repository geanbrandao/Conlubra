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
import com.geanbrandao.gean.conlubra.connection.UserInformation;
import com.geanbrandao.gean.conlubra.connection.ConnectionFirebase;
import com.geanbrandao.gean.conlubra.connection.Operations;
import com.geanbrandao.gean.conlubra.model.Usuario;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment {

    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private Usuario userAux;
    private UserInformation iu;
    private String identifierUser;
    private StorageReference storageReference, imgRef;

    private FloatingActionButton fabSaveProfile;
    private EditText edName, edEmail, edInstitution, edOffice;
    private CircleImageView civProfilePicture;
    private Button bGallery, bCamera;

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        userAux = new Usuario();
        iu = new UserInformation();
        identifierUser = UserInformation.user.getIdUsuario();
        storageReference = ConnectionFirebase.getFirebaseStorage();
        imgRef = ConnectionFirebase.getFirebaseStorage();

        fabSaveProfile = view.findViewById(R.id.fab_salvar_perfil);
        edName = view.findViewById(R.id.edNome);
        edEmail = view.findViewById(R.id.edEmail);
        edOffice = view.findViewById(R.id.edCargo);
        edInstitution = view.findViewById(R.id.edInstituicao);
        civProfilePicture = view.findViewById(R.id.civFotoPerfilFeed);
        bGallery = view.findViewById(R.id.bGaleria);
        bCamera = view.findViewById(R.id.bCamera);

        edName.setText(UserInformation.user.getNome());
        edInstitution.setText(UserInformation.user.getInstituicao());
        edEmail.setText(UserInformation.user.getEmail());
        edOffice.setText(UserInformation.user.getCargo());

        //Validar permissões
        Permissao.validarPermissoes(permissoesNecessarias, getActivity(), 1);

        if (UserInformation.user.getImagemPerfilUrl() != null) {
            new DownloadImageTask(civProfilePicture)
                    .execute(UserInformation.user.getImagemPerfilUrl());
        }


        fabSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                informacoesPerfil();
            }
        });

        bGallery.setOnClickListener(new View.OnClickListener() {
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
        String nome = edName.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String instituicao = edInstitution.getText().toString().trim();
        String cargo = edOffice.getText().toString().trim();

        if (nome.isEmpty()) {
            edName.setError("Digite um nome");
            edName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edEmail.setError("Digite um nome");
            edEmail.requestFocus();
            return;
        }

        userAux.setNome(nome);
        userAux.setEmail(email);
        userAux.setCargo(cargo);
        userAux.setInstituicao(instituicao);

        UserInformation.user.setNome(userAux.getNome());
        UserInformation.user.setEmail(userAux.getEmail());
        UserInformation.user.setCargo(userAux.getCargo());
        UserInformation.user.setInstituicao(userAux.getInstituicao());

        Operations.criarUsuario(UserInformation.user);
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

                    civProfilePicture.setImageBitmap(imagem);

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Salvar imagem no firebase
                    imgRef = storageReference
                            .child("imagens")
                            .child("perfil")
                            .child( identifierUser )
                            .child(identifierUser + ".jpeg");

                    UploadTask uploadTask = imgRef.putBytes(dadosImagem);
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
                            return imgRef.getDownloadUrl();
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
                                    UserInformation.user.setImagemPerfilUrl(downloadUri.toString());
                                    iu.atualizaFotoDePerfilUsuario(downloadUri.toString());
                                    Operations.updateFotoPerfilUrl(downloadUri.toString());
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
