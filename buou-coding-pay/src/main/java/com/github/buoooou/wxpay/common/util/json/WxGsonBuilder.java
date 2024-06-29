package com.github.buoooou.wxpay.common.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.github.buoooou.wxpay.common.bean.WxAccessToken;
import com.github.buoooou.wxpay.common.bean.WxNetCheckResult;
import com.github.buoooou.wxpay.common.bean.menu.WxMenu;
import com.github.buoooou.wxpay.common.bean.result.WxMediaUploadResult;
import com.github.buoooou.wxpay.common.error.WxError;

import java.util.Objects;

/**
 * .
 *
 * @author chanjarster
 */
public class WxGsonBuilder {
    private static final GsonBuilder INSTANCE = new GsonBuilder();
    private static volatile Gson GSON_INSTANCE;

    static {
        INSTANCE.disableHtmlEscaping();
        INSTANCE.registerTypeAdapter(WxAccessToken.class, new WxAccessTokenAdapter());
        INSTANCE.registerTypeAdapter(WxError.class, new WxErrorAdapter());
        INSTANCE.registerTypeAdapter(WxMenu.class, new WxMenuGsonAdapter());
        INSTANCE.registerTypeAdapter(WxMediaUploadResult.class, new WxMediaUploadResultAdapter());
        INSTANCE.registerTypeAdapter(WxNetCheckResult.class, new WxNetCheckResultGsonAdapter());

    }

    public static Gson create() {
        if (Objects.isNull(GSON_INSTANCE)) {
            synchronized (INSTANCE) {
                if (Objects.isNull(GSON_INSTANCE)) {
                    GSON_INSTANCE = INSTANCE.create();
                }
            }
        }
    return GSON_INSTANCE;
  }

}
