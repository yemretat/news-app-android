package edu.sabanciuniv.newsstarterexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import edu.sabanciuniv.newsstarterexample.model.NewsItem;

public class NewsDetailActivity extends AppCompatActivity {

    NewsItem selectedNews;
    ImageView imgView;
    TextView txtView;
    TextView detailheader;
    TextView detailtarih;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        imgView=findViewById(R.id.imageView);
        txtView = findViewById(R.id.textView);
        detailheader=findViewById(R.id.detailheader);
        detailtarih=findViewById(R.id.detailtarih);
        selectedNews=(NewsItem)getIntent().getSerializableExtra("selectedNews");
        txtView.setText(selectedNews.getText());
        detailheader.setText(selectedNews.getTitle());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(selectedNews.getNewsDate());
        detailtarih.setText(strDate);
        getSupportActionBar().setTitle("News Detail");
        ActionBar currentActionBar = getSupportActionBar();
        currentActionBar.setHomeButtonEnabled(true);
        currentActionBar.setDisplayHomeAsUpEnabled(true);
        currentActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24px);
        ImageDownloadTask tsk = new ImageDownloadTask(imgView);
        tsk.execute(selectedNews);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailmenu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//1.13 edit control point arrow back
// 1.2
        selectedNews=(NewsItem)getIntent().getSerializableExtra("selectedNews");
        if(item.getItemId()==android.R.id.home)
        {
            Intent i=new Intent(this,MainActivity.class);
            startActivity(i);
        }
        else if(item.getItemId()==R.id.mn_edit)
        {
            Intent i=new Intent(this,CommentsActivity.class);
            i.putExtra("selectedNews",selectedNews);
            //  i.putExtra(selectedNews.)
            startActivity(i);
        }
        return true;
    }
}
