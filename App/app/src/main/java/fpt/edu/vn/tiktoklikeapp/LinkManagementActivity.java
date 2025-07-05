package fpt.edu.vn.tiktoklikeapp;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class LinkManagementActivity extends AppCompatActivity implements LinkAdapter.OnLinkActionListener {
    private TextInputEditText etName, etUrl;
    private MaterialButton btnAdd;
    private RecyclerView rvLinks;
    private DatabaseHelper dbHelper;
    private VideoCacheManager cacheManager;
    private LinkAdapter adapter;
    private List<Link> links;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_management);

        etName = findViewById(R.id.etName);
        etUrl = findViewById(R.id.etUrl);
        btnAdd = findViewById(R.id.btnAdd);
        rvLinks = findViewById(R.id.rvLinks);
        dbHelper = new DatabaseHelper(this);
        cacheManager = new VideoCacheManager(this);
        links = new ArrayList<>();

        rvLinks.setLayoutManager(new LinearLayoutManager(this));

        new LoadLinksTask().execute();

        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String url = etUrl.getText().toString().trim();
            if (!name.isEmpty() && !url.isEmpty()) {
                String processedUrl = processYouTubeUrl(url);
                if (processedUrl != null) {
                    new AddLinkTask(name, processedUrl).execute();
                } else {
                    Toast.makeText(this, "Invalid YouTube URL (must be embed, Shorts, or youtu.be format)", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter name and URL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadLinks() {
        links.clear();
        Cursor cursor = dbHelper.getAllLinks();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String url = cursor.getString(cursor.getColumnIndexOrThrow("url"));
            links.add(new Link(id, name, url));
        }
        cursor.close();
        if (adapter == null) {
            adapter = new LinkAdapter(this, links, this);
            rvLinks.setAdapter(adapter);
        } else {
            adapter.updateLinks(links);
        }
    }

    private String processYouTubeUrl(String url) {
        String cleanUrl = url.split("\\?")[0];
        if (cleanUrl.matches("https?://www\\.youtube\\.com/embed/[\\w-]{11}")) {
            return cleanUrl;
        } else if (cleanUrl.matches("https?://(www\\.)?youtube\\.com/shorts/[\\w-]{11}")) {
            String videoId = cleanUrl.substring(cleanUrl.lastIndexOf("/") + 1);
            return "https://www.youtube.com/embed/" + videoId;
        } else if (cleanUrl.matches("https?://youtu\\.be/[\\w-]{11}")) {
            String videoId = cleanUrl.substring(cleanUrl.lastIndexOf("/") + 1);
            return "https://www.youtube.com/embed/" + videoId;
        }
        return null;
    }

    @Override
    public void onUpdate(Link link) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_link, null);
        TextInputEditText etDialogName = dialogView.findViewById(R.id.etDialogName);
        TextInputEditText etDialogUrl = dialogView.findViewById(R.id.etDialogUrl);

        etDialogName.setText(link.getName());
        etDialogUrl.setText(link.getUrl());

        new MaterialAlertDialogBuilder(this)
                .setTitle("Update Link")
                .setMessage("Enter new details for the link:")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    String newName = etDialogName.getText().toString().trim();
                    String newUrl = etDialogUrl.getText().toString().trim();
                    String processedNewUrl = processYouTubeUrl(newUrl);
                    if (!newName.isEmpty() && processedNewUrl != null) {
                        new UpdateLinkTask(link.getId(), newName, processedNewUrl).execute();
                    } else {
                        Toast.makeText(this, "Please enter valid name and URL", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDelete(int id) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Delete Link")
                .setMessage("Are you sure you want to delete this link?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    new DeleteLinkTask(id).execute();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private class LoadLinksTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            loadLinks();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {}
    }

    private class AddLinkTask extends AsyncTask<Void, Void, Boolean> {
        private final String name;
        private final String url;

        AddLinkTask(String name, String url) {
            this.name = name;
            this.url = url;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return dbHelper.addLink(name, url);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(LinkManagementActivity.this, "Link added", Toast.LENGTH_SHORT).show();
                etName.setText("");
                etUrl.setText("");
                cacheManager.clearCache();
                loadLinks();
            } else {
                Toast.makeText(LinkManagementActivity.this, "Failed to add link due to database error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UpdateLinkTask extends AsyncTask<Void, Void, Boolean> {
        private final int id;
        private final String name;
        private final String url;

        UpdateLinkTask(int id, String name, String url) {
            this.id = id;
            this.name = name;
            this.url = url;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return dbHelper.updateLink(id, name, url);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(LinkManagementActivity.this, "Link updated", Toast.LENGTH_SHORT).show();
                cacheManager.clearCache();
                loadLinks();
            } else {
                Toast.makeText(LinkManagementActivity.this, "Failed to update link", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DeleteLinkTask extends AsyncTask<Void, Void, Boolean> {
        private final int id;

        DeleteLinkTask(int id) {
            this.id = id;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return dbHelper.deleteLink(id);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(LinkManagementActivity.this, "Link deleted", Toast.LENGTH_SHORT).show();
                cacheManager.clearCache();
                loadLinks();
            } else {
                Toast.makeText(LinkManagementActivity.this, "Failed to delete link", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}