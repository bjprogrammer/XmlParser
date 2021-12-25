package com.avoma.rssfeed.network;


import com.avoma.rssfeed.BaseApplication;
import com.avoma.rssfeed.model.Feed;
import com.avoma.rssfeed.utils.Constants;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;

import com.avoma.rssfeed.BuildConfig;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

public class RetrofitInterfaces {
    public static Retrofit newInstance() {
        Cache cache = new Cache(BaseApplication.getContext().getCacheDir(), Constants.CACHE_SIZE);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request original = chain.request();
                Request.Builder builder = original.newBuilder()
                        .removeHeader("Pragma");

                Request request = builder.build();
                Response response;

                try {
                    response = chain.proceed(request);
                } catch (Exception e) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(Constants.CACHE_MAX_STALE, TimeUnit.DAYS)
                            .build();

                    request = original.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", cacheControl.toString())
                            .build();

                    response = chain.proceed(request);
                }
                return response;
        }).addNetworkInterceptor(chain -> {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .removeHeader("Pragma")
                        .build();

                Response response = chain.proceed(request);

                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(Constants.CACHE_MAX_AGE, TimeUnit.MILLISECONDS)
                        .build();

                return response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", cacheControl.toString())
                        .build();
        }).cache(cache)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        .build();

        OkHttpClient client = RetrofitInterfaces.trustAllSslClient(okHttpClient);       //Trusting SSL certificate

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASEURL)
                .client(client)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
    }

    public interface AdminService {
        @GET("/feed/the-economist")
        Observable<Feed> getEconomistData();

        @GET("/feed/backchannel")
        Observable<Feed> getBackChannelData();

        @GET("/feed/matter")
        Observable<Feed> getMatterData();
    }


    private static final TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        }
    };

    private static final SSLContext trustAllSslContext;

    static {
        try {
            trustAllSslContext = SSLContext.getInstance("SSL");
            trustAllSslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    private static final SSLSocketFactory trustAllSslSocketFactory = trustAllSslContext.getSocketFactory();

    private static OkHttpClient trustAllSslClient(OkHttpClient client) {
        OkHttpClient.Builder builder = client.newBuilder();
        builder.sslSocketFactory(trustAllSslSocketFactory, (X509TrustManager) trustAllCerts[0]);
        builder.hostnameVerifier((hostname, session) -> true);
        return builder.build();
    }
}
