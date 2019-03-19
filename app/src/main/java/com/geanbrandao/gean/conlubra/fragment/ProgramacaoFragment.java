package com.geanbrandao.gean.conlubra.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.fragment.subFragmentGeral.QuartaGeralFragment;
import com.geanbrandao.gean.conlubra.fragment.subFragmentGeral.QuintaGeralFragment;
import com.geanbrandao.gean.conlubra.fragment.subFragmentGeral.SabadoGeralFragment;
import com.geanbrandao.gean.conlubra.fragment.subFragmentGeral.SextaGeralFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramacaoFragment extends Fragment {


    private ViewPager viewPagerProgramacao;
    private SmartTabLayout smartTabLayoutProgramacao;

    public ProgramacaoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_programacao, container, false);

        viewPagerProgramacao = view.findViewById(R.id.viewPagerTabProgramacao);
        smartTabLayoutProgramacao = view.findViewById(R.id.smartTabProgramacao);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getFragmentManager(), FragmentPagerItems.with(getContext())
                .add("Qua - 28", QuartaGeralFragment.class)
                .add("Qui - 29", QuintaGeralFragment.class)
                .add("Sex - 30", SextaGeralFragment.class)
                .add("Sab - 31", SabadoGeralFragment.class)
                .create()
        );
        // seta o adapter, recebe os fragments no view pager
        viewPagerProgramacao.setAdapter(adapter);
        // configura
        smartTabLayoutProgramacao.setViewPager(viewPagerProgramacao);

        return view;
    }

}
