package com.github.buoooou.wxpay.config;

import com.github.buoooou.wxpay.exception.WxPayException;
import com.github.buoooou.wxpay.service.WxPayService;
import com.github.buoooou.wxpay.testbase.CustomizedApiTestModule;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

/**
 * @author <a href="https://github.com/ifcute">dagewang</a>
 */
@Slf4j
@Test
@Guice(modules = CustomizedApiTestModule.class)
public class CustomizedWxPayConfigTest {

  @Inject
  private WxPayService wxPayService;

  public void testCustomizerHttpClient() {
    try {
      wxPayService.queryOrder("a", null);
    } catch (WxPayException e) {
      // ignore
      e.printStackTrace();
    }
  }

  public void testCustomizerV3HttpClient() {
    try {
      wxPayService.queryOrderV3("a", null);
    } catch (WxPayException e) {
      // ignore
      e.printStackTrace();
    }
  }
}
