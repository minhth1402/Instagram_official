package com.minhthieu.instagramofficial;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import ir.shahabazimi.instagrampicker.InstagramPicker;

public class PostActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        StartTheGallery();

    }

    private void StartTheGallery() {
        // start the post library
        InstagramPicker in = new InstagramPicker(PostActivity.this);
        in.show('1','1', address ->  {
            // get the image Uri from the library
            Uri imageUri = Uri.parse(address);

            // put the image to container and be ready to post
            ImageView imageToPost = (ImageView) findViewById(R.id.image_to_post);
            imageToPost.setImageURI(imageUri);

        });
    }
}