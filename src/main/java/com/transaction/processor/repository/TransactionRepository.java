package com.transaction.processor.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.transaction.processor.entity.TransactionEntity;

// TODO: check Cassandra reactive repository
// https://spring.io/blog/2016/11/28/going-reactive-with-spring-data
@Repository
public interface TransactionRepository extends CassandraRepository<TransactionEntity> {

}