package com.github.buoooou.wxpay.common.api;

import com.github.buoooou.wxpay.common.error.WxErrorException;

/**
 * WxErrorException处理器.
 *
 * @author Daniel Qian
 */
public interface WxErrorExceptionHandler {

  void handle(WxErrorException e);

}
