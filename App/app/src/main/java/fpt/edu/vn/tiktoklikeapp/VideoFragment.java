package fpt.edu.vn.tiktoklikeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class VideoFragment extends Fragment {
    private static final String ARG_VIDEO_URL = "video_url";
    private String videoUrl;
    private WebView webView;
    private ProgressBar progressBar;
    private boolean isVisibleToUser;

    public VideoFragment() {}

    public static VideoFragment newInstance(String videoUrl) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VIDEO_URL, videoUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoUrl = getArguments().getString(ARG_VIDEO_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        webView = view.findViewById(R.id.webView);
        progressBar = view.findViewById(R.id.progressBar);

        // Cấu hình WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAllowFileAccess(false);
        webSettings.setAllowContentAccess(false);
        webSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 10; Pixel 4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Mobile Safari/537.36");
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                if (isVisibleToUser) {
                    webView.loadUrl("javascript:document.getElementsByTagName('video')[0].play()");
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                progressBar.setVisibility(View.GONE);
                String errorMessage = "Lỗi tải video: " + error.getDescription();
                if (error.getErrorCode() == WebViewClient.ERROR_AUTHENTICATION ||
                        error.getErrorCode() == WebViewClient.ERROR_HOST_LOOKUP ||
                        error.getErrorCode() == WebViewClient.ERROR_FILE_NOT_FOUND ||
                        error.getErrorCode() == WebViewClient.ERROR_UNKNOWN) {
                    errorMessage = "Video không thể phát (có thể bị giới hạn độ tuổi, khu vực, hoặc lỗi mạng)";
                }
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Tải URL nhúng
        webView.loadUrl(videoUrl);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (webView != null) {
            if (isVisibleToUser) {
                webView.loadUrl("javascript:document.getElementsByTagName('video')[0].play()");
            } else {
                webView.loadUrl("javascript:document.getElementsByTagName('video')[0].pause()");
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
            webView.loadUrl("javascript:document.getElementsByTagName('video')[0].pause()");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
            if (isVisibleToUser) {
                webView.loadUrl("javascript:document.getElementsByTagName('video')[0].play()");
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (webView != null) {
            webView.loadUrl("about:blank"); // Xóa nội dung
            webView.destroy();
        }
    }
}