package com.geanbrandao.gean.conlubra.fragment.subFragmentGeral;

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

import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.adapter.ProgramacaoAdapter;
import com.geanbrandao.gean.conlubra.modelo.ItemProgramacao;

import java.util.ArrayList;
import java.util.List;


public class QuintaGeralFragment extends Fragment implements ProgramacaoAdapter.ProgramacaoAdapaterListener {

    private List<ItemProgramacao> programacao = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgramacaoAdapter mAdapter;

    public QuintaGeralFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quinta_geral, container, false);

        recyclerView = view.findViewById(R.id.rv_quinta_geral);

        Log.i("CicloVida", "onCreate");
        getProgramacao();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ProgramacaoAdapter(getContext(), programacao, this);
        recyclerView.setAdapter(mAdapter);


        return view;
    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public void onFavoritoCliked(int position, boolean like) {

    }


    private void getProgramacao() {
        programacao.clear();
        // add programacao estatica
        ItemProgramacao p = new ItemProgramacao();
        p.setNome("Palestra 5");
        p.setHora("15:00 - 15:30");
        p.setLocal("Sala 102");
        p.setDetalhes("Palestra sobre palestra sobree");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Palestra 6");
        p.setHora("18:00 - 18:30");
        p.setLocal("Sala 132");
        p.setDetalhes("Palestra sobre palestra sobreeada ");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Palestra 7");
        p.setHora("11:00 - 11:30");
        p.setLocal("Sala 112");
        p.setDetalhes("Palestra sobre palestra sobreada ad e");
        programacao.add(p);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("CicloVida", "onResume");
        getProgramacao();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("CicloVida", "onPause");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("CicloVida", "onStart");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("CicloVida", "onDestroy");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("CicloVida", "onStop");
    }
}
