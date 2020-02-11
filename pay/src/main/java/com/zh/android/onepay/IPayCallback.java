package com.zh.android.onepay;

/**
 * <b>Package:</b> com.zh.android.onepay <br>
 * <b>Create Date:</b> 2020-02-10  16:57 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b> 支付回调 <br>
 */
public interface IPayCallback {
    /**
     * 支付成功
     */
    void onPaySuccess();

    /**
     * 支付失败
     *
     * @param error 异常对象
     * @param msg   异常原因
     */
    void onPayFailture(Throwable error, String msg);

    /**
     * 支付取消
     */
    void onPayCancel();
}