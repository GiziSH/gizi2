package com.suhyun.gizi2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {
    private static String TAG = "phptest_MainActivity";
    private static final String TAG_JSON = "webnautes";
    private static final String TAG_name = "name";
    private static final String TAG_line = "line";
    private static final String TAG_bookmark = "bookmark";
    String mJsonString;
    ArrayList<HashMap<String, String>> mArrayList;

    ImageButton mBtn; //편집버튼

    private List<String> list_bookmark;
    private SharedPreferences pref1;
    private SharedPreferences.Editor editor1;
    ListView bm_listview;

    public Fragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment1, container, false);

        //pref1 = getActivity().getSharedPreferences("pref1", getActivity().MODE_PRIVATE);
        //editor1 = pref1.edit();

        bm_listview = (ListView) v.findViewById(R.id.bookmark_listview);
        TextView tx = (TextView) v.findViewById(R.id.empty2_text);
        bm_listview.setEmptyView(tx);
        bookmarkDB bmDB = new bookmarkDB();
        bmDB.execute("http://192.168.200.199/select_bookmark.php");
        //bmDB.execute("http://192.168.0.15/select_bookmark.php"); //티아모

/*
        mBtn = (ImageButton)v.findViewById(R.id.popup_menu);
        mBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                final PopupMenu popup = new PopupMenu(getContext(),v); //팝업메뉴 객체만듦
                //xml파일에 메뉴 정의한 것 가져오기  위한 전개자 선언
                MenuInflater inflater = popup.getMenuInflater();
                Menu menu = popup.getMenu();
                //실제 메뉴 정의한 것 가져오는 부분 menu객체 넣어주기
                inflater.inflate(R.menu.popupmenu, menu);


                //메뉴 클릭했을 때 처리하는 부분
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                    public int position;

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.popup_select:
                                ArrayAdapter<String> rsadapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_multiple_choice,list_bookmark);
                                bm_listview.setAdapter(rsadapter2);
                                bm_listview.setItemsCanFocus(true);
                                bm_listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                                popup.dismiss();
                                break;
                            case R.id.popup_delete:
                                SparseBooleanArray checkedItems = bm_listview.getCheckedItemPositions();
                                int count = rsadapter.getCount() ;
                                for (int i = count-1; i >= 0; i--) {
                                    if (checkedItems.get(i)) {
                                        list_names.remove(i) ;
                                    }
                                }
                                // 모든 선택 상태 초기화.
                                rs_listview.clearChoices() ;
                                saveLately();
                                rsadapter.notifyDataSetChanged();
                                rs_listview.setAdapter(rsadapter);
                                break;
                            case R.id.popup_allselect:
                                rsadapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_multiple_choice,list_bookmark);
                                rs_listview.setAdapter(rsadapter2);
                                rs_listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                                popup.dismiss();

                                count = rsadapter.getCount() ;

                                for (int i=0; i<count; i++) {
                                    rs_listview.setItemChecked(i, true) ;
                                }
                                popup.dismiss();

                                break;

                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

*/
        return v;
    }

    private class bookmarkDB extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String data = "";
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getContext(),
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            progressDialog.dismiss();
            Log.d(TAG, "response  - " + data);

            if (data == null) {

            } else {

                mJsonString = data;
                showbookmark();

            }
        }

        @Override
        protected String doInBackground(String... params) {

            /* 인풋 파라메터값 생성 */
            String serverURL = params[0];
            try {
                /* 서버연결 */
                URL url = new URL(serverURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                //outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;
                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();

                /* 서버에서 응답 */
                Log.e("RECV DATA", data);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }


    }

    private void showbookmark() {
        list_bookmark = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);


                String name = item.getString(TAG_name);

                list_bookmark.add(name);

            }

            ArrayAdapter<String> bmadapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_list,list_bookmark);

            bm_listview.setAdapter(bmadapter);

        } catch (JSONException e) {

            //Log.d(TAG, "showResult : ", e);
        }

    }


}
