package com.suhyun.gizi2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by suhyun on 2018-08-06.
 */

public class ToiletAdapter extends ArrayAdapter<Toilet> {

    //private Fragment fragment;
    private Activity activity;
    private ArrayList<Toilet> toilets;
    private final String TAG = ToiletAdapter.class.getSimpleName();

    private List<String> list_bookmark;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    //private ArrayList<Toilet> items = new ArrayList<Toilet>(); //모든 데이터 arraylist


    private List<Toilet> newitems = null;



    public ToiletAdapter(Activity activity, int resource,ArrayList<Toilet> toilets){
        super(activity, resource, toilets);
        this.activity = activity;
        this.toilets = toilets;
        Log.i(TAG, "init adapter");
    }



/* items는 모두 toilets으로 바꿔야함
    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public Object getItem(int position)
    {
        return items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }
*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final int pos = position;
        ViewHolder holder = null;
        final Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_listview, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        }else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        Toilet listViewItem = toilets.get(position);

        ImageView image = (ImageView) convertView.findViewById(R.id.imgsearch);
        TextView  name = (TextView) convertView.findViewById(R.id.toiletname);
        TextView  line = (TextView) convertView.findViewById(R.id.toiletline);


        holder.image.setImageDrawable(listViewItem.getImg());
        holder.name.setText(listViewItem.getToiletname());
        holder.line.setText(listViewItem.getToiletline());

        //Fragment3 fragment3 = new Fragment3();
        //set event for checkbox
        holder.check.setOnCheckedChangeListener(onCheckedChangeListener(listViewItem));
        //showbookmark();

        return convertView;
    }


        private CompoundButton.OnCheckedChangeListener onCheckedChangeListener(final Toilet t) {
            return new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        String str = new String(t.getToiletname());
                        addbookmark(str);
                        //savebookmark();
                        System.out.println("체크됨");
                        t.setSelected(true);
                    } else {
                        System.out.println("체크풀림");
                        t.setSelected(false);
                    }
                }
            };
        }

    private class ViewHolder {
        private ImageView image;
        private TextView name;
        private TextView line;
        private CheckBox check;

        public ViewHolder(View v) {
            image = (ImageView) v.findViewById(R.id.imgsearch);
            name = (TextView) v.findViewById(R.id.toiletname);
            line = (TextView) v.findViewById(R.id.toiletline);
            check = (CheckBox) v.findViewById(R.id.checkboxbookmark);
        }
    }
    //즐겨찾기
    //배열안에 집어넣기
    public void addbookmark(String value) {

        String str1 = new String();

        for(int i =0; i<list_bookmark.size(); i++){//중복검사
            str1 = list_bookmark.get(i);
            if (str1.equals(value)){
                list_bookmark.remove(value);
                list_bookmark.add(value);
                return;
            }
        }
        list_bookmark.add(value);
    }
    //내부메모리에 저장
    public void savebookmark(){
        JSONArray array = new JSONArray();
        for(int i=0; i<list_bookmark.size();i++){
            array.put(list_bookmark.get(i));
        }
        String a = array.toString();

        editor.putString("bookmark", a);
        editor.commit();
    }
    //내부메모리에서 불러오기
    public void showbookmark(){
        String json = pref.getString("bookmark", null);
        if (json != null){
            try{
                JSONArray array = new JSONArray(json);
                list_bookmark.clear();

                for(int i = array.length() - 1; i>=0;i--){
                    String url = array.optString(i);
                    list_bookmark.add(url);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    //데이터 넣기
    public void addtoilet(Drawable icon, String toiletname, String toiletline){
        Toilet toilet = new Toilet();

        toilet.setImg(icon);
        toilet.setToiletname(toiletname);
        toilet.setToiletline(toiletline);

        toilets.add(toilet);
        System.out.println(toilets);
    }

/*
    public void filter(String charText) {
        //Pattern p = Pattern.compile("^[a-zA-Z가-힣]*$");
        //Matcher m = p.matcher(charText);

        newitems.clear();

        if (charText.length() == 0) {
            newitems.addAll(items);
        }
        else {
            for(Toilet to : items){
                if (to.getToiletname().toLowerCase().contains(charText)){
                    newitems.add(to);
                }
            }/*
            for (int i=0; i<items.size();i++){
                String str = items.get(i).getToiletname();

                if (str.toLowerCase().contains(charText)) {

                    newitems.add(items.get(i));
                }
            }///
        }
        notifyDataSetChanged();
    }*/


}
