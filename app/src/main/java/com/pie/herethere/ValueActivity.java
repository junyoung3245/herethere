package com.pie.herethere;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ValueActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView value_back, value_img;
    TextView value_toolbar;

    String contentId;

    boolean isBookMarkOk = false;
    ImageView bookMark;

    public void Declaration() {
        value_back = (ImageView) findViewById(R.id.value_back);
        value_back.setOnClickListener(this);

        value_img = (ImageView) findViewById(R.id.value_img);
        bookMark = (ImageView) findViewById(R.id.value_book);

        value_toolbar = (TextView) findViewById(R.id.value_toolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value);
        Declaration();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("jua.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        Intent intent = getIntent();

        value_toolbar.setText(intent.getStringExtra("title"));
        Glide
                .with(getApplicationContext())
                .load(intent.getStringExtra("img"))
                .into(value_img);

        contentId = intent.getStringExtra("id");

        try {
            FileInputStream fis = openFileInput("Book-" + contentId + ".txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

            if (br.readLine().equals("북마크")) {
                isBookMarkOk = true;
                Toast.makeText(getApplicationContext(), "북마크 사용", Toast.LENGTH_SHORT).show();
            }

            br.close();
            fis.close();
        } catch (Exception e) { }

        bookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBookMarkOk) {
                    // 북마크가 되어있을 시
                    File file = new File("Book-" + contentId + ".txt");
                    file.delete();

                    isBookMarkOk = false;
                    Toast.makeText(getApplicationContext(), "북마크 해제", Toast.LENGTH_SHORT).show();

                } else {
                    // 북마크가 되어있지않을 시
                    try {
                        FileOutputStream fo = openFileOutput("Book-" + contentId + ".txt", Context.MODE_PRIVATE);
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fo, "UTF-8"));

                        bw.write("북마크");
                        bw.close();
                        fo.flush();
                        fo.close();

                        isBookMarkOk = true;
                        Toast.makeText(getApplicationContext(), "북마크 사용", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) { }
                }
            }
        });
    }

    @Override
    protected void attachBaseContext (Context newBase){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.value_back) {
            finish();
        }
    }
}