package com.geanbrandao.gean.conlubra.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.geanbrandao.gean.conlubra.DownloadImageTask;
import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.adapter.CommentsAdapter;
import com.geanbrandao.gean.conlubra.connection.UserInformation;
import com.geanbrandao.gean.conlubra.connection.ConnectionFirebase;
import com.geanbrandao.gean.conlubra.model.Comentario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, CommentsAdapter.ComentariosAdapaterListener {

    private static final String TAG = "ListaComentarios";
    private static final String COMMENTS = "comentarios";
    private static final String DATE_COMMENT = "dataComentario";

    private CircleImageView civPostDetails;
    private CircleImageView civProfileComment;
    private ImageView ivPostDetails;
    private TextView tvName, tvDate, tvPublicacao, countLikes, countComment;
    private Button like, comment;

    private Button bPublish;
    private EditText edPublishComment;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommentsAdapter adapter;

    private List<Comentario> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        comments = new ArrayList<>();

        recyclerView = findViewById(R.id.rv_comentarios);
        swipeRefreshLayout = findViewById(R.id.srl_comentarios);
        swipeRefreshLayout.setOnRefreshListener(this);

        civPostDetails = findViewById(R.id.civPefilPost);
        civProfileComment = findViewById(R.id.civPerfilComentario);
        tvName = findViewById(R.id.tvDetalhesPostNome);
        tvDate = findViewById(R.id.tvDetalhesPostData);
        tvPublicacao = findViewById(R.id.tvDetalhesPostPublicacao);
        countLikes = findViewById(R.id.tvDetalhesPostContadorLikes);
        countComment = findViewById(R.id.tvDetalhesPostContadorComentarios);
        ivPostDetails = findViewById(R.id.ivFotoDetalhesPublicacao);
        like = findViewById(R.id.bDetalhesPostLike);
        comment = findViewById(R.id.bDetalhesPostComentario);

        /*
            Inicia detalhes da publicacao
        */
        tvName.setText(UserInformation.post.getNomeAutorPostagem());
        tvDate.setText(formataData(UserInformation.post.getDataPostagem()));
        tvPublicacao.setText(UserInformation.post.getConteudoPostagem());
        countLikes.setText(UserInformation.post.getContadorLikesPostagem()+"");
        countComment.setText(UserInformation.post.getContadorComentariosPostagem()+"");
        if(UserInformation.post.getFotoAutorPostagem() != null) {
            new DownloadImageTask(civPostDetails).execute(UserInformation.post.getFotoAutorPostagem());
        }
        if(UserInformation.post.getImagemPostagem() != null) {
            new DownloadImageTask(ivPostDetails).execute(UserInformation.post.getImagemPostagem());
        }
        if(isLike(UserInformation.post.getIdPostagem())){
            like.setBackground(this.getDrawable(R.drawable.ic_favorite_black_24dp));
        } else {
            like.setBackground(this.getDrawable(R.drawable.ic_favorite_border_black_24dp));
        }

        bPublish = findViewById(R.id.bComentar);
        edPublishComment = findViewById(R.id.edComentar);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getComentarios();

        bPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Comentario novoComentario = new Comentario();
                String coment = edPublishComment.getText().toString().trim();

                if(!coment.isEmpty()) {
                    novoComentario.setConteudoComentario(coment);
                }
                novoComentario.set*/
            }
        });


    }

    private String formataData(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm '-' dd/MM/yyyy", Locale.US);
        return dateFormatter.format(date);
    }

    private boolean isLike(String id) { //
        if (UserInformation.user.getIdsPostagemCurtidas() != null ) {
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

        db.collection(COMMENTS).orderBy(DATE_COMMENT, Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc: task.getResult()){
                        comments.add(doc.toObject(Comentario.class));
                        Log.i(TAG, "Comentario Id = " + doc.getId());
                    }

                    adapter = new CommentsAdapter(comments, getApplicationContext(), DetailsActivity.this);
                    recyclerView.setAdapter(adapter);
                    Log.i(TAG, "Adapater carregado");
                } else {
                    Log.i(TAG, "Falhou ao carregar o Adapater");
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
    public void onItemClicked(int position) {

    }

    @Override
    public void onLikeCliked(int position, boolean like) {

    }
}
