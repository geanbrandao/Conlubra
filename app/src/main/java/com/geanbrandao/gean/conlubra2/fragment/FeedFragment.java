package com.geanbrandao.gean.conlubra2.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.geanbrandao.gean.conlubra2.R;
import com.geanbrandao.gean.conlubra2.activity.DetailsActivity;
import com.geanbrandao.gean.conlubra2.activity.WritePublicationActivity;
import com.geanbrandao.gean.conlubra2.adapter.PublicationAdapter;
import com.geanbrandao.gean.conlubra2.connection.UserInformation;
import com.geanbrandao.gean.conlubra2.connection.ConnectionFirebase;
import com.geanbrandao.gean.conlubra2.connection.Operations;
import com.geanbrandao.gean.conlubra2.model.Postagem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, PublicationAdapter.PublicacaoAdapterListener {

    private static final String TAG = "EscPublicacao";
    private static final String POSTS = "postagens";
    private static final String DATE_POST = "dataPostagem";

    private TextView tvPublication;
    private CircleImageView civProfilePicture;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PublicationAdapter adapter;


    private List<Postagem> posts;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        posts = new ArrayList<>();

        recyclerView = view.findViewById(R.id.rv_feed);
        swipeRefreshLayout = view.findViewById(R.id.srl_feed);
        swipeRefreshLayout.setOnRefreshListener(this);

        tvPublication = view.findViewById(R.id.tv_publicar);
        civProfilePicture = view.findViewById(R.id.civFotoPerfilFeed);

        tvPublication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), WritePublicationActivity.class));
            }
        });

        if(UserInformation.user.getImagemPerfilUrl() != null){
            Glide.with(getContext())
                    .load(UserInformation.user.getImagemPerfilUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(civProfilePicture);
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getPostagens();
        adapter = new PublicationAdapter(posts, getContext(), FeedFragment.this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void getPostagens() {
        posts.clear();
        FirebaseFirestore db = ConnectionFirebase.getFirebaseFirestore();

        db.collection(POSTS).orderBy(DATE_POST, Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        posts.add(doc.toObject(Postagem.class));
                        Log.i(TAG, "Id = " + doc.getId());
                    }
                    adapter.notifyDataSetChanged();

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
        getPostagens();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClicked(int i) {
        UserInformation.post = posts.get(i);
        startActivity(new Intent(getContext(), DetailsActivity.class));
    }

    @Override
    public void onLikeCliked(int i, boolean like) {
        List<String> listaAux = new ArrayList<>();
        List<String> listaAuxCurtidas = new ArrayList<>();
        if(UserInformation.user.getIdsPostagemCurtidas() != null) {
            listaAuxCurtidas = UserInformation.user.getIdsPostagemCurtidas();
        }
        String id = UserInformation.user.getIdUsuario();
        int countLikes = posts.get(i).getContadorLikesPostagem();
        if(like) { // se tem like, então precisa adicionar na lista de likes do post
            if(posts.get(i).getLikesPostagemIdUsuarios() != null) {
                posts.get(i).getLikesPostagemIdUsuarios()
                        .add(id);
            } else {
                posts.get(i).setLikesPostagemIdUsuarios(new ArrayList<String>());
                posts.get(i).getLikesPostagemIdUsuarios().add(id);
            }
            listaAuxCurtidas.add(posts.get(i).getIdPostagem());
            countLikes++;
            posts.get(i).setContadorLikesPostagem(countLikes);
            Operations.updateContadorLikes(posts.get(i));
        } else { // se tirou o like então precisa excluir o id do usuario.
            if(posts.get(i).getLikesPostagemIdUsuarios() != null) {
                for(String s: posts.get(i).getLikesPostagemIdUsuarios()) {
                    if(!s.equals(id)){
                        listaAux.add(s);
                    }
                }
                posts.get(i).setLikesPostagemIdUsuarios(listaAux); // passa a nova lista de likes
            }
            listaAuxCurtidas.remove(posts.get(i).getIdPostagem());
            countLikes--;
            posts.get(i).setContadorLikesPostagem(countLikes);
            Operations.updateContadorLikes(posts.get(i));
        }
        Log.i(TAG, "Tenta atualizar lista de likes da postagem");
        // atualiza lista de postagens curtidas pelo usuario localmente
        UserInformation.user.setIdsPostagemCurtidas(listaAuxCurtidas);
        // atualiza lista de postagens curtidas pelo usuario no banco
        Operations.updateListaPostagensCurtidas(listaAuxCurtidas);
        // atualiza a lista de pessoas que deram like na postagem
        Operations.updateListaLikesPostagem(posts.get(i));
        Operations.criarUsuario(UserInformation.user);
    }

    @Override
    public void onComentarioCliked(int i) {
        UserInformation.post = posts.get(i);
        startActivity(new Intent(getContext(), DetailsActivity.class));
    }

    @Override
    public void onImageCliked(int i) {
        UserInformation.post = posts.get(i);
        startActivity(new Intent(getContext(), DetailsActivity.class));
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
