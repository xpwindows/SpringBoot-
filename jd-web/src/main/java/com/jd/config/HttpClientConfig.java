package com.jd.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * httpClient的配置类
 */
@Configuration
@PropertySource(value="classpath:/properties/httpClient.properties")
public class HttpClientConfig {
	@Value("${http.maxTotal}")
	private Integer maxTotal;						//最大连接数

	@Value("${http.defaultMaxPerRoute}")
	private Integer defaultMaxPerRoute;				//最大并发链接数

	@Value("${http.connectTimeout}")
	private Integer connectTimeout;					//创建链接的超时时间

	@Value("${http.connectionRequestTimeout}") 
	private Integer connectionRequestTimeout;		//链接获取超时时间

	@Value("${http.socketTimeout}")
	private Integer socketTimeout;			  		//数据传输超时时间

	@Value("${http.staleConnectionCheckEnabled}")
	private boolean staleConnectionCheckEnabled; 	//提交时检查链接是否可用

	//实例化连接池管理器对象
	@Bean(name="httpClientConnectionManager")
	public PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager() {
		PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
		manager.setMaxTotal(maxTotal);  //设定最大链接数
		manager.setDefaultMaxPerRoute(defaultMaxPerRoute);  //设定并发链接数
		return manager;
	}

	//定义HttpClient
	/**
	 * 实例化连接池
	 *
	 * 这里需要以参数形式注入上面实例化的连接池管理器
      @Qualifier 指定bean标签进行注入
	 */
	@Bean(name = "httpClientBuilder")
	public HttpClientBuilder getHttpClientBuilder(@Qualifier("httpClientConnectionManager")PoolingHttpClientConnectionManager httpClientConnectionManager){

		//HttpClientBuilder中的构造方法被protected修饰，所以这里不能直接使用new来实例化一个HttpClientBuilder,可以使用HttpClientBuilder提供的静态方法create()来获取HttpClientBuilder对象
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		httpClientBuilder.setConnectionManager(httpClientConnectionManager);
		return httpClientBuilder;
	}

	/**
	 * 	注入连接池管理器对象，用于获取httpClient
	 */
	@Bean
	public CloseableHttpClient getCloseableHttpClient(@Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder){

		return httpClientBuilder.build();
	}

	/**
	 * 设置各种超时时间等
	 */
	@Bean(name = "builder")
	public RequestConfig.Builder getBuilder(){
		RequestConfig.Builder builder = RequestConfig.custom();
		return builder.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectionRequestTimeout)
				.setSocketTimeout(socketTimeout)
				.setStaleConnectionCheckEnabled(staleConnectionCheckEnabled);
	}

	/**
	 * 使用builder构建一个RequestConfig对象；
	 * 请求配置对象就是一些各种超时时间的配置信息
	 */
	@Bean
	public RequestConfig getRequestConfig(@Qualifier("builder") RequestConfig.Builder builder){
		return builder.build();
	}
}
