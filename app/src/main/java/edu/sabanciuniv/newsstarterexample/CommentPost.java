package edu.sabanciuniv.newsstarterexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import edu.sabanciuniv.newsstarterexample.model.CommentItem;
import edu.sabanciuniv.newsstarterexample.model.NewsItem;

public class CommentPost extends AppCompatActivity {
    NewsItem selectedNews;
    ProgressDialog prgdialog;

    EditText txtView;
    EditText txtView2;

    Button button1;
    CommentItem selectedComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_post);
        txtView=findViewById(R.id.textView2);
        txtView2=findViewById(R.id.textView3);

        selectedNews=(NewsItem)getIntent().getSerializableExtra("selectedNews");
        ActionBar currentActionBar = getSupportActionBar();
        currentActionBar.setHomeButtonEnabled(true);
        currentActionBar.setDisplayHomeAsUpEnabled(true);
        currentActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24px);

        getSupportActionBar().setTitle("Post Comment");

    }
    public void taskCallClicked(View v){

        JsonTask tsk = new JsonTask();
        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/savecomment",
                txtView.getText().toString(),
                txtView2.getText().toString());



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.postcomment,menu);
        return true;

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            Intent i=new Intent(this, CommentsActivity.class);
            i.putExtra("selectedNews", selectedNews);
            //  i.putExtra(selectedNews.)
            startActivity(i);
        }
        return true;
    }
    class JsonTask extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
           /* prgdialog=new ProgressDialog(CommentPost.this);
            prgdialog.setTitle("Loading");
            prgdialog.setMessage("Please wait...");
            prgdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgdialog.show();*/
        }


        @Override
        protected String doInBackground(String... strings) {

            //yukarıda yazdığımız urlden name lastname'i çektik bunlarla json oluşturduk.
            StringBuilder strBuilder = new StringBuilder();
            String urlStr = strings[0];
            String name = strings[1];
            String comment = strings[2];

            JSONObject obj = new JSONObject();
            try {// jsonı ayarlıyor! output objectimi yapıyorum
                obj.put("news_id",selectedNews.getId());
                obj.put("name",name);
                obj.put("text",comment);
            } catch (JSONException e) {
                Log.e("DEV",e.getMessage());
            }


            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDoInput(true);// post için yazdım
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/json");
                conn.connect();
                // datayu byte'a çeviriyor
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(obj.toString());


                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){// is everything fine?
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line ="";
                    // burada serverın bana döndüğü şeyi alıyorum
                    while((line = reader.readLine())!=null){
                        strBuilder.append(line);
                    }

                }
                else
                {

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //burada post executa returnlüyorum
            return strBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject obj=new JSONObject(s);
                if(obj.getInt("serviceMessageCode")==1){
                    Intent i=new Intent(CommentPost.this,CommentsActivity.class);

                    i.putExtra("selectedNews",selectedNews);
                    //  i.putExtra(selectedNews.)
                    startActivity(i);
                }
                else{
                    AlertDialog.Builder builder=new AlertDialog.Builder(CommentPost.this);
                    builder.setTitle("Error");
                    builder.setMessage("Wrong Input");
                    builder.setNegativeButton("Error",null);
                    builder.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}


















