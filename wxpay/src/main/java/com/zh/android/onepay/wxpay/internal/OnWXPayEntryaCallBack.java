package com.zh.android.onepay.wxpay.internal;

/**
 * <b>Package:</b> com.zh.android.onepay.wxpay <br>
 * <b>Create Date:</b> 2020-02-11  10:11 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b> 微信支付的回调 <br>
 */
public interface OnWXPayEntryaCallBack {
    /**
     * 初始化结束
     */
    void onInitFinished();

    /**
     * 支付过程取消
     *
     * @param developerpayload 回调的代码
     */
    void onPayCancel(String developerpayload);

    /**
     * 支付成功
     *
     * @param developerpayload 回调的代码
     */
    void onPaySuccessed(String developerpayload);

    /**
     * 支付失败
     *
     * @param developerpayload 回调的代码
     * @param code             失败代号
     */
    void onPayFailture(String developerpayload, String code);
}