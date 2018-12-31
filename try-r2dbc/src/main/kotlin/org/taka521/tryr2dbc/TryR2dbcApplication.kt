package org.taka521.tryr2dbc

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.annotation.Id
import org.springframework.data.r2dbc.dialect.Dialect
import org.springframework.data.r2dbc.dialect.PostgresDialect
import org.springframework.data.r2dbc.function.DatabaseClient
import org.springframework.data.r2dbc.function.DefaultReactiveDataAccessStrategy
import org.springframework.data.r2dbc.function.ReactiveDataAccessStrategy
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.lang.NonNull
import org.springframework.stereotype.Component
import reactor.core.publisher.*
import javax.annotation.PostConstruct


fun main(args: Array<String>) {
    runApplication<TryR2dbcApplication>(*args)
}

@SpringBootApplication
class TryR2dbcApplication

@Component
class DataLoader(private val coffeeRepository: CoffeeRepository) {

    @PostConstruct
    fun load() {
        this.coffeeRepository.deleteAll()
            .thenMany(Flux.just("スターバックス・コーヒー", "ブルーボトルコーヒー", "ドトール・コーヒー"))
            .map { name -> Coffee(name = name) }
            .flatMap { coffee -> this.coffeeRepository.save(coffee) }
            .thenMany(coffeeRepository.findAll())
            .subscribe { coffee -> logger.info(coffee.toString()) }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DataLoader::class.java)
    }
}


@Configuration
class DatabaseConfiguration {

    @Bean
    fun connectionFactory(): PostgresqlConnectionFactory = PostgresqlConnectionFactory(
        PostgresqlConnectionConfiguration.builder()
            .host("localhost")
            .database("postgres")
            .username("postgres")
            .password("password")
            .build()
    )

    @Bean
    fun databaseClient(connectionFactory: ConnectionFactory): DatabaseClient =
        DatabaseClient.builder()
            .connectionFactory(connectionFactory)
            .build()


    @Bean
    fun repositoryFactory(databaseClient: DatabaseClient): R2dbcRepositoryFactory {
        val context = RelationalMappingContext()
        context.afterPropertiesSet()

        val strategy = DefaultReactiveDataAccessStrategy(PostgresDialect.INSTANCE)

        return R2dbcRepositoryFactory(databaseClient, context, strategy)
    }

    @Bean
    fun coffeeRepository(repositoryFactory: R2dbcRepositoryFactory) =
        repositoryFactory.getRepository(CoffeeRepository::class.java)
}

interface CoffeeRepository: ReactiveCrudRepository<Coffee, Long>

data class Coffee(@Id val id: Long? = null, @NonNull val name: String)
