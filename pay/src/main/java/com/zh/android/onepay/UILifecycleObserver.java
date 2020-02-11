package com.zh.android.onepay;

/**
 * <b>Package:</b> com.zh.android.onepay <br>
 * <b>Create Date:</b> 2020-02-11  11:36 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b> 页面生命周期监听接口 <br>
 */
public interface UILifecycleObserver {
    /**
     * 页面获取到焦点
     */
    void onUIResume();

    /**
     * 页面失去焦点
     */
    void onUIStop();

    /**
     * 页面销毁
     */
    void onUIDestroy();
}