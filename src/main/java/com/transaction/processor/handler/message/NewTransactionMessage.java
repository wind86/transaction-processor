package com.transaction.processor.handler.message;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewTransactionMessage {

	Long issuerAccountId;
	Long recipientAccountId;
	Date time;
	BigDecimal value;
}