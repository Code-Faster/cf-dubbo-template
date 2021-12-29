package com.createTemplate.api.util.wxpay;


public class WXCloud {
    public WXPay wXPay;
    
    private WXCloud(){
        wXPay=new WXPay();
    }
    
    private volatile static WXCloud wXCloud;
    
    public static WXCloud getInstance() {
        if (wXCloud == null) {
            synchronized (WXCloud.class) {
                if (wXCloud == null)
                    wXCloud = new WXCloud();//instance为volatile，现在没问题了
            }
        }
        return wXCloud;
    }

} 
