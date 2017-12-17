package com.transaction.processor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
@ComponentScan(basePackages = { "com.transaction.processor.controller" })
public class WebConfiguration {

	@Value("${server.port}")
	private int serverPort;
	
	@Value("${server.context-path}")
	private String contextPath;
	
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
		factory.setContextPath(contextPath);
		factory.setPort(serverPort);
		return factory;
	}
}