package com.avoma.rssfeed.main;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.muddzdev.styleabletoast.StyleableToast;
import com.avoma.rssfeed.BaseActivity;
import com.avoma.rssfeed.R;
import com.avoma.rssfeed.databinding.ActivityMainBinding;
import com.avoma.rssfeed.utils.Status;

import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends BaseActivity implements MainAdapter.onClickListener{
    private ProgressBar progressBar;
    private RecyclerView list;
    private MainAdapter mAdapter;
    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(0);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        mainViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(MainViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setupToolbar();
        attachViews();
        init();
        configRecyclerView();
    }

    private void setupToolbar(){
        Toolbar toolbar = binding.appBar.toolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
    }

    private void attachViews() {
        progressBar = binding.progress;
        list = binding.list;
    }

    private void init() {
        fetchData();

        mainViewModel.getLiveData().observe(this, resource -> {
            removeWait();
            if (resource.getStatus() == Status.ERROR) {
                StyleableToast.makeText(MainActivity.this, resource.getMessage(), Toast.LENGTH_LONG, R.style.warningToast).show();
                onBackPressed();
            } else if (resource.getStatus() == Status.AUTH_ERROR) {
                StyleableToast.makeText(MainActivity.this, resource.getMessage(), Toast.LENGTH_LONG, R.style.warningToast).show();
            } else {
                mAdapter.updateList(resource.getData());
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void configRecyclerView() {
        list.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MainAdapter(this);
        list.setAdapter(mAdapter);
    }

    private void fetchData() {
        showWait();
        mainViewModel.getData();
    }

    private void showWait() {
        progressBar.setVisibility(VISIBLE);
    }

    private void removeWait() {
        progressBar.setVisibility(GONE);
    }

    @Override
    public void onBookmarkClick(int id, Boolean isBookmarked) {
        mainViewModel.editData(isBookmarked, id);
    }
}