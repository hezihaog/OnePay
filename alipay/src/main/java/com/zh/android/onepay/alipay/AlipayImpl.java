package com.zh.android.onepay.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.zh.android.onepay.IPayCallback;
import com.zh.android.onepay.PayApi;
import com.zh.android.onepay.PayParams;

import java.util.Map;

/**
 * <b>Package:</b> com.zh.android.onepay.alipay <br>
 * <b>Create Date:</b> 2020-02-10  17:12 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b> 支付宝支付实现 <br>
 */
public class AlipayImpl implements PayApi {
    /**
     * 支付结果Flag
     */
    private static final int ALIPAY_PAY_FLAG = 1;

    /**
     * 支付结果码
     */
    private interface PayResultCode {
        /**
         * 支付成功Code
         */
        String ALIPAY_PAY_SUCC = "9000";
        /**
         * 支付失败Code
         */
        String ALIPAY_PAY_CANCEL = "6001";
    }

    /**
     * 处理结果Handler
     */
    private AlipayPayResultHandler mPayResultHandler;

    @Override
    public void startPay(final Activity activity, final PayParams params, final IPayCallback payCallback) {
        //获取支付信息
        final String orderInfo = params.getPayString();
        if (TextUtils.isEmpty(orderInfo)) {
            if (payCallback != null) {
                payCallback.onPayFailture();
            }
            return;
        }
        try {
            //调用支付宝的支付API进行支付
            Thread payThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    PayTask payTask = new PayTask(activity);
                    Map<String, String> result = payTask.payV2(orderInfo, true);
                    //用Handler发送支付结果
                    ensureInitPayHandler(payCallback);
                    Message message = Message.obtain(mPayResultHandler);
                    message.what = ALIPAY_PAY_FLAG;
                    message.obj = result;
                    mPayResultHandler.sendMessage(message);
                }
            });
            payThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            if (payCallback != null) {
                payCallback.onPayFailture();
            }
        }
    }

    /**
     * 用于接收支付结果的Handler
     */
    private static class AlipayPayResultHandler extends Handler {
        /**
         * 支付回调
         */
        private IPayCallback mPayCallback;

        AlipayPayResultHandler(IPayCallback payCallback) {
            mPayCallback = payCallback;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //支付结果
                case ALIPAY_PAY_FLAG: {
                    //对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                    AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
                    //用支付状态，判断支付结果
                    String resultStatus = payResult.getResultStatus();
                    //为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, PayResultCode.ALIPAY_PAY_SUCC)) {
                        //该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        if (mPayCallback != null) {
                            mPayCallback.onPaySuccess();
                        }
                    } else if (TextUtils.equals(resultStatus, PayResultCode.ALIPAY_PAY_CANCEL)) {
                        //"6001" 代表用户中途取消支付
                        if (mPayCallback != null) {
                            mPayCallback.onPayCancel();
                        }
                    } else {
                        //该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        if (mPayCallback != null) {
                            mPayCallback.onPayFailture();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }

    /**
     * '
     * 确保初始化了Handler
     */
    private void ensureInitPayHandler(IPayCallback payCallback) {
        if (mPayResultHandler == null) {
            mPayResultHandler = new AlipayPayResultHandler(payCallback);
        }
    }
}