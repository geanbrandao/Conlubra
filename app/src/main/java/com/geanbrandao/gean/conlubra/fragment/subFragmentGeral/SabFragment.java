package com.geanbrandao.gean.conlubra.fragment.subFragmentGeral;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.adapter.ProgrammingAdapter;
import com.geanbrandao.gean.conlubra.model.ItemProgramacao;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SabFragment extends Fragment implements ProgrammingAdapter.ProgramacaoAdapaterListener {

    private final String USER_ID = "user01";

    private List<ItemProgramacao> programacao = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgrammingAdapter mAdapter;

    public SabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sabado_geral, container, false);

        recyclerView = view.findViewById(R.id.rv_sabado_geral);

        getProgramacao();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ProgrammingAdapter(getContext(), programacao, this);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    private void getProgramacao() {
        programacao.clear();
        // add programacao estatica
        ItemProgramacao p = new ItemProgramacao();
        p.setNome("Palestra 1");
        p.setHora("15:00 - 15:30");
        p.setLocal("Sala 102");
        p.setDetalhes("Palestra sobre palestra sobree");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Palestra 3");
        p.setHora("18:00 - 18:30");
        p.setLocal("Sala 132");
        p.setDetalhes("Palestra sobre palestra sobreeada ");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Palestra 2");
        p.setHora("11:00 - 11:30");
        p.setLocal("Sala 112");
        p.setDetalhes("Palestra sobre palestra sobreada ad e");
        programacao.add(p);
    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public void onFavoritoCliked(int position, boolean like) {

    }
}
