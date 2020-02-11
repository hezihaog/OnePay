package com.zh.android.onepay;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <b>Package:</b> com.zh.android.onepay <br>
 * <b>Create Date:</b> 2020-02-10  16:20 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b> 统一支付入口，内部控制具体实例进行调用 <br>
 */
public class OnePay implements UILifecycleObserver {
    /**
     * 支付集合
     */
    private Map<String, PayApi> mPayApis;

    private OnePay() {
        mPayApis = new ConcurrentHashMap<>();
    }

    @Override
    public void onUIResume() {
        for (Map.Entry<String, PayApi> entry : mPayApis.entrySet()) {
            PayApi api = entry.getValue();
            if (api instanceof UILifecycleObserver) {
                ((UILifecycleObserver)api).onUIResume();
            }
        }
    }

    @Override
    public void onUIStop() {
        for (Map.Entry<String, PayApi> entry : mPayApis.entrySet()) {
            PayApi api = entry.getValue();
            if (api instanceof UILifecycleObserver) {
                ((UILifecycleObserver)api).onUIStop();
            }
        }
    }

    @Override
    public void onUIDestroy() {
        for (Map.Entry<String, PayApi> entry : mPayApis.entrySet()) {
            PayApi api = entry.getValue();
            if (api instanceof UILifecycleObserver) {
                ((UILifecycleObserver)api).onUIDestroy();
            }
        }
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
     * 按名称获取支付实例
     */
    public PayApi getPayApi(String name) {
        return mPayApis.get(name);
    }

    /**
     * 注册支付的实现，可以传入OnePayApi的具体实现类，例如支付宝、微信
     *
     * @param name   API唯一名称
     * @param payApi API实现
     */
    public void registerPayApi(String name, PayApi payApi) {
        mPayApis.put(name, payApi);
    }

    /**
     * 取消注册支付实现
     *
     * @param payApi API实现
     */
    public void unregisterPayApi(PayApi payApi) {
        String name = null;
        for (Map.Entry<String, PayApi> entry : mPayApis.entrySet()) {
            if (entry.getValue() == payApi) {
                name = entry.getKey();
                break;
            }
        }
        if (name != null) {
            mPayApis.remove(name);
        }
    }

    /**
     * 取消注册支付实现
     *
     * @param name 唯一名称
     */
    public void unregisterPayApi(String name) {
        mPayApis.remove(name);
    }
}