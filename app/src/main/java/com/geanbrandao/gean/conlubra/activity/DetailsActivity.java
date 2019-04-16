package com.geanbrandao.gean.conlubra.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geanbrandao.gean.conlubra.utils.Help;
import com.geanbrandao.gean.conlubra.utils.DownloadImageTask;
import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.adapter.CommentsAdapter;
import com.geanbrandao.gean.conlubra.connection.ConnectionFirebase;
import com.geanbrandao.gean.conlubra.connection.Operations;
import com.geanbrandao.gean.conlubra.connection.UserInformation;
import com.geanbrandao.gean.conlubra.model.Comentario;
import com.geanbrandao.gean.conlubra.model.Postagem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, CommentsAdapter.ComentariosAdapaterListener {

    private static final String TAG = "ListaComentarios";
    private static final String POST = "postagens";
    private static final String DATE_COMMENT = "dataComentario";

    private CircleImageView civProfile;
    private CircleImageView civMyProfile;
    private ImageView ivPicture;
    private TextView tvName, tvDate, tvPublication, tvLikes, tvComment;
    private Button like, bPublish;
    private EditText edPublishComment;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommentsAdapter adapter;

    // variaveis auxiliares
    private String nameAuthorComment;
    private String pictureAuthorComment;
    private int countCommentPost, countLikesPost;
    private String idComment;
    private List<String> listAuxLikesUser = new ArrayList<>();

    private Context context;

    private List<Comentario> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setTitle("");

        Toolbar toolbar = findViewById(R.id.toolbar_detalhes);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        nameAuthorComment = UserInformation.user.getNome();
        pictureAuthorComment = UserInformation.user.getImagemPerfilUrl();
        countCommentPost = UserInformation.post.getContadorComentariosPostagem()+1;
        idComment = UserInformation.post.getIdPostagem() + "" + countCommentPost;
        listAuxLikesUser = UserInformation.user.getIdsPostagemCurtidas();

        // guarda qtd likes
        countLikesPost = UserInformation.post.getContadorLikesPostagem();

        context = this;
        comments = new ArrayList<>();

        recyclerView = findViewById(R.id.rv_comentarios);
        swipeRefreshLayout = findViewById(R.id.srl_comentarios);
        swipeRefreshLayout.setOnRefreshListener(this);

        civProfile = findViewById(R.id.civ_profile_detalhes);
        civMyProfile = findViewById(R.id.civ_my_perfil_detalhes);
        tvName = findViewById(R.id.tv_name_detalhes);
        tvDate = findViewById(R.id.tv_date_detalhes);
        tvPublication = findViewById(R.id.tv_publication_detalhes);
        tvLikes = findViewById(R.id.tv_likes_detalhes);
        tvComment = findViewById(R.id.tv_comments_detalhes);
        ivPicture = findViewById(R.id.iv_picture_detalhes);
        like = findViewById(R.id.b_like_detalhes);
        bPublish = findViewById(R.id.b_publish_comment_detalhes);
        edPublishComment = findViewById(R.id.ed_comment_detalhes);

        //Inicia detalhes da publicacao
        tvName.setText(UserInformation.post.getNomeAutorPostagem());
        tvDate.setText(formataData(UserInformation.post.getDataPostagem()));
        tvPublication.setText(UserInformation.post.getConteudoPostagem());
        tvLikes.setText(String.valueOf(UserInformation.post.getContadorLikesPostagem()));
        tvComment.setText(String.valueOf(UserInformation.post.getContadorComentariosPostagem()));
        // foto autor da postagem
        if (UserInformation.post.getFotoAutorPostagem() != null) {
            new DownloadImageTask(civProfile).execute(UserInformation.post.getFotoAutorPostagem());
        }
        // foto da postagem
        if (UserInformation.post.getImagemPostagem() != null) {
            new DownloadImageTask(ivPicture).execute(UserInformation.post.getImagemPostagem());
        }
        // verifica se tem like
        if (isLike(UserInformation.post.getIdPostagem())) {
            like.setBackground(this.getDrawable(R.drawable.ic_favorite_black_24dp));
        } else {
            like.setBackground(this.getDrawable(R.drawable.ic_favorite_border_black_24dp));
        }

        // carrega a foto do usuario
        if(UserInformation.user.getImagemPerfilUrl() != null) {
            new DownloadImageTask(civMyProfile).execute(UserInformation.user.getImagemPerfilUrl());
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getComentarios();
        adapter = new CommentsAdapter(comments, DetailsActivity.this, DetailsActivity.this);
        recyclerView.setAdapter(adapter);

        bPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comentario newComment = new Comentario();
                String comment = edPublishComment.getText().toString().trim();
                if (!comment.isEmpty()) {
                    newComment.setConteudoComentario(comment);
                    newComment.setNomeAutorComentario(nameAuthorComment);
                    newComment.setFotoAutorPostagem(pictureAuthorComment);
                    newComment.setContadorLikesComentario(0);
                    newComment.setIdComentario(idComment);
                    newComment.setDataComentario(new Date());
                    // atualiza lista de comentarios offline
                    updateComments(newComment);
                    edPublishComment.setText("");

                }
                // minimiza teclado
                ((InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(edPublishComment.getWindowToken(), 0);

            }
        });

        // precisa setar todas as informações alteradas no post.
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Botao curtir", Toast.LENGTH_SHORT).show();
                // verifica se tem like
                if ((Help.buttonLike(like, context))) {
                    like.setBackground(context.getDrawable(R.drawable.ic_favorite_black_24dp));
                    Help.addInList(UserInformation.post.getLikesPostagemIdUsuarios(), UserInformation.user.getIdUsuario());
                    countLikesPost++;
                    // adiciona o like na lista do usuario
                    Help.addInList(UserInformation.user.getIdsPostagemCurtidas(), UserInformation.post.getIdPostagem());
                    Toast.makeText(context, "like", Toast.LENGTH_SHORT).show();
                } else {
                    like.setBackground(context.getDrawable(R.drawable.ic_favorite_border_black_24dp));
                    Help.removeInList(UserInformation.post.getLikesPostagemIdUsuarios(), UserInformation.user.getIdUsuario());
                    countLikesPost--;
                    // remove o like na lista do usuario
                    Help.removeInList(UserInformation.user.getIdsPostagemCurtidas(), UserInformation.post.getIdPostagem());
                    Toast.makeText(context, "deslike", Toast.LENGTH_SHORT).show();
                }
                UserInformation.post.setContadorLikesPostagem(countLikesPost);
                tvLikes.setText(String.valueOf(countLikesPost));
                // quando a activity chamar onPause precisa gravar as alteracoes na publicacao online
            }
        });

        // minimiza o teclado
