package com.github.buoooou.wxpay.common.util.http.okhttp;

import com.github.buoooou.wxpay.common.bean.result.WxMediaUploadResult;
import com.github.buoooou.wxpay.common.enums.WxType;
import com.github.buoooou.wxpay.common.error.WxError;
import com.github.buoooou.wxpay.common.error.WxErrorException;
import com.github.buoooou.wxpay.common.util.http.InputStreamData;
import com.github.buoooou.wxpay.common.util.http.MediaInputStreamUploadRequestExecutor;
import com.github.buoooou.wxpay.common.util.http.RequestHttp;
import okhttp3.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件输入流上传.
 *
 * @author meiqin.zhou91@gmail.com
 * created on  2022/02/15
 */
public class OkHttpMediaInputStreamUploadRequestExecutor extends MediaInputStreamUploadRequestExecutor<OkHttpClient, OkHttpProxyInfo> {
    public OkHttpMediaInputStreamUploadRequestExecutor(RequestHttp requestHttp) {
        super(requestHttp);
    }

    @Override
    public WxMediaUploadResult execute(String uri, InputStreamData data, WxType wxType) throws WxErrorException, IOException {

        RequestBody body = new MultipartBody.Builder()
                .setType(MediaType.parse("multipart/form-data"))
                .addFormDataPart("media", data.getFilename(), RequestBody.create(this.toByteArray(data.getInputStream()), MediaType.parse("application/octet-stream")))
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


    public byte[] toByteArray(InputStream input) throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        }
    }
}
