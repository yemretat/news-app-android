package edu.sabanciuniv.newsstarterexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.sabanciuniv.newsstarterexample.model.CommentItem;
import edu.sabanciuniv.newsstarterexample.model.NewsItem;

public class CommentsActivity extends AppCompatActivity  {
    NewsItem selectedNews;
    RecyclerView recyclerView;
    ProgressDialog prgdialog;
    List<CommentItem> data;
    CommentsAdapter adp;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        data = new ArrayList<>();
        selectedNews = (NewsItem) getIntent().getSerializableExtra("selectedNews");
        recyclerView = findViewById(R.id.recycom);
        adp=new CommentsAdapter(data,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adp);
        int selectedid = selectedNews.getId();
        setTitle("Comments");
        ActionBar currentActionBar = getSupportActionBar();
        currentActionBar.setHomeButtonEnabled(true);
        currentActionBar.setDisplayHomeAsUpEnabled(true);
        currentActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24px);
        // executlayacağım
        String s=String.valueOf(selectedid);
        CategorisedTask tsk=new CategorisedTask();

        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getcommentsbynewsid",s);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.commentsmenu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//1.13 edit control point arrow back
// 1.22
        if(item.getItemId()==android.R.id.home)
        {
            Intent i=new Intent(this, NewsDetailActivity.class);
            i.putExtra("selectedNews", selectedNews);

            startActivity(i);
        }
        else if (item.getItemId() == R.id.mn_plus) {
            Intent i = new Intent(this, CommentPost.class);
            i.putExtra("selectedNews", selectedNews);
            startActivity(i);
        }
        return true;
    }


     class CategorisedTask extends AsyncTask<String, Void, String> {
       @Override
        protected void onPreExecute() {
            prgdialog=new ProgressDialog(CommentsActivity.this);
            prgdialog.setTitle("Loading");
            prgdialog.setMessage("Please wait...");
            prgdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String name = strings[1];
            String urlStr = strings[0] + "/" + name;
            StringBuilder buffer = new StringBuilder();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                //  Log.e("DEV", e.getMessage());
                e.printStackTrace();
            }
            return buffer.toString();
        }

        protected void onPostExecute(String e) {
            data.clear();
            try {
                JSONObject obj = new JSONObject(e);
                if (obj.getInt("serviceMessageCode") == 1) {
                    JSONArray arr = obj.getJSONArray("items");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject current = (JSONObject) arr.get(i);
                        CommentItem item = new CommentItem(

                                current.getInt("id"),
                                current.getString("name"),
                                current.getString("text")


                        );

                        data.add(item);

                    }
                } else {

                }
                adp.notifyDataSetChanged();
                prgdialog.dismiss();

            } catch (JSONException ex) {
                ex.printStackTrace();
            }


        }
    }
}