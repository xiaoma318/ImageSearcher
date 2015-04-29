package com.chengma.search;

import com.loopj.android.image.SmartImageView;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by chengma on 2/5/15.
 */
public class ImageDisplayActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        
        ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");
        SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
        ivImage.setImageUrl(result.getFullUrl());
    }
}