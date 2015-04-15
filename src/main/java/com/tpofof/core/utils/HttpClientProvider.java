package com.tpofof.core.utils;

import static com.tpofof.core.utils.HttpClientProvider.HttpClientProviderScope.PER_THREAD;

import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

@Component("httpClientProvider")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public final class HttpClientProvider {
	
	public enum HttpClientProviderScope {
		SINGLETON,
		PROTOTYPE,
		PER_THREAD
	}

	private final Map<Long, HttpClient> clientMap;
	@Autowired private Config config;
	
	public HttpClientProvider() {
		this.clientMap = Maps.newConcurrentMap();
	}

	/**
	 * Defaults to PER_THREAD
	 * @return
	 */
	public HttpClient get() {
		return get(PER_THREAD);
	}
	
	public HttpClient get(HttpClientProviderScope scope) {
		long tid = 0;
		switch (scope) {
		case PER_THREAD:
			tid = Thread.currentThread().getId();
			break;
		case PROTOTYPE:
			return initClient();
		case SINGLETON:
			tid = -1;
			break;
		}
		if (!clientMap.containsKey(tid)) {
			clientMap.put(tid, initClient());
		}
		return clientMap.get(tid);
	}
	
	private HttpClient initClient() {
		final HttpConnectionManagerParams cmparams = new HttpConnectionManagerParams();
	    final int soTimeout = config.getInt("httpclient.sotimeout", 5000);
		cmparams.setSoTimeout(soTimeout);
	    cmparams.setTcpNoDelay(config.getBoolean("httpclient.tcpnodelay", true));
	    final HttpConnectionManager manager = new SimpleHttpConnectionManager();
		final HttpClientParams params = new HttpClientParams();
		params.setSoTimeout(soTimeout);
		params.setConnectionManagerTimeout(config.getInt("httpclient.connectionManager.timeout", 5000));
		return new HttpClient(params, manager);
	}
}
