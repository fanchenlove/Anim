package com.fanchen.anim.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanchen.anim.R;
import com.fanchen.anim.base.BaseActivity;
import com.fanchen.anim.util.DisplayUtil;
import com.fanchen.anim.util.SystemUtil;
import com.fanchen.anim.view.ObservableWebView;
import com.fanchen.anim.view.call.ObservableScrollViewCallbacks;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * webview Activity
 * Created by fanchen on 2017/3/20.
 */
public class AnimWebActivity extends BaseActivity implements View.OnClickListener, DownloadListener, ObservableScrollViewCallbacks {

    public static final String WEB_URL = "url";

    private ObservableWebView webview;
    private ProgressBar load_pro;
    private FloatingActionButton mShareImageView;
    private Toolbar mToolbarView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private int count = 0;
    private String mImageUrl;
    private boolean mFabIsShown;

    @Override
    protected int getLayout() {
        return R.layout.activity_web;
    }

    @Override
    protected void findView(View view) {
        super.findView(view);
        webview = (ObservableWebView) findViewById(R.id.wb_web_webview);
        load_pro = (ProgressBar) findViewById(R.id.web_load_pro);
        mShareImageView = (FloatingActionButton) findViewById(R.id.multiple_actions);
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected void initViewData(Bundle savedState, LayoutInflater inflater) {
        super.initViewData(savedState, inflater);
        setSupportActionBar(mToolbarView);
        ViewCompat.setElevation(mToolbarView, DisplayUtil.dip2px(appliction, 4));
        // 设置WebView属性，能够执行Javascript脚本
        WebSettings settings = webview.getSettings();
        if(settings != null){
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);// 开启DOM
            settings.setAllowFileAccess(true);// 设置支持文件流
            settings.setUseWideViewPort(true);// 调整到适合webview大小
            settings.setLoadWithOverviewMode(true);// 调整到适合webview大小
            settings.setBlockNetworkImage(true);// 提高网页加载速度，暂时阻塞图片加载，然后网页加载好了，在进行加载图片
            settings.setAppCacheEnabled(true);// 开启缓存机制
        }
        webview.setWebViewClient(webViewClient);
        load_pro.setVisibility(View.VISIBLE);
        // 显示加载进度条
        webview.setWebChromeClient(chromeClient);
        webview.setDownloadListener(this);
        webview.setVisibility(View.VISIBLE);
        String url = getIntent().getStringExtra(WEB_URL);
//        ShareResource extra = getIntent().getParcelableExtra(ISEXTRACT);
        if (url != null)
            webview.loadUrl(url);
//        else if (extra != null) {
//            HttpListener<?> listener = new ParserResponseListener(this,
//                    NET_ONE, IParserManager.RESOURCE_PARSER, extra.source,IResourceParesr.EXTRACTCONTENT);
//            OkHttpUtil.getInstance().get(extra.url, listener);
//        }
        registerForContextMenu(webview);
    }

    @Override
    protected void setListener() {
        super.setListener();
        webview.setScrollViewCallbacks(this);
        mShareImageView.setOnClickListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        try {
            super.onCreateContextMenu(menu, v, menuInfo);
            WebView.HitTestResult result = webview.getHitTestResult();
            if (result.getType() == WebView.HitTestResult.SRC_ANCHOR_TYPE) {
                mImageUrl = result.getExtra();
                menu.setHeaderView(new ContextMenuTitleView(this, mImageUrl));
                menu.add(Menu.NONE, 2, 0, "使用第三方程序打开").setOnMenuItemClickListener(menuItemHandler);
                menu.add(Menu.NONE, 0, 1, "复制网址").setOnMenuItemClickListener(menuItemHandler);
            } else if (result.getType() == WebView.HitTestResult.IMAGE_TYPE || result.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                mImageUrl = result.getExtra();
                menu.setHeaderView(new ContextMenuTitleView(this, mImageUrl));
                menu.add(Menu.NONE, 0, 0, "复制网址").setOnMenuItemClickListener(menuItemHandler);
                menu.add(Menu.NONE, 1, 1, "保存图片").setOnMenuItemClickListener(menuItemHandler);
                menu.add(Menu.NONE, 3, 2, "使用Baidu搜索").setOnMenuItemClickListener(menuItemHandler);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            showSnackbar("出现未知错误");
        }
    }

    @Override
    public void onBackPressed() {
        if (count == 0) {
            finish();
            super.onBackPressed();
            return;
        }
        count--;
        webview.goBack(); // goBack()表示返回WebView的上一页面
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webview != null) {
            webview.removeAllViews();
            webview.destroy();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

    }

    private void showFab() {
        if (mFabIsShown) {
            return;
        }
        ViewPropertyAnimator.animate(mShareImageView).cancel();
        ViewPropertyAnimator.animate(mShareImageView).translationY(0).setDuration(500).start();
        mFabIsShown = true;
    }

    private void hideFab() {
        if (!mFabIsShown) {
            return;
        }
        ViewPropertyAnimator.animate(mShareImageView).cancel();
        int measuredHeight = mShareImageView.getHeight() + 500;
        ViewPropertyAnimator.animate(mShareImageView).translationY(measuredHeight).setDuration(500).start();
        mFabIsShown = false;
    }

    private void startDownload() throws NullPointerException {
//        String fileName = FileUtil.getImageFile(mImageUrl);
//        File imageDir = new File(mSavePath);
//        if (!imageDir.exists())
//            imageDir.mkdirs();
//        final File destinationFile = new File(imageDir, fileName);
//        if (destinationFile.exists()) {
//            showMaterialDialog("文件已存在,是否覆盖文件?", new OnBtnClickL() {
//
//                @Override
//                public void onBtnClick(BaseAlertDialog<?> dialog, int btn) {
//                    dialog.dismiss();
//                    if (btn != OnBtnClickL.RIGHT)return;
//                    mImageLoader.loadImage(mImageUrl, imageLoadingListener);
//                }
//            });
//        } else {
//            mImageLoader.loadImage(mImageUrl, imageLoadingListener);
//        }
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.UP) {
            hideFab();
        } else if (scrollState == ScrollState.DOWN) {
            showFab();
        }
    }


