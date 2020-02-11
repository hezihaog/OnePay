package com.zh.android.onepay.wxpay;

import android.app.Activity;

import com.zh.android.onepay.IPayCallback;
import com.zh.android.onepay.PayApi;
import com.zh.android.onepay.PayParams;
import com.zh.android.onepay.UILifecycleObserver;
import com.zh.android.onepay.wxpay.internal.WXPayHelper;

/**
 * <b>Package:</b> com.zh.android.onepay.wxpay <br>
 * <b>Create Date:</b> 2020-02-11  11:07 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b> 微信支付实现 <br>
 */
public class WXPayImpl implements PayApi, UILifecycleObserver {
    @Override
    public void startPay(Activity activity, PayParams params, IPayCallback payCallback) {
        WXPayHelper.wxpay(activity, params.getPayString(), payCallback);
    }

    @Override
    public void onUIResume() {
    }

    @Override
    public void onUIStop() {
    }

    @Override
    public void onUIDestroy() {
        WXPayHelper.onDestroy();
    }
}