package com.geanbrandao.gean.conlubra.fragment;


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
import android.widget.Button;
import android.widget.TextView;

import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.activity.EscreverPublicacaoActivity;
import com.geanbrandao.gean.conlubra.adapter.PublicacaoAdapter;
import com.geanbrandao.gean.conlubra.conexao.InformacoesUsuario;
import com.geanbrandao.gean.conlubra.conexao.InstanciasFirebase;
import com.geanbrandao.gean.conlubra.conexao.Operacoes;
import com.geanbrandao.gean.conlubra.modelo.Postagem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, PublicacaoAdapter.PublicacaoAdapterListener {

    private TextView tvPublicar;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PublicacaoAdapter adapter;

    private static final String TAG = "EscPublicacao";

    private List<Postagem> postagens;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        postagens = new ArrayList<>();

        recyclerView = view.findViewById(R.id.rv_feed);
        swipeRefreshLayout = view.findViewById(R.id.srl_feed);
        swipeRefreshLayout.setOnRefreshListener(this);

        tvPublicar = view.findViewById(R.id.tv_publicar);

        tvPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EscreverPublicacaoActivity.class));
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getPostagens();

        return view;
    }

    private void getPostagens() {
        postagens.clear();
        FirebaseFirestore db = InstanciasFirebase.getFirebaseFirestore();

        db.collection("postagens").orderBy("dataPostagem", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        postagens.add(doc.toObject(Postagem.class));
                        Log.i(TAG, "Id = " + doc.getId());
                    }

                    adapter = new PublicacaoAdapter(postagens, getContext(), FeedFragment.this);
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
        getPostagens();
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public void onLikeCliked(int i, boolean like) {
        List<String> listaAux = new ArrayList<>();
        List<String> listaAuxCurtidas = new ArrayList<>();
        if(InformacoesUsuario.user.getIdsPostagemCurtidas() != null) {
            listaAuxCurtidas = InformacoesUsuario.user.getIdsPostagemCurtidas();
        }
        String id = InformacoesUsuario.user.getIdUsuario();
        int countLikes = postagens.get(i).getContadorLikesPostagem();
        if(like) { // se tem like, então precisa adicionar na lista de likes do post
            if(postagens.get(i).getLikesPostagemIdUsuarios() != null) {
                postagens.get(i).getLikesPostagemIdUsuarios()
                        .add(id);
            } else {
                postagens.get(i).setLikesPostagemIdUsuarios(new ArrayList<String>());
                postagens.get(i).getLikesPostagemIdUsuarios().add(id);
            }
            listaAuxCurtidas.add(postagens.get(i).getIdPostagem());
            countLikes++;
            postagens.get(i).setContadorLikesPostagem(countLikes);
            Operacoes.updateContadorLikes(postagens.get(i));
        } else { // se tirou o like então precisa excluir o id do usuario.
            if(postagens.get(i).getLikesPostagemIdUsuarios() != null) {
                for(String s: postagens.get(i).getLikesPostagemIdUsuarios()) {
                    if(!s.equals(id)){
                        listaAux.add(s);
                    }
                }
                postagens.get(i).setLikesPostagemIdUsuarios(listaAux); // passa a nova lista de likes
            }
            listaAuxCurtidas.remove(InformacoesUsuario.user.getIdUsuario());
            countLikes--;
            postagens.get(i).setContadorLikesPostagem(countLikes);
            Operacoes.updateContadorLikes(postagens.get(i));
        }
        Log.i(TAG, "Tenta atualizar lista de likes da postagem");
        // atualiza lista de postagens curtidas pelo usuario localmente
        InformacoesUsuario.user.setIdsPostagemCurtidas(listaAuxCurtidas);
        // atualiza lista de postagens curtidas pelo usuario no banco
        Operacoes.updateListaPostagensCurtidas(listaAuxCurtidas);
        // atualiza a lista de pessoas que deram like na postagem
        Operacoes.updateListaLikesPostagem(postagens.get(i));
        Operacoes.criarUsuario(InformacoesUsuario.user);
    }

    @Override
    public void onComentarioCliked(int i, boolean like) {

    }
}
