package com.transaction.processor.config;

import static com.transaction.processor.handler.extension.SpringExtension.SPRING_EXTENSION_PROVIDER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import akka.actor.ActorSystem;

//Inspired by http://www.baeldung.com/akka-with-spring

@Configuration
@ComponentScan(basePackages = { "com.transaction.processor.handler" })
public class HandlerConfiguration {

	public static final String TRANSACTION_HANDLER = "transactionHandler";
	
	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	public ActorSystem transactionHandlerSystem() {
		ActorSystem system = ActorSystem.create(TRANSACTION_HANDLER);
		SPRING_EXTENSION_PROVIDER.get(system).initialize(applicationContext);
		return system;
	}	
}