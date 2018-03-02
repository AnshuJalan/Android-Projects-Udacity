package com.example.anshujalan.newsnow;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>>{

    public static String requestUrl = "";
    NewsAdapter adapter = null;
    boolean loaderCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        findViewById(R.id.progress_bar).setVisibility(View.GONE);
    }

    public void SearchForNews(View view)
    {
        if(adapter != null)
        {
            adapter.clear();
        }

        EditText searchText = (EditText) findViewById(R.id.search_text);
        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);

        requestUrl = "http://content.guardianapis.com/search?q=" + searchText.getText().toString() + "&use-date=last-modified&order-by=newest" +"&api-key=test";
        System.out.println(requestUrl);

        if(loaderCalled) {
            getLoaderManager().restartLoader(0, null, this);
        }
        else
        {
            getLoaderManager().initLoader(0, null, this);
        }
    }

    private void updateAdapter(ArrayList<News> new_list)
    {
        ListView newsView = (ListView) findViewById(R.id.list);

        adapter = new NewsAdapter(this, new_list);;
        newsView.setAdapter(adapter);

        newsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openUrl(adapter.getItem(i).getUrl());
            }
        });
    }

    private void openUrl(String url)
    {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int i, Bundle bundle)
    {
        loaderCalled = true;
        return new NewsAsyncLoader(NewsActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> news)
    {
        findViewById(R.id.progress_bar).setVisibility(View.GONE);

        if(news != null)
        {
            updateAdapter(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {

    }

    private static class NewsAsyncLoader extends AsyncTaskLoader<ArrayList<News>>
    {

        public NewsAsyncLoader(Context context) {
            super(context);
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public ArrayList<News> loadInBackground() {
            return QueryUtils.extractNews(requestUrl);
        }
    }
}
