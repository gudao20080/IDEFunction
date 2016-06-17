package com.greatmap.idefunction.network;

import com.greatmap.idefunction.util.T;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * 数据请求
 * Created by police on 2016/6/14.
 */
public class DataAccessUtil {

    private static APIService mAPIService;

    static {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(RequestUrl.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mAPIService = retrofit.create(APIService.class);
    }

    public static Observable<ResponseBody> upload(File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        if (file != null && file.exists()) {
            return mAPIService.uploadFile(file.getName(), requestBody);

        }else {
            T.show("文件不存在");
            return null;
        }
    }
}
