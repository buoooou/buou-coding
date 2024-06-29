package com.github.buoooou.wxpay.common.executor;

import com.github.buoooou.wxpay.common.bean.CommonUploadData;
import com.github.buoooou.wxpay.common.bean.CommonUploadParam;
import com.github.buoooou.wxpay.common.enums.WxType;
import com.github.buoooou.wxpay.common.error.WxError;
import com.github.buoooou.wxpay.common.error.WxErrorException;
import com.github.buoooou.wxpay.common.util.http.RequestHttp;
import com.github.buoooou.wxpay.common.util.http.okhttp.OkHttpProxyInfo;
import lombok.AllArgsConstructor;
import okhttp3.*;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

/**
 * OkHttp 通用文件上传器
 *
 * @author <a href="https://www.sacoc.cn">广州跨界</a>
 * created on  2024/01/11
 */
public class CommonUploadRequestExecutorOkHttpImpl extends CommonUploadRequestExecutor<OkHttpClient, OkHttpProxyInfo> {

    public CommonUploadRequestExecutorOkHttpImpl(RequestHttp<OkHttpClient, OkHttpProxyInfo> requestHttp) {
        super(requestHttp);
    }

    @Override
    public String execute(String uri, CommonUploadParam param, WxType wxType) throws WxErrorException, IOException {
        RequestBody requestBody = new CommonUpdateDataToRequestBodyAdapter(param.getData());
        RequestBody body = new MultipartBody.Builder()
                .setType(MediaType.get("multipart/form-data"))
                .addFormDataPart(param.getName(), param.getData().getFileName(), requestBody)
                .build();
        Request request = new Request.Builder().url(uri).post(body).build();

        try (Response response = requestHttp.getRequestHttpClient().newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            String responseContent = responseBody == null ? "" : responseBody.string();
            if (responseContent.isEmpty()) {
                throw new WxErrorException(String.format("上传失败，服务器响应空 url:%s param:%s", uri, param));
            }
            WxError error = WxError.fromJson(responseContent, wxType);
            if (error.getErrorCode() != 0) {
                throw new WxErrorException(error);
            }
            return responseContent;
        }
    }

    /**
     * 通用上传输入 到 OkHttp 请求提 适配器
     */
    @AllArgsConstructor
    public static class CommonUpdateDataToRequestBodyAdapter extends RequestBody {

        private static final MediaType CONTENT_TYPE = MediaType.get("application/octet-stream");

        private CommonUploadData data;

        @Override
        public long contentLength() {
            return data.getLength();
        }

        @Nullable
        @Override
        public MediaType contentType() {
            return CONTENT_TYPE;
        }

        @Override
        public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
            InputStream inputStream = data.getInputStream();
            int count;
            byte[] buffer = new byte[4096];
            while ((count = inputStream.read(buffer)) != -1) {
                bufferedSink.write(buffer, 0, count);
            }
            inputStream.close();
        }

        @Override
        public boolean isOneShot() {
            return true;
        }
    }
}
