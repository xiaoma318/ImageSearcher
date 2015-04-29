package com.chengma.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by chengma on 2/5/15.
 */

public class HistoryActivity extends ActionBarActivity {
    private ListView lv;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Read search history from SharedPreferences object
        SharedPreferences sharedPreferences = getSharedPreferences("search_history.dat", Context.MODE_PRIVATE);
        Set<String> stringSet = sharedPreferences.getStringSet("search", null);
        lv = (ListView) findViewById(R.id.lv_history);
        List<String> searchList = new ArrayList<>();
        if(stringSet != null) {
            for (String str : stringSet) {
                searchList.add(str);
            }
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, searchList);
        lv.setAdapter(adapter);

    }


}
