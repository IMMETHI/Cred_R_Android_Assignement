package com.example.ktbffh.cred_r_android_assignment;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    final String url = "https://www.snapdeal.com/search?keyword=%s&sort=rlvncy";
    final String className = "product-image lazy-load";
    final String defaultUrl = "https://www.snapdeal.com/search?keyword=mobile&sort=rlvncy";
    private Document htmlDocument;
    Elements dataRes;
    List<Item> dataList = new ArrayList<>();
    RecyclerView recyclerView;
    private ItemAdapter adapter;
    ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intailizeViews();
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute(defaultUrl);
    }

    private void intailizeViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new ItemAdapter(this, dataList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        loader = new ProgressDialog(this);
        loader.setMessage("Fetching Data....");
        loader.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loader.setCancelable(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                String url = CreateUrl(query);
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute(url);
            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public String CreateUrl(String query) {
        String val = String.format(url, query);
        return val;
    }

    private class JsoupAsyncTask extends AsyncTask<String, Void, List<Item>> {

        @Override
        protected void onPreExecute() {
            if (!isFinishing()) {
                loader.show();
            }
        }

        @Override
        protected List<Item> doInBackground(String... params) {
            List<Item> itemList = new ArrayList<>();
            try {
                htmlDocument = Jsoup.connect(params[0]).get();
                dataRes = htmlDocument.getElementsByClass(className);
                for (Element element : dataRes) {
                    Item item = new Item();
                    item.setImageUrl(element.attr("data-src"));
                    item.setTitle(element.attr("title"));
                    itemList.add(item);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return itemList;
        }

        @Override
        protected void onPostExecute(List<Item> itemList) {
            if (!isFinishing()) {
                if (loader.isShowing()) {
                    loader.cancel();
                }
            }
            if (itemList != null && itemList.size() != 0) {
                dataList.clear();
                dataList.addAll(itemList);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
