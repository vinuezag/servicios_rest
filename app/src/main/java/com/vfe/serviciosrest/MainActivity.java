package com.vfe.serviciosrest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnPost, btnPut, btnAll, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPost = findViewById(R.id.btnPost);
        btnPut = findViewById(R.id.btnPut);
        btnAll = findViewById(R.id.btnAll);
        btnDelete = findViewById(R.id.btnDelete);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnPost) {
                    startActivity(new Intent(MainActivity.this, post.class));
                } else if (v.getId() == R.id.btnPut) {
                    startActivity(new Intent(MainActivity.this, put.class));
                } else if (v.getId() == R.id.btnAll) {
                    startActivity(new Intent(MainActivity.this, all.class));
                } else if (v.getId() == R.id.btnDelete) {
                    startActivity(new Intent(MainActivity.this, delete.class));
                }
            }

        };

        btnPost.setOnClickListener(listener);
        btnPut.setOnClickListener(listener);
        btnAll.setOnClickListener(listener);
        btnDelete.setOnClickListener(listener);

    }

}