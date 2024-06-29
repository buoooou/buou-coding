package com.github.buoooou.wxpay.common.util;

import lombok.extern.slf4j.Slf4j;
import com.github.buoooou.wxpay.common.api.WxErrorExceptionHandler;
import com.github.buoooou.wxpay.common.error.WxErrorException;

/**
 * @author Daniel Qian
 */
@Slf4j
public class LogExceptionHandler implements WxErrorExceptionHandler {
  @Override
  public void handle(WxErrorException e) {
    log.error("Error happens", e);
  }

}
