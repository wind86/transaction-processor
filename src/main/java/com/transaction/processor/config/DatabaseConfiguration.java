package com.transaction.processor.config;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.ArrayUtils.toArray;
import static org.cassandraunit.utils.EmbeddedCassandraServerHelper.cleanEmbeddedCassandra;
import static org.cassandraunit.utils.EmbeddedCassandraServerHelper.startEmbeddedCassandra;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories("com.transaction.processor.repository")
public class DatabaseConfiguration extends AbstractCassandraConfiguration {
	
	private static final String CREATE_KEYSPACE_QUERY = ""
			+ "CREATE KEYSPACE IF NOT EXISTS %s "
			+ "WITH durable_writes = true "
			+ "AND replication = {'class':'SimpleStrategy', 'replication_factor' : 3};";
	
	private static final String DROP_KEYSPACE_QUERY = "DROP KEYSPACE %s;";
	
	private static final String PROPERTIES_FILE = "cassandra.yaml";
	
	private static final long STARTUP_TIMEOUT = 20000;


	@Value("${spring.data.cassandra.keyspace-name}")
	private String keyspace;
	
	@Value("${spring.data.cassandra.contact-points}")
	private String contactPoints;
	
	@Value("${spring.data.cassandra.port}")
	private Integer port;

	@PostConstruct
	public void startDatabase() throws ConfigurationException, TTransportException, IOException {
		startEmbeddedCassandra(PROPERTIES_FILE, STARTUP_TIMEOUT);
	}

	@PreDestroy
	public void stopDatabase() {
		cleanEmbeddedCassandra();
	}
	
	@Override
	public SchemaAction getSchemaAction() {
		return SchemaAction.CREATE_IF_NOT_EXISTS;
	}

	@Override
	public String[] getEntityBasePackages() {
		return toArray("com.transaction.processor.entity");
	}

	@Override
	protected String getKeyspaceName() {
		return keyspace;
	}

	@Override
	protected List<String> getStartupScripts() {
		return createKeyspaceScript(CREATE_KEYSPACE_QUERY);
	}

	@Override
	protected List<String> getShutdownScripts() {
		return createKeyspaceScript(DROP_KEYSPACE_QUERY);
	}
	
	private List<String> createKeyspaceScript(String queryPattern) {
		return singletonList(createKeyspaceQuery(queryPattern));
	}
	
	private String createKeyspaceQuery(String queryPattern) {
		return String.format(queryPattern, keyspace);
	}
}