package fpt.edu.vn.tiktoklikeapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnVideo = findViewById(R.id.btnVideo);
        Button btnManageLinks = findViewById(R.id.btnManageLinks);

        btnVideo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VideoActivity.class);
            startActivity(intent);
        });

        btnManageLinks.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LinkManagementActivity.class);
            startActivity(intent);
        });
    }
}