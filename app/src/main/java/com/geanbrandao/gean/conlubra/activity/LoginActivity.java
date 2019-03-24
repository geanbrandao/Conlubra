package com.geanbrandao.gean.conlubra.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.fragment.RegisterFragment;
import com.geanbrandao.gean.conlubra.fragment.LoginFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class LoginActivity extends AppCompatActivity {
    private static final String LOGIN = "Login";
    private static final String REGISTER = "Registre-se";

    private ViewPager viewPager;
    private SmartTabLayout smartTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewPager = findViewById(R.id.viewpager);
        smartTabLayout = findViewById(R.id.viewpagertab);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(LOGIN, LoginFragment.class)
                .add(REGISTER, RegisterFragment.class)
                .create()
        );

        // seta o adapter, recebe os fragments no view pager
        viewPager.setAdapter(adapter);
        // configura
        smartTabLayout.setViewPager(viewPager);
    }
}
