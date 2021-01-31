package edu.sabanciuniv.newsstarterexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

import edu.sabanciuniv.newsstarterexample.model.CommentItem;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
    List<CommentItem> commentsItems;
    Context context;

    public CommentsAdapter(List<CommentItem> commentsItems,Context context){
        this.commentsItems=commentsItems;
        this.context=context;

    }
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.new_comment_card,parent,false);
        return new CommentsViewHolder(v);

// this method will be called while each row displayed
    }
    public void onBindViewHolder(@NonNull CommentsAdapter.CommentsViewHolder holder, final int position) {
        holder.txtcomment.setText(commentsItems.get(position).getMessage());
        holder.txtname.setText(commentsItems.get(position).getName());


    }
    public int getItemCount() {
        return commentsItems.size();
    }
  //  public interface Commentlistener{
   // }
    class CommentsViewHolder  extends RecyclerView.ViewHolder {

        TextView txtname;
        TextView txtcomment;
        ConstraintLayout root;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            txtname=itemView.findViewById(R.id.isim);
            txtcomment=itemView.findViewById(R.id.comment);
            root =itemView.findViewById(R.id.container2);

        }

    }





}
