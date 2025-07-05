package fpt.edu.vn.tiktoklikeapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.List;

public class VideoPagerAdapter extends FragmentStateAdapter {
    private final List<String> videoUrls;

    public VideoPagerAdapter(FragmentActivity fragmentActivity, List<String> videoUrls) {
        super(fragmentActivity);
        this.videoUrls = videoUrls;
    }

    @Override
    public Fragment createFragment(int position) {
        int index = position % videoUrls.size();
        return VideoFragment.newInstance(videoUrls.get(index));
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}