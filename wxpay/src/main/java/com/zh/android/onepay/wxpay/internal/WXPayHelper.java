package com.zh.android.onepay.wxpay.internal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zh.android.onepay.IPayCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <b>Package:</b> com.zh.android.onepay.wxpay <br>
 * <b>Create Date:</b> 2020-02-11  10:17 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b> 微信支付帮助类 <br>
 */
public class WXPayHelper {
    private WXPayHelper() {
    }

    /**
     * 页面销毁时调用
     */
    public static void onDestroy() {
        WXPayCallBackInstance.get().setupWXPayFinish();
    }

    /**
     * 微信支付
     *
     * @param payString {
     *                  charge_url: "",
     *                  parameters: "{"appid":"wx4c7e15768818a0fb","partnerid":"1220079301","prepayid":"wx201803241656559023f4b1cf0766325797","package":"Sign=WXPay","timestamp":1521881815,"noncestr":"5ab612d7b1afb","sign":"B739A8678BDFC5172D543D0B752731B5"}"
     *                  }
     * @param callback  监听
     */
    public static void wxpay(final Activity context, final String payString, final IPayCallback callback) {
        try {
            if (TextUtils.isEmpty(payString)) {
                if (callback != null) {
                    String msg = "payString为空";
                    callback.onPayFailture(new NullPointerException(msg), msg);
                }
                return;
            }
            PayReq req = null;
            String errorMsg = "解析微信支付需要的参数异常";
            try {
                JSONObject json = new JSONObject(payString);
                String parameters = json.getString("parameters");
                JSONObject payJson = new JSONObject(parameters);
                req = getPayReq(payJson);
                if (req == null) {
                    if (callback != null) {
                        callback.onPayFailture(new JSONException(errorMsg), errorMsg);
                    }
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onPayFailture(e, errorMsg);
                }
            }
            WXStorage.saveWxPayId(req.appId);
            IWXAPI iwxapi = getWxApi(context, req.appId, false, true);
            WXPayCallBackInstance.get().setupWXPayCallBack(context, new OnWXPayEntryaCallBack() {
                @Override
                public void onInitFinished() {
                }

                @Override
                public void onPayCancel(String developerpayload) {
                    if (callback != null) {
                        callback.onPayCancel();
                    }
                }

                @Override
                public void onPaySuccessed(String developerpayload) {
                    if (callback != null) {
                        callback.onPaySuccess();
                    }
                }

                @Override
                public void onPayFailture(String developerpayload, String code) {
                    if (callback != null) {
                        String msg = "微信支付失败，developerpayload = " + developerpayload + " code = " + code;
                        callback.onPayFailture(new RuntimeException(msg), msg);
                    }
                }
            });
            iwxapi.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onPayFailture(e, "调起微信支付发生异常");
            }
        }
    }

    /**
     * 获取微信支付API对象-IWXAPI
     *
     * @param context   context
     * @param appid     应用id
     * @param checksign 是否检验签名
     * @param register  是否将应用注册到微信
     */
    public static IWXAPI getWxApi(Context context, String appid, boolean checksign, boolean register) {
        IWXAPI wxApi = WXAPIFactory.createWXAPI(context, appid, checksign);
        if (register) {
            boolean result = wxApi.registerApp(appid);
        }
        return wxApi;
    }

    /**
     * H5微信支付
     *
     * @param payString {
     *                  "charge_url":"weixin:\/\/wap\/pay?prepayid=wx02180524527260eed5eb51822016753075&package=1390456132&noncestr=1533204324&sign=e0b7aef2dd55df7ba5df18277ce8be60",
     *                  "parameters":"[]"
     *                  }
     * @param callback  监听
     */
    public void wxpayH5(final Activity activity, final String payString, final IPayCallback callback) {
        try {
            if (TextUtils.isEmpty(payString)) {
                if (callback != null) {
                    String msg = "payString为空";
                    callback.onPayFailture(new NullPointerException(msg), msg);
                }
                return;
            }
            JSONObject json = new JSONObject(payString);
            String url = json.getString("charge_url");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onPayFailture(e, "调起微信H5支付失败");
            }
        }
    }

    /**
     * 是否安装了微信
     */
    public static boolean isInstallWeixin(Context context) {
        //兼容
        try {
            if (context == null) {
                return false;
            }
            PackageInfo info;
            PackageManager manager = context.getPackageManager();
            if (manager == null) {
                return false;
            }
            info = manager.getPackageInfo("com.tencent.mm", 0);
            return info != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取调起支付需要的参数
     *
     * @param json json
     * @return {@link PayReq}
     */
    private static PayReq getPayReq(JSONObject json) {
        try {
            PayReq req = new PayReq();
            req.appId = json.getString("appid");
            req.partnerId = json.getString("partnerid");
            req.prepayId = json.getString("prepayid");
            req.nonceStr = json.getString("noncestr");
            req.timeStamp = json.getString("timestamp");
            req.packageValue = json.getString("package");
            req.sign = json.getString("sign");
            return req;
        } catch (Exception e) {
            return null;
        }
    }
}