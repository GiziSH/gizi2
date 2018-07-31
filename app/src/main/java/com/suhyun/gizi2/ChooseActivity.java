package com.suhyun.gizi2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by suhyun on 2018-03-28.
 */

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        ImageButton user = (ImageButton)findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//버튼 눌렸을 때
                Intent intent = new Intent(ChooseActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton manager = (ImageButton)findViewById(R.id.manager);
        manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//버튼 눌렸을 때
                Intent intent = new Intent(ChooseActivity.this, signin.class);
                startActivity(intent);
            }
        });
    }
}