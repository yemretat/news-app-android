package edu.sabanciuniv.newsstarterexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Element;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

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

import edu.sabanciuniv.newsstarterexample.model.NewsItem;

public class MainActivity extends AppCompatActivity implements NewsAdapter.NewsItemClickListener {
    Spinner typesofnewssp;
    ProgressDialog prgdialog;
    RecyclerView recyclerView;
    List<NewsItem> data;
    NewsAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new ArrayList<>();

        recyclerView = findViewById(R.id.recylerview);
        adp = new NewsAdapter(data, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adp);
        typesofnewssp = findViewById(R.id.spinner);
        setTitle("News");
        NewsTask tsk=new NewsTask();

        typesofnewssp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategorisedTask tst=new CategorisedTask();
                String selectCity = typesofnewssp.getSelectedItem().toString();
                if(selectCity.equals("ALL"))
                {
                    NewsTask tsk=new NewsTask();
                    tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getall" );
                }
                else {
                    if (selectCity.equals("SPOR"))
                        tst.execute("http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid", "5");
                    else if (selectCity.equals("ECONOMY"))
                        tst.execute("http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid", "4");
                    else if (selectCity.equals("POLITICAL"))
                        tst.execute("http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid", "6");

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void newItemClicked(NewsItem selectedNewsItem) {
        Intent i = new Intent(this, NewsDetailActivity.class);
        i.putExtra("selectedNews", selectedNewsItem);
        startActivity(i);
    }

    class CategorisedTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            prgdialog=new ProgressDialog(MainActivity.this);
            prgdialog.setTitle("Loading");
            prgdialog.setMessage("Please wait...");
            prgdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String name = strings[1];
            String urlStr =  strings[0] + "/" + name;
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
                        long date = current.getLong("date");
                        Date objDATE = new Date(date);
                        NewsItem item = new NewsItem(

                                current.getInt("id"),
                                current.getString("title"),
                                current.getString("text"),
                                current.getString("image"),
                                objDATE

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
    class NewsTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            prgdialog=new ProgressDialog(MainActivity.this);
            prgdialog.setTitle("Loading");
            prgdialog.setMessage("Please wait...");
            prgdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String urlStr = strings[0];
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
                        long date = current.getLong("date");
                        Date objDATE = new Date(date);
                        NewsItem item = new NewsItem(

                                current.getInt("id"),
                                current.getString("title"),
                                current.getString("text"),
                                current.getString("image"),
                                objDATE

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