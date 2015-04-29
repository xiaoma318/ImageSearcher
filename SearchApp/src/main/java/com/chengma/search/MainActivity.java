package com.chengma.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chengma on 2/5/15.
 */

public class MainActivity extends ActionBarActivity {


    private GridView gvResult;
    private ImageView ivWelcome ;
    private final String URL_PROXY = "https://ajax.googleapis.com/ajax/services/search/images?";
    private ArrayList<ImageResult> imageList;
    private ImageResultArrayAdapter imageAdapter;
    private static String size, color, type, filetype;
    private static final String LOG_TAG = "GoogleImageSearch";
    private SharedPreferences sharedPreferences;
    private int currentCount;
    private static String query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("search_history.dat", Context.MODE_PRIVATE); //for persistence of search history
        currentCount = 0;
        gvResult = (GridView) findViewById(R.id.resultGird);
        ivWelcome = (ImageView) findViewById(R.id.iv_welcome);
        imageList = new ArrayList<ImageResult>();
        imageAdapter = new ImageResultArrayAdapter(this, imageList);
        gvResult.setAdapter(imageAdapter);
        gvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
                ImageResult imageResult = imageList.get(arg2);
                i.putExtra("result", imageResult);
                startActivity(i);
            }

        });
        gvResult.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore() {
                fetch(query);
            }

        });
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    //get setting information from settingActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            size = extras.getString("size");
            color = extras.getString("color");
            type = extras.getString("type");
            filetype = extras.getString("filetype");
        }
    }

    //get optional params based on setting information
    private String getOptionalParams() {
        StringBuilder params = new StringBuilder();

        if (size != null && !size.equals("all"))
            params.append("&imgsz=" + size);
        if (color != null && !color.equals("all"))
            params.append("&imgcolor=" + color);
        if (type != null && !type.equals("all"))
            params.append("&imgtype=" + type);
        if (filetype != null && !filetype.equals("all"))
            params.append("&as_filetype=" + filetype);

        return params.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_search:
                if(ivWelcome.getVisibility() == View.VISIBLE)
                    ivWelcome.setVisibility(View.GONE);
                break;
            case R.id.action_settings:
                Intent i1 = new Intent(this, SettingActivity.class);
                startActivityForResult(i1, 1);
                break;
            case R.id.action_history:
                Intent i2 = new Intent(this, HistoryActivity.class);
                startActivity(i2);
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    private void handleIntent(Intent intent) {
        //handle the intent triggered by search area in actionbar
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            query = intent.getStringExtra(SearchManager.QUERY);
            persistSearchItem(query);
            Toast.makeText(getBaseContext(), "Search for " + query, Toast.LENGTH_LONG).show();
            imageList.clear();
            fetch(query);
        }else{
            ivWelcome.setVisibility(View.VISIBLE);
        }
    }

    private void fetch(String query) {
        if (query == null || query.isEmpty()) return;
        AsyncHttpClient client = new AsyncHttpClient();
        String fetchURL = URL_PROXY + "rsz=8&start=" + currentCount + getOptionalParams() + "&v=1.0&q=" + Uri.encode(query);

        client.get(fetchURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (statusCode == 200) {
                        currentCount += 8;
                        JSONArray imageResult = response.getJSONObject("responseData").getJSONArray("results");
                        imageAdapter.addAll(ImageResult.fromJSONArray(imageResult));
                    } else {
                        Toast.makeText(MainActivity.this, "Error: " + statusCode, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //write search query to sharedPreferences object
    private void persistSearchItem(String str) {
        Set<String> set = sharedPreferences.getStringSet("search", new HashSet<String>());
        set.add(str);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("search", set);
        editor.commit();
    }
}
