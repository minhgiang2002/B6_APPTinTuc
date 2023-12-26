package com.example.b6_apptintuc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.IOException;

public class AddNewsItemActivity extends AppCompatActivity {

    private EditText titleEditText, imageUrlEditText, contentUrlEditText;
    private Button addButton, chooseImageButton;
    private ImageView imageView;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        titleEditText = findViewById(R.id.title_input);
        imageUrlEditText = findViewById(R.id.image_url_input);
        contentUrlEditText = findViewById(R.id.content_url_input);
        addButton = findViewById(R.id.add_news_button);
        chooseImageButton = findViewById(R.id.choose_image_button);
        imageView = findViewById(R.id.image_view);

        chooseImageButton.setOnClickListener(v -> {
            if (imageUrlEditText.getText().toString().trim().isEmpty()) {
                // If the image URL is empty, open the Gallery to choose an image
                openGallery();
            } else {
                // If there is an image URL, load the image from URL
                loadImageFromUrl();
            }
        });

        addButton.setOnClickListener(view -> {
            // Get information from the user
            String title = titleEditText.getText().toString().trim();
            String imageUrl = imageUrlEditText.getText().toString().trim();
            String contentUrl = contentUrlEditText.getText().toString().trim();

            Bitmap selectedImageBitmap = getBitmapFromImageView(imageView);

            // Check if the user has provided an image URL or selected an image from the gallery
            if (imageUrl.isEmpty() && selectedImageBitmap == null) {
                Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
                return;
            }

            NewsItem newsItem = new NewsItem(imageUrl, title, contentUrl);

            // Return the result to MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("NEW_NEWS_ITEM", newsItem);
            setResult(RESULT_OK, resultIntent);
            finish(); // Close the activity
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    private void loadImageFromUrl() {
        // Load the image from the provided URL using Picasso
        Picasso.get().load(imageUrlEditText.getText().toString().trim()).into(imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();

                try {
                    Bitmap selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    // Set the selected image to the ImageView
                    imageView.setImageBitmap(selectedImageBitmap);
                    // Clear the image URL when a new image is selected from the gallery
                    imageUrlEditText.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Image selection canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Helper method to get Bitmap from ImageView
    private Bitmap getBitmapFromImageView(ImageView imageView) {
        android.graphics.drawable.Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            return null;
        }
    }
}
