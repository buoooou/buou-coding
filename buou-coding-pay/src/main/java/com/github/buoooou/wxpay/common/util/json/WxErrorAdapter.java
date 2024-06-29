package com.github.buoooou.wxpay.common.util.json;

import com.google.gson.*;
import com.github.buoooou.wxpay.common.api.WxConsts;
import com.github.buoooou.wxpay.common.error.WxError;

import java.lang.reflect.Type;

/**
 * @author Daniel Qian.
 */
public class WxErrorAdapter implements JsonDeserializer<WxError> {

  @Override
  public WxError deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
    throws JsonParseException {
    WxError.WxErrorBuilder errorBuilder = WxError.builder();
    JsonObject wxErrorJsonObject = json.getAsJsonObject();

    if (wxErrorJsonObject.get(WxConsts.ERR_CODE) != null && !wxErrorJsonObject.get(WxConsts.ERR_CODE).isJsonNull()) {
      errorBuilder.errorCode(GsonHelper.getAsPrimitiveInt(wxErrorJsonObject.get(WxConsts.ERR_CODE)));
    }
    if (wxErrorJsonObject.get("errmsg") != null && !wxErrorJsonObject.get("errmsg").isJsonNull()) {
      errorBuilder.errorMsg(GsonHelper.getAsString(wxErrorJsonObject.get("errmsg")));
    }

    errorBuilder.json(json.toString());

    return errorBuilder.build();
  }

}
