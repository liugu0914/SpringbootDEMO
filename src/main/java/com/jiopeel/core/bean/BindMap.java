package com.jiopeel.core.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于客户端数据的绑定,由于spring不能指定map的优先级绑定下,只能继承map,使用此类进行Map绑定
 * 
 * @author liud at 2013-10-25
 * @param <K>
 *            key map的KEY类型
 * @param <V>
 *            value map的Val类型
 */
public class BindMap<K, V> {

	private Map<K, V> bindMap;

	BindMap() {
		this.bindMap = new HashMap<K, V>();
	}

	public V put(K key, V value) {
		return this.bindMap.put(key, value);
	}

	public void putAll(Map<? extends K, ? extends V> map) {
		this.bindMap.putAll(map);
	}

	/**
	 * 转换为Map
	 * 
	 * 
	 * @return map对象
	 */
	public Map<K, V> toMap() {
		return this.bindMap;
	}
}
