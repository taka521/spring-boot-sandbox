package org.taka521.tryr2dbc.simplicity

import org.slf4j.LoggerFactory
import org.springframework.data.r2dbc.function.DatabaseClient
import org.springframework.data.r2dbc.function.TransactionalDatabaseClient
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import javax.annotation.PostConstruct

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