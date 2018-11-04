package jp.co.taka521.sandbox.service

import jp.co.taka521.sandbox.entity.User
import reactor.core.publisher.Mono

interface UserService {

    fun findAll(): Mono<List<User>>

    fun findById(id: Long): Mono<User>

    fun update(user: User): Mono<User>

    fun delete(id: Long): Mono<Unit>
}