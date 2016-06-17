package com.greatmap.idefunction.network;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by police on 2016/6/15.
 */
public interface APIService {

    /**
     * 上传图片
     * @param fileDescription
     * @param requestBody
     * @return
     */
    @Multipart
    @POST(RequestUrl.upload)
    Observable<ResponseBody> uploadFile(@Part("fileName") String fileDescription,
                                        @Part("file\"; filename=\"image.png\"") RequestBody requestBody);
}
