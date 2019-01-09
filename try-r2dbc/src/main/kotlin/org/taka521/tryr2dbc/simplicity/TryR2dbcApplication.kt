package org.taka521.tryr2dbc.simplicity

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

/**
 * 簡易的にR2DBCを利用するサンプルアプリケーション
 */
@SpringBootApplication
class TryR2dbcApplication

fun main(args: Array<String>) {
    runApplication<TryR2dbcApplication>(*args)
}



