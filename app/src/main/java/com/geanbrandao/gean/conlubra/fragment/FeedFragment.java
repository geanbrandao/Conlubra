package com.geanbrandao.gean.conlubra.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.activity.EscreverPublicacaoActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    private TextView tvPublicar;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        tvPublicar = view.findViewById(R.id.tv_publicar);

        tvPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EscreverPublicacaoActivity.class));
            }
        });

        return view;
    }

}
