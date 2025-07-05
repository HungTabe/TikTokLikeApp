package fpt.edu.vn.tiktoklikeapp;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private DatabaseHelper dbHelper;
    private VideoCacheManager cacheManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No internet connection. Please check your network.", Toast.LENGTH_LONG).show();
            return;
        }

        viewPager = findViewById(R.id.viewPager);
        dbHelper = new DatabaseHelper(this);
        cacheManager = new VideoCacheManager(this);

        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                List<String> videoUrls = cacheManager.getCachedVideoIds();
                if (videoUrls.isEmpty()) {
                    videoUrls = new ArrayList<>();
                    Cursor cursor = null;
                    try {
                        cursor = dbHelper.getAllLinks();
                        while (cursor.moveToNext()) {
                            String url = cursor.getString(cursor.getColumnIndexOrThrow("url"));
                            videoUrls.add(url); // Lưu URL đầy đủ
                        }
                        cacheManager.cacheVideoIds(videoUrls);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }
                    }
                }
                return videoUrls;
            }

            @Override
            protected void onPostExecute(List<String> videoUrls) {
                if (videoUrls.isEmpty()) {
                    Toast.makeText(VideoActivity.this, "No videos available. Add links in Manage Links.", Toast.LENGTH_LONG).show();
                } else {
                    VideoPagerAdapter adapter = new VideoPagerAdapter(VideoActivity.this, videoUrls);
                    viewPager.setAdapter(adapter);
                    viewPager.setCurrentItem(videoUrls.size() * 1000, false);
                    viewPager.setOffscreenPageLimit(1);
                }
            }
        }.execute();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}