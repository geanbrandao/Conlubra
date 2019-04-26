package com.geanbrandao.gean.conlubra2.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geanbrandao.gean.conlubra2.R;
import com.geanbrandao.gean.conlubra2.fragment.subFragmentGeral.QuaFragment;
import com.geanbrandao.gean.conlubra2.fragment.subFragmentGeral.QuiFragment;
import com.geanbrandao.gean.conlubra2.fragment.subFragmentGeral.SexFragment;
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
                getChildFragmentManager(), FragmentPagerItems.with(getContext())
                .add("Qua - 28/08", QuaFragment.class)
                .add("Qui - 29/08", QuiFragment.class)
                .add("Sex - 30/08", SexFragment.class)
                .create()
        );
        // seta o adapter, recebe os fragments no view pager
        viewPagerProgramming.setAdapter(adapter);
        // configura
        smartTabLayoutProgramming.setViewPager(viewPagerProgramming);

        return view;
    }

}
