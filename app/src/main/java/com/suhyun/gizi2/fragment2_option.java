package com.suhyun.gizi2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by suhyun on 2018-07-08.
 */

public class fragment2_option extends Fragment {
    TextView textview;


    public fragment2_option() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment2_option, container, false);

        ImageButton girl = (ImageButton)view.findViewById(R.id.girl);
        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//버튼 눌렸을 때
                fragment2_toiletstate ts = new fragment2_toiletstate();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, ts);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //Intent intent = new Intent(ChooseActivity.this, MainActivity.class);
                //startActivity(intent);
            }
        });

        return view;
    }

}