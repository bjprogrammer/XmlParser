package com.avoma.rssfeed.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.avoma.rssfeed.R;
import com.avoma.rssfeed.detail.DetailActivity;
import com.avoma.rssfeed.model.Item;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    public List<Item> feedList = new ArrayList<>();
    private final Context context;

    interface onClickListener{
        void onBookmarkClick(int id, Boolean isBookmarked);
    }

    public MainAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case ITEM:
                viewHolder = new PostViewHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false));
                break;

            case LOADING:
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                PostViewHolder viewHolder = (MainAdapter.PostViewHolder) holder;
                Item result = feedList.get(position);
                viewHolder.bind(result);
                break;

            case LOADING:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (feedList.get(position) == null) ? LOADING : ITEM;
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public void updateList(List<Item> list) {
        this.feedList = list;
    }

    public void clear() {
        if (feedList != null) {
            feedList.clear();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView date, name, title, category;
        private final AppCompatImageView share, bookmark;

        private View itemView;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.name);
            title = itemView.findViewById(R.id.headline);
            category = itemView.findViewById(R.id.tags);
            share = itemView.findViewById(R.id.share);
            bookmark = itemView.findViewById(R.id.bookmark);
        }

        @SuppressLint("DefaultLocale")
        private void bind( Item item) {
            String [] array = item.getPubDate().split(" ");

            date.setText(String.format("%s %s, %s", array[1], array[2], array[3]));
            name.setText(item.getCreator());
            title.setText(item.getTitle());
            category.setText(Html.fromHtml("<b>Tags: </b>"+ item.getCategory()));

            share.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_feed));
                intent.putExtra(Intent.EXTRA_TEXT, item.getGuid());
                context.startActivity(Intent.createChooser(intent, context.getString(R.string.rss_feed)));
            });

            if(item.getBookmarked()){
                bookmark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark_filled));
            }else {
                bookmark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark));
            }

            bookmark.setOnClickListener(v -> {
                if(!item.getBookmarked()){
                    bookmark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark_filled));
                    item.setBookmarked(true);
                }else {
                    bookmark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark));
                    item.setBookmarked(false);
                }

                if(context instanceof MainActivity){
                    ((MainActivity) context).onBookmarkClick(item.getId(), item.getBookmarked());
                }
            });

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("data", item.getEncoded());
                context.startActivity(intent);
            });
        }
    }
}
