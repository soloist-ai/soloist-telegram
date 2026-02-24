package com.soloist.telegram

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers

@Suppress("unused")
@Transactional
@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseTestClass {

	companion object {
		private val network = Network.newNetwork()

		private val postgresContainer = PostgreSQLContainer("postgres:16.2")
			.apply {
				withDatabaseName("soloist_test")
				withUsername("test")
				withPassword("test")
				withNetwork(network)
				withNetworkAliases("postgres")
			}

		private val redisContainer = GenericContainer("redis:7.2.4")
			.apply {
				withExposedPorts(6379)
				withNetwork(network)
				withNetworkAliases("redis")
			}

		init {
			postgresContainer.start()
			redisContainer.start()
		}

		@JvmStatic
		@DynamicPropertySource
		fun configureProperties(registry: DynamicPropertyRegistry) {
			registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
			registry.add("spring.datasource.username", postgresContainer::getUsername)
			registry.add("spring.datasource.password", postgresContainer::getPassword)

			registry.add("spring.data.redis.host", redisContainer::getHost)
			registry.add("spring.data.redis.port") {
				redisContainer.getMappedPort(6379).toString()
			}
		}
	}
}
