package com.ovopark.dc.apigetway.sdk.kit;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName:  CurrentTimeMillisClockKit   
 * @Description:(时间戳工具类)
 * 解决System.currentTimeMillis()潜在的性能问题
 * 参考：https://www.jianshu.com/p/d2039190b1cb   
 * @author: Remiel_Mercy 
 * @date:   2020年4月22日 上午10:37:12   
 *     
 * @Copyright: 2020 www.ovopark.com Inc. All rights reserved.
 */
public class CurrentTimeMillisClockKit {
	private volatile long now;

	private CurrentTimeMillisClockKit() {
	        this.now = System.currentTimeMillis();
	        scheduleTick();
	    }

	private void scheduleTick() {
		new ScheduledThreadPoolExecutor(1, runnable -> {
			Thread thread = new Thread(runnable, "current-time-millis");
			thread.setDaemon(true);
			return thread;
		}).scheduleAtFixedRate(() ->
				now = System.currentTimeMillis(), 1, 1, TimeUnit.MILLISECONDS);
	}

	public long now() {
		return now;
	}

	public static CurrentTimeMillisClockKit getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		private static final CurrentTimeMillisClockKit INSTANCE = new CurrentTimeMillisClockKit();
	}
}
