/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.api.common.redission;

import java.util.HashMap;
import java.util.Map;

import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.core.RBlockingQueue;

public class DistributedQueue<T> {
	static Map<String,Redisson> map = new HashMap<String, Redisson>();
	public static Redisson getRedisson(String address,String password){
		if(!map.containsKey(address)){
			Config config = new Config();
//			config.useSingleServer().setAddress(address).setDatabase(Integer.parseInt(RedisConfig.get("redissionDb")));			
			config.useSingleServer().setAddress(address).setPassword(password);
			map.put(address, Redisson.create(config));
		}
		return map.get(address);
	}
	

	protected Redisson mRedisson;

	protected RBlockingQueue<T> mQueue;

    public DistributedQueue(String address,String password, String name) {
//		Config config = new Config();
//		config.useSingleServer().setAddress(address);
//		mRedisson = Redisson.create(config);
    	mRedisson = DistributedQueue.getRedisson(address,password);
		mQueue = mRedisson.getBlockingQueue(name);
		
		//mQueue.
	}

    public DistributedQueue(Redisson redisson, String name) {
        mRedisson = redisson;

        mQueue = mRedisson.getBlockingQueue(name);
    }

    public boolean isEmpty() {
    	return mQueue.isEmpty();
    }

    public T peek() {
    	return mQueue.peek();
    }

    public boolean offer(T message) {
    	return mQueue.offer(message);
    }

    public T poll() {
    	return mQueue.poll();
    }
    
    public boolean remove(T message){
    	return mQueue.remove(message);
    }

    public void put(T message) throws InterruptedException {
    	mQueue.put(message);
    }

    public T take() throws InterruptedException {
    	return mQueue.take();
    }
    public Integer size(){
    	return mQueue.size();
    }
    public void clear() {
        mQueue.clear();
    }

    public void shutdown() {
    	mRedisson.shutdown();
    }

}