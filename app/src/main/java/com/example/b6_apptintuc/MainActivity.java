package com.example.b6_apptintuc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NewsAdapter.OnItemClickListener {

    private static final int ADD_NEWS_REQUEST_CODE = 100;
    private FloatingActionButton fabAdd;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<NewsItem> newsItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fabAdd = findViewById(R.id.fab_add);
        recyclerView = findViewById(R.id.news_recycler_view);
        newsItemList = new ArrayList<>();

        // Add an existing news item
        NewsItem defaultNewsItem = new NewsItem(
                "https://file1.hutech.edu.vn/file/editor/homepage1/171616-z4986109389253_fbe931d9cb786581199f7463351d7a50jpg.jpg",
                "Sự kiện ",
                "https://www.hutech.edu.vn/homepage/tin-tuc/tin-hutech/14614461-giang-vien-sinh-vien-khoa-kien-truc-my-thuat-tham-du-hoi-thao-su-dung-vat-lieu-noi-that-ben-vung-va-");

        // Add the item to your list
        newsItemList.add(defaultNewsItem);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(this, newsItemList);
        recyclerView.setAdapter(adapter);

        // Set OnClickListener for FloatingActionButton
        fabAdd.setOnClickListener(view -> {
            Intent addNewsIntent = new Intent(MainActivity.this, AddNewsItemActivity.class);
            startActivityForResult(addNewsIntent, ADD_NEWS_REQUEST_CODE); // Sử dụng hằng số đã định nghĩa ở trên
        });

        // Set the click listener for the adapter
        adapter.setOnItemClickListener(this);
    }

    // Handle the result from AddNewsItemActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NEWS_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Retrieve the NewsItem from the intent
            NewsItem newNewsItem = (NewsItem) data.getSerializableExtra("NEW_NEWS_ITEM");
            // Add new item to your list and notify the adapter
            newsItemList.add(newNewsItem);
            adapter.notifyItemInserted(newsItemList.size() - 1);
        }
    }

    @Override
    public void onItemClick(NewsItem item) {
        // Handle item click, for example:
        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
        detailIntent.putExtra("URL", item.getContentUrl());
        startActivity(detailIntent);
    }
}
