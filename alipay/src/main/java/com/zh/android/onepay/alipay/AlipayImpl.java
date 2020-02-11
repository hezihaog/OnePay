package com.zh.android.onepay.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.zh.android.onepay.IPayCallback;
import com.zh.android.onepay.PayApi;
import com.zh.android.onepay.PayParams;
import com.zh.android.onepay.UILifecycleObserver;

import org.json.JSONObject;

import java.util.Map;

/**
 * <b>Package:</b> com.zh.android.onepay.alipay <br>
 * <b>Create Date:</b> 2020-02-10  17:12 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b> 支付宝支付实现 <br>
 */
public class AlipayImpl implements PayApi, UILifecycleObserver {
    /**
     * 支付结果Flag
     */
    private static final int ALIPAY_PAY_FLAG = 1;

    @Override
    public void onUIResume() {
    }

    @Override
    public void onUIStop() {
    }

    @Override
    public void onUIDestroy() {
    }

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
        //调起支付宝支付
        alipay(activity, orderInfo, payCallback);
    }

    /**
     * 支付宝支付
     *
     * @param payString {
     *                  charge_url: "",
     *                  parameters: "{"string":"app_id=2017120600403397&method=alipay.trade.app.pay&format=JSON&return_url=http%3A%2F%2Fsandbox.money.linghit.com%2Freturn%2F45%2F152188183109500080&charset=utf8&sign_type=RSA2&timestamp=2018-03-24+16%3A57%3A08&version=1.0&notify_url=http%3A%2F%2Fsandbox.money.linghit.com%2Fnotify_charge%2F45&biz_content=%7B%22out_trade_no%22%3A%22152188183109500080%22%2C%22total_amount%22%3A%220.10%22%2C%22subject%22%3A%22%5Cu6613%5Cu8d77%5Cu95ee%22%2C%22body%22%3A%22%5Cu6613%5Cu8d77%5Cu95ee%22%7D&sign=HwC7i1JkrWqmLRwUeuwhx8XGIspIv%2BkIJdApJt7PSKn30XjvkksT4aefdANNiW9QWTgNmGsmzf8Nz52UMEpDcs0qBxPF5Rc2lP%2B8SwKqruCUHiv7j4%2FCO1%2F82lLLCjahD87S%2B8ccUS7pfxCC44GZRndKpAZFUA9cEaFIMIvwW1ie9wmBScwua0NksNecA7U5Mm%2BRkhUlTt4mMb7SugFE9tTBkzLpUgTr2OVie1TFboxuqbMi34RTGLjW8wLzuTIEvPj%2F%2BxfcISU6wJ0MKAla4D8golcdbfHeO6xU80kwCj9kqy9LewWqOuVDAwu%2B7fFCy7VjzOlRhQQXdtlo84%2BPzQ%3D%3D"}"
     *                  }
     * @param callback  监听
     */
    @SuppressWarnings("ALL")
    public void alipay(final Activity activity, final String payString, final IPayCallback callback) {
        if (TextUtils.isEmpty(payString)) {
            if (callback != null) {
                String msg = "payString为空";
                callback.onPayFailture(new NullPointerException(msg), msg);
            }
            return;
        }
        try {
            JSONObject json = new JSONObject(payString);
            String parameters = json.getString("parameters");
            JSONObject payJson = new JSONObject(parameters);
            final String finalStr = payJson.getString("string");
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    PayTask payTask = new PayTask(activity);
                    Map<String, String> result = payTask.payV2(finalStr, true);
                    //用Handler发送支付结果
                    ensureInitPayHandler(callback);
                    Message message = Message.obtain(mPayResultHandler);
                    message.what = ALIPAY_PAY_FLAG;
                    message.obj = result;
                    mPayResultHandler.sendMessage(message);
                }
            };
            //调用支付宝的支付API进行支付
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onPayFailture(e, "支付宝支付，解析支付参数数据异常");
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
                            String errorMsg = "支付回调状态码不正确，非9000和6001，状态码：" + resultStatus;
                            mPayCallback.onPayFailture(new IllegalStateException(errorMsg), errorMsg);
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
     * 确保初始化了Handler
     */
    private void ensureInitPayHandler(IPayCallback payCallback) {
        if (mPayResultHandler == null) {
            mPayResultHandler = new AlipayPayResultHandler(payCallback);
        }
    }
}