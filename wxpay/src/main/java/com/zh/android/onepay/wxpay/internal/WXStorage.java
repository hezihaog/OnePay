package com.zh.android.onepay.wxpay.internal;

/**
 * <b>Package:</b> com.zh.android.onepay.wxpay <br>
 * <b>Create Date:</b> 2020-02-11  10:52 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b> 微信信息存储 <br>
 */
public class WXStorage {
    /**
     * 微信支付AppId
     */
    private static final String WX_PAY_APPID = "pay_wx_pay_appid";

    /**
     * 保存微信支付AppId
     */
    public static void saveWxPayId(String id) {
        SPUtils.getInstance().put(WX_PAY_APPID, id);
    }

    /**
     * 获取保存微信支付AppId
     */
    public static String getWxPayAppid() {
        return SPUtils.getInstance().getString(WX_PAY_APPID, "");
    }
}