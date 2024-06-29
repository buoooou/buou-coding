package com.github.buoooou.wxpay.common.util.http.jodd;

import jodd.http.HttpConnectionProvider;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.http.ProxyInfo;
import lombok.extern.slf4j.Slf4j;
import com.github.buoooou.wxpay.common.bean.result.WxMinishopImageUploadResult;
import com.github.buoooou.wxpay.common.enums.WxType;
import com.github.buoooou.wxpay.common.error.WxError;
import com.github.buoooou.wxpay.common.error.WxErrorException;
import com.github.buoooou.wxpay.common.util.http.MinishopUploadRequestExecutor;
import com.github.buoooou.wxpay.common.util.http.RequestHttp;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * .
 *
 * @author ecoolper
 * created on  2017/5/5
 */
@Slf4j
public class JoddHttpMinishopMediaUploadRequestExecutor extends MinishopUploadRequestExecutor<HttpConnectionProvider, ProxyInfo> {
    public JoddHttpMinishopMediaUploadRequestExecutor(RequestHttp requestHttp) {
        super(requestHttp);
    }

    @Override
    public WxMinishopImageUploadResult execute(String uri, File file, WxType wxType) throws WxErrorException, IOException {
        HttpRequest request = HttpRequest.post(uri);
        if (requestHttp.getRequestHttpProxy() != null) {
            requestHttp.getRequestHttpClient().useProxy(requestHttp.getRequestHttpProxy());
        }
        request.withConnectionProvider(requestHttp.getRequestHttpClient());
        request.form("media", file);
        HttpResponse response = request.send();
        response.charset(StandardCharsets.UTF_8.name());

        String responseContent = response.bodyText();
        WxError error = WxError.fromJson(responseContent, wxType);
        if (error.getErrorCode() != 0) {
            throw new WxErrorException(error);
        }
        log.info("responseContent: " + responseContent);

        return WxMinishopImageUploadResult.fromJson(responseContent);
  }
}
