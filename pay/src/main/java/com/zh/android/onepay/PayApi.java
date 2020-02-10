package com.zh.android.onepay;

import android.app.Activity;

/**
 * <b>Package:</b> com.zh.android.onepay <br>
 * <b>Create Date:</b> 2020-02-10  15:47 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b> 统一支付API接口 <br>
 */
public interface PayApi {
    /**
     * 开始支付
     *
     * @param activity    上下文
     * @param params      支付参数
     * @param payCallback 支付结果回调
     */
    void startPay(Activity activity, PayParams params, IPayCallback payCallback);
}