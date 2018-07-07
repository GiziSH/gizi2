package com.suhyun.gizi2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment3 extends Fragment  {
    private static String TAG = "phptest_MainActivity";
    private static final String TAG_JSON="webnautes";
    private static final String TAG_name = "name";
    private static final String TAG_line = "line";
    private static final String TAG_bookmark="bookmark";

    private TextView mTextViewResult;
    ArrayList<HashMap<String, String>> mArrayList;
    ArrayList<HashMap<String, String>> mArrayList2;
    ListView mlistView;
    String mJsonString;

    private Spinner MySpinner1;         //검색 선택
    private EditText editSearch;

    public Fragment3() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment3, container, false);

        mTextViewResult = (TextView)v.findViewById(R.id.textView_main_result);
        mlistView = (ListView) v.findViewById(R.id.listView);
        mArrayList = new ArrayList<>();
        mArrayList2 = new ArrayList<>();
        //GetData task = new GetData();
        //task.execute("http://192.168.0.7/searchtest.php");


        CheckBox favorite = (CheckBox) v.findViewById(R.id.checkboxbookmark) ;

        // 지하철역,휴게소 선택
        String [] values1 = {"선택","지하철역","휴게소"};
        MySpinner1 = (Spinner)v.findViewById(R.id.option);
        final ArrayAdapter<String> adapterSpinner1 = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item);
        adapterSpinner1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        MySpinner1.setAdapter(adapterSpinner1);
        adapterSpinner1.addAll(values1);
        MySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 switch (position){
                     case 0:
                         mArrayList.clear();
                         break;
                     case 1:
                         mArrayList.clear();
                         GetData task = new GetData();
                         task.execute("http://192.168.0.7/subway_search.php");
                         break;
                     case 2:
                         mArrayList.clear();
                         GetData task2 = new GetData();
                         task2.execute("http://192.168.0.7/restarea_search.php");
                         break;
                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });


        editSearch = (EditText) v.findViewById(R.id.editSearch);



        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                option op = new option();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, op);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //Intent intent = new Intent(getActivity(),option.class);
                //startActivity(intent);

            }
        });
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //start 지점에서 시작되는 count갯수만큼 글자들이 after길이만큼의 글자로 대치되려고 할때 호출됨
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            //start 지점에서 시작되는 before 갯수만큼의 글자들이 count갯수만큼의 글자드롤 대치되었을 때 호출
            }

            @Override
            public void afterTextChanged(Editable s) {//edittext의 텍스트가 변경되면 호출
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });


        return v;
    }

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getContext(),
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "response  - " + result);

            if (result == null){

                mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult();

            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult(){

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            CheckBox favorite = (CheckBox) getView().findViewById(R.id.checkboxbookmark) ;


            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);


                String name = item.getString(TAG_name);
                String line = item.getString(TAG_line);
                String bookmark = item.getString(TAG_bookmark);

                HashMap<String, String> hashMap = new HashMap<>();


                hashMap.put(TAG_name, name);
                hashMap.put(TAG_line, line);
                hashMap.put(TAG_bookmark, bookmark);

                //System.out.println();
                mArrayList.add(hashMap);

                /*if(bookmark.equals("1")){

                    System.out.println(mArrayList.get(i));

                }*/
            }

            ListAdapter madapter = new SimpleAdapter(
                    getContext(), mArrayList, R.layout.row_listview,
                    new String[]{TAG_name,TAG_line},
                    new int[]{R.id.toiletname, R.id.toiletline}
            )/*{

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View view = super.getView(position, convertView, parent);
                    ImageView imgsearch = (ImageView) view.findViewById(R.id.imgsearch);
                    TextView toiletname = (TextView) view.findViewById(R.id.toiletname);
                    TextView toiletline = (TextView) view.findViewById(R.id.toiletline);
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkboxbookmark);

                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            CheckBox checkBox = (CheckBox) compoundButton.findViewById(R.id.checkbox);
                            if (checkBox.isChecked()) {
                                checkBox.setChecked(b);
                            }
                        }
                    });

                    return view;
                }
            }*/
            ;








            mlistView.setAdapter(madapter);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }


    public void search(String charText){

        //Pattern p = Pattern.compile(".*"+charText+".*");
        Pattern p = Pattern.compile("^[a-zA-Z가-힣]*$");
        Matcher m = p.matcher(charText);
        mArrayList2.clear();

        if (charText.length() == 0) {
            mArrayList2.addAll(mArrayList);
        }
        else {
            for (int i=0; i<mArrayList.size();i++){
                String str = mArrayList.get(i).get("name");



                if (str.toLowerCase().contains(charText)) {

                    mArrayList2.add(mArrayList.get(i));
                }
            }
        }

        ListAdapter madapter = new SimpleAdapter(
                getContext(), mArrayList2, R.layout.row_listview,
                new String[]{TAG_name,TAG_line},
                new int[]{R.id.toiletname, R.id.toiletline}

        );

        mlistView.setAdapter(madapter);

    }



    public void onListItemClick(ListView l, View v, int position, long id) {

    }



}