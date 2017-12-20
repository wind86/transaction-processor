package com.transaction.processor.controller;

import static com.transaction.processor.handler.extension.SpringExtension.SPRING_EXTENSION_PROVIDER;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public abstract class AbstractController {

	protected ActorRef getHandlerActor(ActorSystem system, String actorId) {
		return system.actorOf(SPRING_EXTENSION_PROVIDER.get(system).props(actorId));
	}
}