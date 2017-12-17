package com.transaction.processor.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionDto {

	@NotNull
	Long issuerAccountId;
	
	@NotNull
	Long recipientAccountId;
	
	@NotNull
	@DateTimeFormat(pattern="YYYY-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="YYYY-MM-dd HH:mm:ss")
	Date time;
	
	@NotNull
	@Min(value = 0L)
	BigDecimal value;
}