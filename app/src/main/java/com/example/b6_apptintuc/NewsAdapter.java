package com.example.b6_apptintuc;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private Context context;
    private List<NewsItem> newsItems;
    private OnItemClickListener listener;

    // Setter for the click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Interface for the click listener
    public interface OnItemClickListener {
        void onItemClick(NewsItem item);
    }

    public NewsAdapter(Context context, List<NewsItem> newsItems) {
        this.context = context;
        this.newsItems = newsItems;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsItem item = newsItems.get(position);


        // Use Picasso or Glide to load image
        Picasso.get().load(item.getImageUrl()).into(holder.image);

        // Set the title
        holder.title.setText(item.getTitle());

        // Fetch the title asynchronously based on the URL
        fetchTitleFromUrl(item.getContentUrl(), new TitleCallback() {
            @Override
            public void onTitleFetched(String title) {
                // Update the content TextView with the fetched title
                holder.content.setText(title);
            }

            @Override
            public void onTitleFetchError() {
                // Handle the error case, for example, set a default title
                holder.content.setText("Error Fetching Title");
            }
        });

        // Set a click listener for the item
        holder.itemView.setOnClickListener(v -> {
            // Implement click listener, for example:
            Intent detailIntent = new Intent(context, DetailActivity.class);
            detailIntent.putExtra("URL", item.getContentUrl());
            context.startActivity(detailIntent);
        });
    }

    // This method simulates fetching the title from the server
    private void fetchTitleFromUrl(String url, TitleCallback callback) {
        // Use AsyncTask to perform the network request in the background
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... urls) {
                try {
                    return fetchTitleFromNetwork(urls[0]);
                } catch (IOException e) {
                    return null; // Handle the error
                }
            }

            @Override
            protected void onPostExecute(String title) {
                if (title != null) {
                    runOnUiThread(() -> callback.onTitleFetched(title));
                } else {
                    runOnUiThread(callback::onTitleFetchError);
                }
            }
        }.execute(url);
    }

    // The fetchTitleFromNetwork method using Jsoup
    private String fetchTitleFromNetwork(String url) throws IOException {
        return Jsoup.connect(url).get().title();
    }

    // Callback interface for title fetching
    private interface TitleCallback {
        void onTitleFetched(String title);

        void onTitleFetchError();
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView content; // Add this line

        public NewsViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content); // Add this line
        }
    }

    // Missing runOnUiThread method
    private void runOnUiThread(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }
}
