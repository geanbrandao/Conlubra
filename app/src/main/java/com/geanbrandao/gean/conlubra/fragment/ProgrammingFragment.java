package com.geanbrandao.gean.conlubra.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.fragment.subFragmentGeral.QuaFragment;
import com.geanbrandao.gean.conlubra.fragment.subFragmentGeral.QuiFragment;
import com.geanbrandao.gean.conlubra.fragment.subFragmentGeral.SabFragment;
import com.geanbrandao.gean.conlubra.fragment.subFragmentGeral.SexFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgrammingFragment extends Fragment {


    private ViewPager viewPagerProgramming;
    private SmartTabLayout smartTabLayoutProgramming;

    public ProgrammingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_programacao, container, false);

        viewPagerProgramming = view.findViewById(R.id.viewPagerTabProgramacao);
        smartTabLayoutProgramming = view.findViewById(R.id.smartTabProgramacao);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getFragmentManager(), FragmentPagerItems.with(getContext())
                .add("Qua - 28", QuaFragment.class)
                .add("Qui - 29", QuiFragment.class)
                .add("Sex - 30", SexFragment.class)
                .add("Sab - 31", SabFragment.class)
                .create()
        );
        // seta o adapter, recebe os fragments no view pager
        viewPagerProgramming.setAdapter(adapter);
        // configura
        smartTabLayoutProgramming.setViewPager(viewPagerProgramming);

        return view;
    }

}