//        edPublishComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(!b){
//                    ((InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE))
//                            .hideSoftInputFromWindow(edPublishComment.getWindowToken(), 0);
//                }
//            }
//        });

    }

    public void updateComments(Comentario newComment) {
        comments.add(newComment); // add na lista de comentarios
        //adapter.notifyDataSetChanged();
        UserInformation.post.setContadorComentariosPostagem(countCommentPost); // atualizo offline o contador
        Operations.updateContadorComentarios(countCommentPost); // atualiza online contador
        tvComment.setText(String.valueOf(countCommentPost)); // atualiza IU contador
        countCommentPost++;
        // salva online o comentario
        if(UserInformation.post.getComentariosPostagem() != null) {
            UserInformation.post.getComentariosPostagem().add(newComment);
        } else {
            UserInformation.post.setComentariosPostagem(new ArrayList<Comentario>());
            UserInformation.post.getComentariosPostagem().add(newComment);
        }
        Operations.addComment(UserInformation.post.getComentariosPostagem());
        if(comments.size() == 1) {
            adapter = new CommentsAdapter(comments, DetailsActivity.this, DetailsActivity.this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private String formataData(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm '-' dd/MM/yyyy", Locale.US);
        return dateFormatter.format(date);
    }

    private boolean isLike(String id) { //
        if (UserInformation.user.getIdsPostagemCurtidas() != null) {
            for (String l : UserInformation.user.getIdsPostagemCurtidas()) {
                if (id.equals(l)) { // compara se esta na lista das publicacoes curtidas pelo usuario
                    return true;
                }
            }
        }
        return false;
    }

    private void getComentarios() {
        comments.clear();
        FirebaseFirestore db = ConnectionFirebase.getFirebaseFirestore();

        db.collection(POST).document(UserInformation.post.getIdPostagem())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    Postagem p = task.getResult().toObject(Postagem.class);
                    if(p.getComentariosPostagem() != null) {
//                        swipeRefreshLayout.setRefreshing(true);
                        for (Comentario c : p.getComentariosPostagem()) {
                            comments.add(c);
                            Log.i(TAG, "Comentario Id = " + c.getIdComentario());
                        }
//                        swipeRefreshLayout.setRefreshing(false);
                        Log.i(TAG, "Sucesso ao carregar os comentarios");
                    } else {
                        Log.i(TAG, "Falhou ao carregar os comentarios, mas a task foi concluida");
                        comments = new ArrayList<>();
                    }
                } else {
                    Log.i(TAG, "Falhou ao carregar os comentarios");
                    comments = new ArrayList<>();
                }
                if(comments.size() == 1) {
                    adapter = new CommentsAdapter(comments, DetailsActivity.this, DetailsActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getComentarios();
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onItemClicked(int i) {

    }

    // like do comentario
    @Override
    public void onLikeCliked(int i, boolean like) {
        // true = deu like
        // false = deu deslike

        String idUser = UserInformation.user.getIdUsuario();
        String idPost = UserInformation.post.getIdPostagem();
        String idComment = comments.get(i).getIdComentario();
        int countLikes = comments.get(i).getContadorLikesComentario();
        if (like) {
            Log.i("likes", "Deu like");
            // adiciona o id do usuario que curtiu o comentario
            comments.get(i).setLikesComentarioIdUsuario(Help.addInList(comments.get(i).getLikesComentarioIdUsuario(), idUser));
            // adiciona o id do comentario do usuario atual
            UserInformation.user.setIdsComentarioCurtidos(Help.addInList(UserInformation.user.getIdsComentarioCurtidos(), idComment));
            // atualiza o contador de likes do comentario offline
            countLikes++;
            comments.get(i).setContadorLikesComentario(countLikes);
            // update contador de likes online
        } else { // se tirou o like do comentario então exclui o id do usuario da lista
            Log.i("likes", "Deu deslike");
            // remove id do usuario que descurtiu o comentario
            comments.get(i).setLikesComentarioIdUsuario(Help.removeInList(comments.get(i).getLikesComentarioIdUsuario(), idUser));
            // remove o id do comentario do usuario atual
            UserInformation.user.setIdsComentarioCurtidos(Help.removeInList(UserInformation.user.getIdsComentarioCurtidos(), idComment));
            // atualiza contador de likes do comentario offline
            countLikes--;
            comments.get(i).setContadorLikesComentario(countLikes);
            // update contador de likes do comentatio online
        }
        Log.i("likes", "Atualiza a lista de comentarios no post estatico");
        UserInformation.post.setComentariosPostagem(comments);

    }

    /**
     * Um PROBLEMA o primeiro comentario da publicação nao aparece, somente se entrar e sair da publicacao
     */

    @Override
    protected void onPause() {
        // atualiza o post online
        Log.i(TAG, "Ciclo onPause");
        Log.i("likes", "grava a postagem");
        Operations.gravaPostagem(UserInformation.post);
        // atualiza a lista de quem deu like na postagem - FUNCIONANDO
        super.onPause();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

