package com.zh.android.onepay.wxpay.internal;

import android.app.Activity;

/**
 * <b>Package:</b> com.zh.android.onepay.wxpay <br>
 * <b>Create Date:</b> 2020-02-11  10:16 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b> 微信支付监听，之前是放在Application里面的，现在放到这里 <br>
 */
public class WXPayCallBackInstance {
    private WXPayCallBackInstance() {
    }

    private static class SingletonHolder {
        private static final WXPayCallBackInstance INSTANCE = new WXPayCallBackInstance();
    }

    public static WXPayCallBackInstance get() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 微信支付监听
     */
    private OnWXPayEntryaCallBack mOnWXPayEntryCallBack;
    /**
     * 微信支付Activity
     */
    private Activity mWxPayActivity;

    /**
     * 获取微信支付回调
     *
     * @return OnWXPayEntryaCallBack
     */
    public OnWXPayEntryaCallBack getWXPayCallBack() {
        if (mWxPayActivity == null || mWxPayActivity.isFinishing()) {
            return null;
        }
        if (mOnWXPayEntryCallBack != null) {
            return mOnWXPayEntryCallBack;
        }
        return null;
    }

    /**
     * 设置支付完成
     */
    public void setupWXPayFinish() {
        mOnWXPayEntryCallBack = null;
        mWxPayActivity = null;
    }

    /**
     * 设置微信支付回调接口
     *
     * @param activity activity
     * @param callback OnWXPayEntryaCallBack
     */
    public void setupWXPayCallBack(
            Activity activity,
            OnWXPayEntryaCallBack callback) {
        mWxPayActivity = activity;
        mOnWXPayEntryCallBack = callback;
    }
}