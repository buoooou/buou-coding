package com.github.buoooou.wxpay.common.util.http;

import com.github.buoooou.wxpay.common.bean.result.WxMediaUploadResult;
import com.github.buoooou.wxpay.common.enums.WxType;
import com.github.buoooou.wxpay.common.error.WxErrorException;
import com.github.buoooou.wxpay.common.util.http.apache.ApacheMediaInputStreamUploadRequestExecutor;
import com.github.buoooou.wxpay.common.util.http.jodd.JoddHttpMediaInputStreamUploadRequestExecutor;
import com.github.buoooou.wxpay.common.util.http.okhttp.OkHttpMediaInputStreamUploadRequestExecutor;

import java.io.IOException;

/**
 * 上传媒体文件请求执行器.
 * 请求的参数是File, 返回的结果是String
 *
 * @author Daniel Qian
 */
public abstract class MediaInputStreamUploadRequestExecutor<H, P> implements RequestExecutor<WxMediaUploadResult, InputStreamData> {
    protected RequestHttp<H, P> requestHttp;

    public MediaInputStreamUploadRequestExecutor(RequestHttp requestHttp) {
        this.requestHttp = requestHttp;
    }

    @Override
    public void execute(String uri, InputStreamData data, ResponseHandler<WxMediaUploadResult> handler, WxType wxType) throws WxErrorException, IOException {
        handler.handle(this.execute(uri, data, wxType));
    }

    public static RequestExecutor<WxMediaUploadResult, InputStreamData> create(RequestHttp requestHttp) {
        switch (requestHttp.getRequestType()) {
            case APACHE_HTTP:
                return new ApacheMediaInputStreamUploadRequestExecutor(requestHttp);
            case JODD_HTTP:
                return new JoddHttpMediaInputStreamUploadRequestExecutor(requestHttp);
            case OK_HTTP:
                return new OkHttpMediaInputStreamUploadRequestExecutor(requestHttp);
            default:
        return null;
    }
  }

}
