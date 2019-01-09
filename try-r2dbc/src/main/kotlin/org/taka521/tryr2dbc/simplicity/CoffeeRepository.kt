package org.taka521.tryr2dbc.simplicity

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

/**
 * ## Coffeeリポジトリ
 *
 * * リアクティブなリポジトリを使用する場合には、[ReactiveCrudRepository]を継承します。
 */
@Repository
interface CoffeeRepository: ReactiveCrudRepository<Coffee, Long>