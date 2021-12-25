package com.avoma.rssfeed.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;

import com.avoma.rssfeed.R;
import com.avoma.rssfeed.databinding.ActivityDetailBinding;
import com.avoma.rssfeed.databinding.ActivityMainBinding;
import com.avoma.rssfeed.main.MainViewModel;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(0);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        setupToolbar();
        attachViews();
        init();
    }

    private void setupToolbar(){
        Toolbar toolbar = binding.appBar.toolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getIntent().getStringExtra("title"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void attachViews() {
        webView = binding.webview;
    }

    private void init() {
        webView.loadData(getIntent().getStringExtra("data"), "text/html", "UTF-8");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}