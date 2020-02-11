package com.zh.android.onepay.sample.pay;

import android.app.Activity;

import com.zh.android.onepay.IPayCallback;
import com.zh.android.onepay.OnePay;
import com.zh.android.onepay.PayParams;
import com.zh.android.onepay.alipay.AlipayImpl;
import com.zh.android.onepay.wxpay.WXPayImpl;

/**
 * <b>Package:</b> com.zh.android.onepay.sample.pay <br>
 * <b>Create Date:</b> 2020-02-11  12:01 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b>  <br>
 */
public class PayManager {
    private static final String ALIPAY = "alipay";
    private static final String WXPAY = "wxpay";

    /**
     * 支付门面对象
     */
    private final OnePay mOnePay;

    private PayManager() {
        mOnePay = OnePay.getInstance();
        //注册支付宝支付
        mOnePay.registerPayApi(ALIPAY, new AlipayImpl());
        //注册微信支付
        mOnePay.registerPayApi(WXPAY, new WXPayImpl());
    }

    private static class SingleHolder {
        private static final PayManager INSTANCE = new PayManager();
    }

    public static PayManager getInstance() {
        return SingleHolder.INSTANCE;
    }

    /**
     * 调用支付宝支付
     *
     * @param activity    上下文
     * @param params      支付参数
     * @param payCallback 支付结果回调
     */
    public void alipay(Activity activity, PayParams params, IPayCallback payCallback) {
        mOnePay.getPayApi(ALIPAY).startPay(activity, params, payCallback);
    }

    /**
     * 调用微信支付
     *
     * @param activity    上下文
     * @param params      支付参数
     * @param payCallback 支付结果回调
     */
    public void wxpay(Activity activity, PayParams params, IPayCallback payCallback) {
        mOnePay.getPayApi(WXPAY).startPay(activity, params, payCallback);
    }
}