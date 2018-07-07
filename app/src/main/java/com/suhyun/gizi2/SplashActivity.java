package com.suhyun.gizi2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by suhyun on 2018-03-22.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savednstanceState) {
        super.onCreate(savednstanceState);

        try{
            Thread.sleep(1000); //대기 초 설정
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this, ChooseActivity.class));
        finish();
    }
}
