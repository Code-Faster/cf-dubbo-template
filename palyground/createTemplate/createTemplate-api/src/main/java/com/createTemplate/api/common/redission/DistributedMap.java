/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.api.common.redission;

import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.core.RMap;

public class DistributedMap<K, V> {

	private Redisson mRedisson;

	private RMap<K, V> mMap;

    public DistributedMap(String address, String name) {
		Config config = new Config();
		config.useSingleServer().setAddress(address);
		mRedisson = Redisson.create(config);

		mMap = mRedisson.getMap(name);
	}

    public DistributedMap(Redisson redisson, String name) {
        mRedisson = redisson;

        mMap = mRedisson.getMap(name);
    }

    public boolean fastPut(K key, V val) {
    	return mMap.fastPut(key, val);
    }

    public V put(K key, V val) {
    	return mMap.put(key, val);
    }

    public V putIfAbsent(K key, V val) {
    	return mMap.putIfAbsent(key, val);
    }

    public V get(K key) {
    	return mMap.get(key);
    }

    public V remove(K key) {
    	return mMap.remove(key);
    }

    public void clear() {
    	mMap.clear();
    }

    public void shutdown() {
    	mRedisson.shutdown();
    }

}