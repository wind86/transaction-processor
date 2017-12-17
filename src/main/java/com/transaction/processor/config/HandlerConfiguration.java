package com.transaction.processor.config;

import static com.transaction.processor.config.HandlerConfiguration.SpringExtension.SPRING_EXTENSION_PROVIDER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import akka.actor.AbstractExtensionId;
import akka.actor.Actor;
import akka.actor.ActorSystem;
import akka.actor.ExtendedActorSystem;
import akka.actor.Extension;
import akka.actor.IndirectActorProducer;
import akka.actor.Props;

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

	public static class SpringActorProducer implements IndirectActorProducer {

		private ApplicationContext applicationContext;

		private String beanActorName;

		public SpringActorProducer(ApplicationContext applicationContext, String beanActorName) {
			this.applicationContext = applicationContext;
			this.beanActorName = beanActorName;
		}

		@Override
		public Actor produce() {
			return (Actor) applicationContext.getBean(beanActorName);
		}

		@Override
		@SuppressWarnings("unchecked")
		public Class<? extends Actor> actorClass() {
			return (Class<? extends Actor>) applicationContext.getType(beanActorName);
		}
	}

	public static class SpringExtension extends AbstractExtensionId<SpringExtension.SpringExt> {

		public static final SpringExtension SPRING_EXTENSION_PROVIDER = new SpringExtension();

		@Override
		public SpringExt createExtension(ExtendedActorSystem system) {
			return new SpringExt();
		}

		public static class SpringExt implements Extension {

			private volatile ApplicationContext applicationContext;

			public void initialize(ApplicationContext applicationContext) {
				this.applicationContext = applicationContext;
			}

			public Props props(String actorBeanName) {
				return Props.create(SpringActorProducer.class, applicationContext, actorBeanName);
			}
		}
	}
	
}