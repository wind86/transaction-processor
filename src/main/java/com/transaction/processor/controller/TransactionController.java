package com.transaction.processor.controller;

import static akka.pattern.Patterns.ask;
import static akka.util.Timeout.durationToTimeout;
import static com.transaction.processor.config.HandlerConfiguration.TRANSACTION_HANDLER;
import static java.util.Collections.singletonMap;
import static scala.concurrent.Await.result;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.transaction.processor.dto.TransactionDto;
import com.transaction.processor.handler.message.NewTransactionMessage;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

// TODO: update to Spring 5 and try reactive streams
@RestController
public class TransactionController extends AbstractController {

	@Autowired
	private ActorSystem transactionHandlerSystem;

	@PostMapping("/")
	public ResponseEntity<Map<String, String>> saveTransaction(@Valid @RequestBody TransactionDto transaction) throws Exception {
		NewTransactionMessage message = new NewTransactionMessage(transaction.getIssuerAccountId(),
				transaction.getRecipientAccountId(), transaction.getTime(), transaction.getValue());

		ActorRef handler = getHandlerActor(transactionHandlerSystem, TRANSACTION_HANDLER);
		FiniteDuration duration = FiniteDuration.create(1, TimeUnit.SECONDS);

		Future<Object> transactionIdFuture = ask(handler, message, durationToTimeout(duration));

		String transactionId = (String) result(transactionIdFuture, duration);
		return ResponseEntity.ok(singletonMap("transactionId", transactionId));
	}
}