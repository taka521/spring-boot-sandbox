package org.taka521.tryr2dbc.simplicity

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.function.TransactionalDatabaseClient
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

/**
 * ## R2DBC用のDB設定クラス
 *
 * * [AbstractR2dbcConfiguration]を継承して、最低限の設定だけでR2DBCを使用します。
 * * なお、[AbstractR2dbcConfiguration]を使用する場合には[EnableR2dbcRepositories]を注釈する必要があります。
 * * [AbstractR2dbcConfiguration.connectionFactory]をオーバーライドし、接続先のDBに応じた[ConnectionFactory]を返す関数を定義します。
 * * 今回はPostgreSQLに接続するため、[PostgresqlConnectionFactory]を使っています。
 *
 */
@Configuration
@EnableR2dbcRepositories // 必須
class DatabaseConfiguration: AbstractR2dbcConfiguration() {

    /**
     * ### [ConnectionFactory]を返すメソッド。
     *
     * * [AbstractR2dbcConfiguration]を継承する場合は、このメソッドだけオーバーライドすればよい。
     * * また、[AbstractR2dbcConfiguration.connectionFactory]をオーバーライドする場合は、[Bean]アノテーションを注釈する必要がある。
     *
     * @return 接続先のDBに応じた[ConnectionFactory]
     * @see [AbstractR2dbcConfiguration.connectionFactory]
     */
    @Bean
    override fun connectionFactory(): ConnectionFactory = PostgresqlConnectionFactory(
        PostgresqlConnectionConfiguration.builder()
            .host("localhost")
            .database("postgres")
            .username("postgres")
            .password("password")
            .build()
    )

    /**
     * ### TransactionalDatabaseClientを登録するメソッド
     *
     * [TransactionalDatabaseClient]は自前で登録する必要がある。
     *
     * @param connectionFactory コネクションファクトリ
     * @return [TransactionalDatabaseClient]のインスタンス
     */
    @Bean
    fun transactionalDatabaseClient(connectionFactory: ConnectionFactory): TransactionalDatabaseClient =
        TransactionalDatabaseClient.builder()
            .connectionFactory(connectionFactory)
            .build()
}