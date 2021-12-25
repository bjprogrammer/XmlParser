package com.avoma.rssfeed;

import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.avoma.rssfeed.utils.ConnectivityReceiver;
import com.avoma.rssfeed.utils.CustomSnackbar;

import org.greenrobot.eventbus.EventBus;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class BaseActivity extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener {
    private ConnectivityReceiver receiver;
    public Boolean isConnected = true;
    private CustomSnackbar customSnackbar ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new ConnectivityReceiver();
        customSnackbar = CustomSnackbar.make(findViewById(android.R.id.content), CustomSnackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setConnectivityListener(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (receiver != null)
            unregisterReceiver(receiver);
    }

    private void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


    @Override
    public void onNetworkConnectionChanged(Boolean isConnected) {
        if (!this.isConnected && isConnected) {
            customSnackbar.showNoConnectionSnackBar("Back Online", isConnected);
        }else if (!isConnected){
            customSnackbar.showNoConnectionSnackBar("No Internet Connection", isConnected);
        }

        if(this.isConnected != isConnected) {
            EventBus.getDefault().post(isConnected);
            this.isConnected = isConnected;
        }
    }
}
