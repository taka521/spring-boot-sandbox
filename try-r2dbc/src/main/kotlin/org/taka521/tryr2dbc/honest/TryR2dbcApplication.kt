package org.taka521.tryr2dbc.honest

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.annotation.Id
import org.springframework.data.r2dbc.dialect.Database
import org.springframework.data.r2dbc.function.DatabaseClient
import org.springframework.data.r2dbc.function.DefaultReactiveDataAccessStrategy
import org.springframework.data.r2dbc.function.TransactionalDatabaseClient
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.lang.NonNull
import org.springframework.stereotype.Component
import reactor.core.publisher.*
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers
import javax.annotation.PostConstruct


fun main(args: Array<String>) {
    runApplication<TryR2dbcApplication>(*args)
}

@SpringBootApplication
class TryR2dbcApplication

@Component
class DataLoader(
    private val coffeeRepository: CoffeeRepository,
    private val databaseClient: DatabaseClient,
    private val txDatabaseClient: TransactionalDatabaseClient
) {

    @PostConstruct
    fun load() {

        // JPAを利用したCRUD操作
        val useJpa = this.coffeeRepository.deleteAll()
            .thenMany(Flux.just("ブルーマウンテン", "コロンビア", "モカ"))
            .map { name -> Coffee(name = name) }
            .flatMap { coffee -> this.coffeeRepository.save(coffee) }
            .thenMany(this.coffeeRepository.findAll())

        // DatabaseClientを利用したCRUD操作
        val useDbClient =
            this.txDatabaseClient.inTransaction { db -> db.execute().sql("DELETE FROM coffee").then() }
                .thenMany(Flux.just(Coffee(name = "クリスタルマウンテン")))
                .flatMap { coffee -> this.databaseClient.insert().into(coffee.javaClass).using(coffee).then() }
                .thenMany(Flux.just("キリマンジャロ", "マンデリン"))
                .flatMap { name ->
                    this.databaseClient.execute().sql("INSERT INTO coffee(name) VALUES ($1)").bind("$1", name)
                        .then()
                }
                .thenMany(this.databaseClient.select().from(Coffee::class.java).fetch().all())

        // Publisherを合成して、subscribe
        useJpa.concatWith(useDbClient).subscribe { coffee -> logger.info(coffee.toString()) }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DataLoader::class.java)
    }
}

/**
 * ## R2DBC用のDB設定クラス
 *
 * R2DBCを利用するために必要なBeanの登録を、自前で行います。
 * 登録する必要のあるBeanは以下の通り。
 *
 * * [ConnectionFactory]
 * * [DatabaseClient]
 * * [R2dbcRepositoryFactory]
 * * 利用するリポジトリインターフェース
 *
 */
@Configuration
class DatabaseConfiguration {

    /**
     * 接続先の製品に応じた、[ConnectionFactory]を生成して登録します。
     * 今回はPostgreSQLを想定し、[PostgresqlConnectionFactory]を返しています。
     *
     * @return 接続先の製品に応じた、[ConnectionFactory]
     */
    @Bean
    fun connectionFactory(): PostgresqlConnectionFactory = PostgresqlConnectionFactory(
        PostgresqlConnectionConfiguration.builder()
            .host("localhost")
            .database("postgres")
            .username("postgres")
            .password("password")
            .build()
    )

    /**
     * コンテナに登録してある[ConnectionFactory]を利用して、[DatabaseClient]を生成・登録します。
     * JPAなどを利用しない場合は、基本的にこの[DatabaseClient]が提供するAPIを用いてCRUD操作を行います。
     *
     * @return データベースクライアント
     */
    @Bean
    fun databaseClient(connectionFactory: ConnectionFactory): DatabaseClient =
        DatabaseClient.builder()
            .connectionFactory(connectionFactory)
            .build()

    @Bean
    fun transactionalDatabaseClient(connectionFactory: ConnectionFactory): TransactionalDatabaseClient =
        TransactionalDatabaseClient.builder()
            .connectionFactory(connectionFactory)
            .build()

    /**
     * JPAを利用するために[R2dbcRepositoryFactory]を登録します。
     * ここで登録した[R2dbcRepositoryFactory]を利用し、リポジトリインターフェースから実態を生成します。
     *
     * @return リポジトリファクトリ
     */
    @Bean
    fun repositoryFactory(databaseClient: DatabaseClient,
                          connectionFactory: ConnectionFactory): R2dbcRepositoryFactory {
        val context = RelationalMappingContext()
        context.afterPropertiesSet() // Todo: おまじないっぽいけど、わからんので後で調べる

        /**
         * ストラテジ
         * * 直接[Database.POSTGRES.defaultDialect]を指定することも可能
         */
        fun strategy() = DefaultReactiveDataAccessStrategy(
            Database.findDatabase(connectionFactory)
                .orElseThrow { UnsupportedOperationException("指定されたDBはサポートしていません") }
                .defaultDialect()
        )

        return R2dbcRepositoryFactory(databaseClient, context, strategy())
    }

    /**
     * コンテナに登録されている[R2dbcRepositoryFactory]を利用して、[CoffeeRepository]を生成・登録します。
     *
     * @return コーヒーリポジトリ
     */
    @Bean
    fun coffeeRepository(repositoryFactory: R2dbcRepositoryFactory) =
        repositoryFactory.getRepository(CoffeeRepository::class.java)


    @Bean
    fun scheduler(): Scheduler = Schedulers.single()
}

interface CoffeeRepository: ReactiveCrudRepository<Coffee, Long>

data class Coffee(@Id val id: Long? = null, @NonNull val name: String)