    /**
     * // 设置web页面 // 如果页面中链接，如果希望点击链接继续在当前browser中响应， //
     * 而不是新开Android的系统browser中响应该链接，必须覆盖 webview的WebViewClient对象。
     *
     * @author fanchen
     */
    private WebViewClient webViewClient = new WebViewClient() {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.indexOf("taobao://") != -1) {
                showSnackbar("正在打开淘宝，请确定你的机器上有安装淘宝");
                SystemUtil.startThreeApp(AnimWebActivity.this, url);
                return true;
            } else if (url.indexOf("baiduyun://") != -1) {
                showSnackbar("正在打开百度云，请确定你的机器上有安装了百度云");
                SystemUtil.startThreeApp(AnimWebActivity.this, url);
                return true;
            } else if (url.contains("http")) {
                count++;
                view.loadUrl(url);
                return true;
            }
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (isFinishing()) return;
            try {
                view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        // 设置程序的标题为网页的标题
        @Override
        public void onPageFinished(WebView view, String url) {
            if (isFinishing()) return;
            view.setLayerType(View.LAYER_TYPE_NONE, null);
//            mTabBarManage.setContentTextViewText(view.getTitle());
            view.getSettings().setBlockNetworkImage(false);
        }

        // 当load有ssl层的https页面时，如果这个网站的安全证书在Android无法得到认证，
        // WebView就会变成一个空白页，而并不会像PC浏览器中那样跳出一个风险提示框
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,SslError error) {
            // 忽略证书的错误继续Load页面内容
            handler.proceed();
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
            view.requestFocus();
            view.requestFocusFromTouch();
        }
    };

    private WebChromeClient chromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                load_pro.setVisibility(View.GONE);
            } else {
                if (load_pro.getVisibility() == View.GONE)
                    load_pro.setVisibility(View.VISIBLE);
                load_pro.setProgress(newProgress);
            }
        }

    };

    private MenuItem.OnMenuItemClickListener menuItemHandler = new MenuItem.OnMenuItemClickListener() {
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case 0:
                    SystemUtil.putText2Clipboard(AnimWebActivity.this, mImageUrl);
                    showSnackbar("复制成功");
                    break;
                case 1:
                    try {
                        startDownload();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        showSnackbar("下载出现错误");
                    }
                    break;
                case 2:
                    SystemUtil.startThreeApp(AnimWebActivity.this, mImageUrl);
                    break;
                case 3:
                    webview.loadUrl("https://www.google.com/searchbyimage?image_url=" + mImageUrl);
                    break;
            }
            return true;
        }
    };


    public static class ContextMenuTitleView extends ScrollView {
        private static final int MAX_HEIGHT_DP = 70;
        private static final int PADDING_DP = 16;
        private int makeSpec;

        public ContextMenuTitleView(Context context, String title) {
            super(context);
            int padding = DisplayUtil.dip2px(context, PADDING_DP);
            makeSpec = DisplayUtil.dip2px(context, MAX_HEIGHT_DP);
            setPadding(padding, padding, padding, padding);
            TextView titleView = new TextView(context);
            titleView.setMaxLines(2);
            titleView.setText(title);
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            titleView.setTextColor(Color.BLACK);
            addView(titleView);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            try {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(makeSpec,MeasureSpec.AT_MOST);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
