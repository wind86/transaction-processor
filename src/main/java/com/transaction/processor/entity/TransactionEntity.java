package com.transaction.processor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Table("transactions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKeyColumn(name = "id", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	UUID id;

	@PrimaryKeyColumn(name = "issuer_account_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	Long issuerAccountId;

	@PrimaryKeyColumn(name = "recipient_account_id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
	Long recipientAccountId;

	@Column("time")
	Date timepoint;
	
	@Column("value")
	BigDecimal value;
}