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

import edu.sabanciuniv.newsstarterexample.model.CommentItem;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentsViewHolder> {
    List<CommentItem> commentItems;
    Context context;
    CommentItemClickListener listener;
    // context bizim bu activityi çağırıyor.
    public CommentAdapter(List<CommentItem> commentItems,Context context,CommentItemClickListener listener){
        this.commentItems=commentItems;
        this.context=context;
        this.listener=listener;
    }
    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(context).inflate(R.layout.new_comment_card,parent,false);
        return new CommentsViewHolder(v);

// this method will be called while each row displayed
    }
    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, final int position) {
        holder.txtName.setText(commentItems.get(position).getName());
        holder.txtComment.setText(commentItems.get(position).getMessage());
        holder.root.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                listener.CommentItemClicked(commentItems.get(position));
            }
        });
        //holdera datayı koyduğum yer!!! cashing mechanism
    }
    @Override
    public int getItemCount() {
        return commentItems.size();
    }
    public interface CommentItemClickListener{
        public void CommentItemClicked(CommentItem selectedNewsItem);
    }
    class CommentsViewHolder  extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtComment;
        ConstraintLayout root;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.isim);
            txtComment=itemView.findViewById(R.id.comment);
            root =itemView.findViewById(R.id.container2);

        }

    }



}
