package fpt.edu.vn.tiktoklikeapp;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

public class VideoCacheManager {
    private static final String PREF_NAME = "VideoCache";
    private static final String KEY_VIDEO_IDS = "video_ids";
    private final SharedPreferences prefs;

    public VideoCacheManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void cacheVideoIds(List<String> videoIds) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_VIDEO_IDS, String.join(",", videoIds));
        editor.apply();
    }

    public List<String> getCachedVideoIds() {
        String idsString = prefs.getString(KEY_VIDEO_IDS, "");
        List<String> videoIds = new ArrayList<>();
        if (!idsString.isEmpty()) {
            for (String id : idsString.split(",")) {
                videoIds.add(id);
            }
        }
        return videoIds;
    }

    public void clearCache() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}