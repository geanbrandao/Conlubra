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


public class SexFragment extends Fragment {

    private List<ItemProgramacao> programacao;
    private RecyclerView recyclerView;
    private ProgrammingAdapter mAdapter;

    public SexFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sexta_geral, container, false);

        recyclerView = view.findViewById(R.id.rv_sexta_geral);
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
        p.setNome("Mesa 3 - Intervenção Precoce");
        p.setHora("8:30");
        p.setLocal("Ana Paula Pereira - Universidade do Minho-PT\n" +
                "Drª Vera Lucia Israel - UFPR\n" +
                "Claudete Lima - UNIPAMPA\n" +
                "Débora Jacks-  CAA e UFPEL");
        p.setDetalhes("");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Intervalo");
        p.setHora("10:30 - 11h");
        p.setLocal("");
        p.setDetalhes("");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Palestra \"Desenvolvimento infantil e importância da Intervenção Precoce\"\n" +
                "Ricardo Halpern - UFCSPA");
        p.setHora("11h");
        p.setLocal("");
        p.setDetalhes("");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Mesa 4");
        p.setHora("13:30 - 15:45");
        p.setLocal("Cláudia Frutuoso- movimento orgulho autista  Brasil\n" +
                "Láu Patron (mãe e autora - POA– Livro 71 leões)\n" +
                "Nádia Porto - ser asperger\n" +
                "Movimento Down - Vaneza Pereira");
        p.setDetalhes("");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Em paralelo");
        p.setHora("13:30 - 15:30");
        p.setLocal("Mini-cursos");
        p.setDetalhes("");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Intervalo");
        p.setHora("15:45 - 16h");
        p.setLocal("");
        p.setDetalhes("");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Lançamentos de livros");
        p.setHora("16h");
        p.setLocal("");
        p.setDetalhes("");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Mesa de encerramento - Práticas em TEA e Educação Inclusiva");
        p.setHora("17h");
        p.setLocal("Dan Pascolli-  FFMUSP");
        p.setDetalhes("");
        programacao.add(p);

        p = new ItemProgramacao();
        p.setNome("Mesa de encerramento - Práticas em TEA e Educação Inclusiva");
        p.setHora("18h");
        p.setLocal("Carla Gikovate - Clínica Neurológica Professor Fernando Pompeu. RJ\n" +
                "Mediação UERGS");
        p.setDetalhes("");
        programacao.add(p);

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("CicloVida", "Sexta - onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("CicloVida", "Sexta - onPause");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("CicloVida", "Sexta - onStart");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("CicloVida", "Sexta - onDestroy");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("CicloVida", "Sexta - onStop");
    }

}
