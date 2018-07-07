package com.suhyun.gizi2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment2 extends Fragment {

    Button mBtn;

    public Fragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment2, container, false);

        mBtn = (Button)v.findViewById(R.id.popup_menu);
        mBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(),v); //팝업메뉴 객체만듦

                //xml파일에 메뉴 정의한 것 가져오기  위한 전개자 선언
                MenuInflater inflater = popup.getMenuInflater();
                Menu menu = popup.getMenu();

                //실제 메뉴 정의한 것 가져오는 부분 menu객체 넣어주기
                inflater.inflate(R.menu.popupmenu, menu);

                //메뉴 클릭했을 때 처리하는 부분
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.popup_select:
                                mBtn.setBackgroundColor(Color.RED);
                                break;
                            case R.id.popup_delete:
                                mBtn.setBackgroundColor(Color.BLUE);
                                break;

                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
        return v;
    }

}
