package com.example.b6_apptintuc;
import android.graphics.Bitmap;

import java.io.Serializable;
public class NewsItem implements Serializable {
    private String imageUrl;

    private String title;
    private String contentUrl;

    public NewsItem(String imageUrl,  String title, String contentUrl) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.contentUrl = contentUrl;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

}