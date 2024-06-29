package com.github.buoooou.wxpay.common.bean.ocr;

import com.github.buoooou.wxpay.common.util.json.WxGsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

/**
 * 银行卡OCR识别结果
 *
 * @author Theo Nie
 */
@Data
public class WxOcrBankCardResult implements Serializable {

    private static final long serialVersionUID = 554136620394204143L;
    @SerializedName("number")
    private String number;

    @Override
    public String toString() {
        return WxGsonBuilder.create().toJson(this);
    }

    public static WxOcrBankCardResult fromJson(String json) {
        return WxGsonBuilder.create().fromJson(json, WxOcrBankCardResult.class);
    }
}
