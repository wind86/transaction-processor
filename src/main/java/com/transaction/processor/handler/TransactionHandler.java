package com.transaction.processor.handler;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.utils.UUIDs;
import com.transaction.processor.entity.TransactionEntity;
import com.transaction.processor.handler.message.NewTransactionMessage;
import com.transaction.processor.repository.TransactionRepository;

import akka.actor.UntypedAbstractActor;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TransactionHandler extends UntypedAbstractActor {

	private TransactionRepository transactionRepository;
	
	public TransactionHandler(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}
	
	@Override
	public void onReceive(Object message) throws Throwable {
		if (!(message instanceof NewTransactionMessage)) {
			unhandled(message);
			return;
		}
		
		handle((NewTransactionMessage) message);
	}
	
	private void handle(NewTransactionMessage message) {
		TransactionEntity entity = transactionRepository.save(newEntity(message));
		
		getSender().tell(String.valueOf(entity.getId()), getSelf());
	}
	
	private static TransactionEntity newEntity(NewTransactionMessage message) {
		TransactionEntity entity = new TransactionEntity();
		
		entity.setId(UUIDs.timeBased());
		entity.setIssuerAccountId(message.getIssuerAccountId());
		entity.setRecipientAccountId(message.getRecipientAccountId());
		entity.setTimepoint(message.getTime());
		entity.setValue(message.getValue());
		
		return entity;
	}
}