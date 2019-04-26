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


public class QuiFragment extends Fragment {

    private List<ItemProgramacao> programacao;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgrammingAdapter mAdapter;

    public QuiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quinta_geral, container, false);

        recyclerView = view.findViewById(R.id.rv_quinta_geral);
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
        Log.i("CicloVida", "pega programacao quinta");
        programacao.clear();
        // add programacao estatica
        ItemProgramacao p = new ItemProgramacao();
        p.setNome("Mesa 1 - Pesquisas e avanços  nos estudos sobre TEA: diferentes olhares");
        p.setHora("08:30");
        p.setLocal("Graciela Pignatari - TISMOO\n" +
                "Gilberto Garcias - UFPEL\n" +
                "Aderbal Sabra - UNIGRANRIO\n" +
                "Carlo Schimidt UFSM\n" +
                "Mediação: Siglia Camargo UFPEL");
        p.setDetalhes("");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Intervalo");
        p.setHora("10:30 - 11h");
        p.setLocal("");
        p.setDetalhes("");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Newra Rotta - Plasticidade cerebral e aprendizagem - UFRGS");
        p.setHora("11h");
        p.setLocal("");
        p.setDetalhes("");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Mesa 2 - Caminhos e descaminhos da inclusão: da educação básica ao ensino superior");
        p.setHora("13:30");
        p.setLocal("Mônica Santos - UFRJ\n" +
                "Fabiane Adela Tonetto Costas - UFSM\n" +
                "Franceli Brizola - UFPR\n" +
                "Mediação -  Rita de Cássia Morem Cóssio Rodriguez UFPEL");
        p.setDetalhes("");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Intervalo");
        p.setHora("16h - 16:30");
        p.setLocal("");
        p.setDetalhes("");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Comunicações orais e Pôsters");
        p.setHora("16:30 - 19h");
        p.setLocal("");
        p.setDetalhes("");
        programacao.add(p);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("CicloVida", "quinta - onResume");
        getProgramacao();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("CicloVida", "quinta - onPause");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("CicloVida", "quinta - onStart");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("CicloVida", "quarta - onDestroy");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("CicloVida", "quinta - onStop");
    }

}
