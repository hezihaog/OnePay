package com.zh.android.onepay;

import android.app.Activity;

/**
 * <b>Package:</b> com.zh.android.onepay <br>
 * <b>Create Date:</b> 2020-02-10  16:20 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b> 统一支付入口，内部控制具体实例进行调用 <br>
 */
public class OnePay implements PayApi {
    /**
     * 具体实现
     */
    private PayApi mPayImpl;

    private OnePay() {ww
    }

    private static final class SingleHolder {
        private static final OnePay INSTANCE = new OnePay();
    }

    /**
     * 获取实例
     */
    public static OnePay getInstance() {
        return SingleHolder.INSTANCE;
    }

    /**
     * 配置支付的实现，可以传入OnePayApi的具体实现类，例如支付宝、微信
     *
     * @param payImpl 具体实现
     */
    public void setupImpl(PayApi payImpl) {
        mPayImpl = payImpl;
    }

    @Override
    public void startPay(Activity activity, PayParams params, IPayCallback payCallback) {
        mPayImpl.startPay(activity, params, payCallback);
    }
}