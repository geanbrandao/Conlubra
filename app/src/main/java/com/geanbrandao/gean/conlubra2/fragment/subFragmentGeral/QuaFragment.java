package com.geanbrandao.gean.conlubra2.fragment.subFragmentGeral;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geanbrandao.gean.conlubra2.R;
import com.geanbrandao.gean.conlubra2.adapter.ProgrammingAdapter;
import com.geanbrandao.gean.conlubra2.model.ItemProgramacao;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuaFragment extends Fragment {


    private List<ItemProgramacao> programacao;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgrammingAdapter mAdapter;


    public QuaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quarta_geral, container, false);

        recyclerView = view.findViewById(R.id.rv_quarta_geral);
        programacao = new ArrayList<>();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ProgrammingAdapter(getContext(), programacao);
        recyclerView.setAdapter(mAdapter);
        getProgramacao();

        return view;
    }

    private void getProgramacao() {
        programacao.clear();
        // add programacao estatica
        ItemProgramacao p = new ItemProgramacao();
        p.setNome("Credenciamento");
        p.setHora("13:30");
        p.setLocal("");
        p.setDetalhes("");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Abertura oficial");
        p.setHora("18h");
        p.setLocal("");
        p.setDetalhes("");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Mesa de abertura: Políticas e práticas de inclusão Brasil-Portugal");
        p.setHora("19h");
        p.setLocal("David Rodrigues - PT\n" +
                "Renato Duro - FURG\n" +
                "Mediação - Fátima Cóssio UFPEL");
        p.setDetalhes("");
        programacao.add(p);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("CicloVida", "quarta - onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("CicloVida", "quarta - onPause");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("CicloVida", "quarta - onStart");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("CicloVida", "quarta - onDestroy");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("CicloVida", "quarta - onStop");
    }

}
