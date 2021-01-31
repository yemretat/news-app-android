package edu.sabanciuniv.newsstarterexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import edu.sabanciuniv.newsstarterexample.model.NewsItem;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    List<NewsItem> newsItems;
    Context context;
    NewsItemClickListener listener;
    // context bizim bu activityi çağırıyor.
    public NewsAdapter(List<NewsItem> newsItems,Context context,NewsItemClickListener listener){
        this.newsItems=newsItems;
        this.context=context;
        this.listener=listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.new_card_layout,parent,false);
        return new NewsViewHolder(v);

// this method will be called while each row displayed
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, final int position) {
        holder.txtDate.setText(new SimpleDateFormat("dd/MM/yyy").format(newsItems.get(position).getNewsDate()));
        holder.txtTitle.setText(newsItems.get(position).getTitle());
        holder.root.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                listener.newItemClicked(newsItems.get(position));
            }
        });
    //holdera datayı koyduğum yer!!! cashing mechanism
        if(newsItems.get(position).getBitmap()==null)
        {
            new ImageDownloadTask(holder.imgNews).execute(newsItems.get(position));
        }
        else{
        holder.imgNews.setImageBitmap(newsItems.get(position).getBitmap());}
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }
    public interface NewsItemClickListener{
        public void newItemClicked(NewsItem selectedNewsItem);
    }
    class NewsViewHolder  extends RecyclerView.ViewHolder {
        ImageView imgNews;
        TextView txtTitle;
        TextView txtDate;
        ConstraintLayout root;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgNews=itemView.findViewById(R.id.imageView2);
            txtDate=itemView.findViewById(R.id.txtlistdate);
            txtTitle=itemView.findViewById(R.id.txtlisttitle);
            root =itemView.findViewById(R.id.container);

        }

    }

}
