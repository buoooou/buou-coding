package com.github.buoooou.wxpay.bean.marketing.payroll;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <pre>
 * 查询微工卡授权关系
 * 文档地址：https://pay.weixin.qq.com/wiki/doc/apiv3_partner/Offline/apis/chapter4_1_2.shtml
 *
 * 适用对象：服务商
 * 请求URL：https://api.mch.weixin.qq.com/v3/payroll-card/relations/{openid}
 * 请求方式：GET
 * </pre>
 *
 * @author xiaoqiang
 * created on  2021/12/2
 */
@Data
@NoArgsConstructor
public class RelationsRequest implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * <pre>
   * 字段名：用户标识
   * 变量名：openid
   * 是否必填：是
   * 类型：string[1, 64]
   * 描述：
   *  用户在商户对应appid下的唯一标识
   *  示例值：9x111111
   * </pre>
   */
  @SerializedName(value = "openid")
  private String openid;

  /**
   * <pre>
   * 字段名：子商户号
   * 变量名：sub_mchid
   * 是否必填：是
   * 类型：string[1, 32]
   * 描述：
   *  微信服务商下特约商户的商户号，由微信支付生成并下发
   * 示例值：1111111
   * </pre>
   */
  @SerializedName(value = "sub_mchid")
  private String subMchid;

  /**
   * <pre>
   * 字段名：应用ID
   * 变量名：appid
   * 是否必填：二选一
   * 类型：string[1, 32]
   * 描述：
   *   是服务商在微信申请公众号/小程序或移动应用成功后分配的账号ID（与服务商主体一致），登录平台为mp.weixin.qq.com或open.weixin.qq.com。
   *  当输入应用ID时，会校验其与服务商商户号的绑定关系。服务商应用ID和与子商户应用ID至少输入一个，且必须要有拉起微工卡时使用的APPID。
   *  示例值：wxa1111111
   * </pre>
   */
  @SerializedName(value = "appid")
  private String appid;

  /**
   * <pre>
   * 字段名：子商户应用ID
   * 变量名：sub_appid
   * 是否必填：是
   * 类型：string[1, 32]
   * 描述：
   *  是特约商户在微信申请公众号/小程序或移动应用成功后分配的账号ID（与特约商户主体一致），登录平台为mp.weixin.qq.com或open.weixin.qq.com。当输入子商户应用ID时，会校验其与特约商户号的绑定关系。 服务商应用ID和与子商户应用ID至少输入一个，且必须要有拉起微工卡时使用的APPID。
   *  示例值：wxa1111111
   * </pre>
   */
  @SerializedName(value = "sub_appid")
  private String subAppid;
}
