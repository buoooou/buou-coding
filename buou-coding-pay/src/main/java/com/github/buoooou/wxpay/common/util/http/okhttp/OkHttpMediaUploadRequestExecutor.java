package com.github.buoooou.wxpay.common.util.http.okhttp;

import com.github.buoooou.wxpay.common.bean.result.WxMediaUploadResult;
import com.github.buoooou.wxpay.common.enums.WxType;
import com.github.buoooou.wxpay.common.error.WxError;
import com.github.buoooou.wxpay.common.error.WxErrorException;
import com.github.buoooou.wxpay.common.util.http.MediaUploadRequestExecutor;
import com.github.buoooou.wxpay.common.util.http.RequestHttp;
import okhttp3.*;

import java.io.File;
import java.io.IOException;

/**
 * .
 *
 * @author ecoolper
 * created on  2017/5/5
 */
public class OkHttpMediaUploadRequestExecutor extends MediaUploadRequestExecutor<OkHttpClient, OkHttpProxyInfo> {
    public OkHttpMediaUploadRequestExecutor(RequestHttp requestHttp) {
        super(requestHttp);
    }

    @Override
    public WxMediaUploadResult execute(String uri, File file, WxType wxType) throws WxErrorException, IOException {

        RequestBody body = new MultipartBody.Builder()
                .setType(MediaType.parse("multipart/form-data"))
                .addFormDataPart("media",
                        file.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();
        Request request = new Request.Builder().url(uri).post(body).build();

        Response response = requestHttp.getRequestHttpClient().newCall(request).execute();
        String responseContent = response.body().string();
        WxError error = WxError.fromJson(responseContent, wxType);
        if (error.getErrorCode() != 0) {
            throw new WxErrorException(error);
        }
        return WxMediaUploadResult.fromJson(responseContent);
    }

}
