package jp.co.taka521.sandbox.service.impl

import jp.co.taka521.sandbox.entity.User
import jp.co.taka521.sandbox.repository.UserRepository
import jp.co.taka521.sandbox.service.UserService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    @Qualifier("jdbcScheduler") private val scheduler: Scheduler): UserService {

    @Transactional(readOnly = true)
    override fun findAll(): Mono<List<User>> = async { this.userRepository.findAll() }

    @Transactional(readOnly = true)
    override fun findById(id: Long): Mono<User> = async { this.userRepository.findById(id).orElse(User.empty) }

    @Transactional
    override fun update(user: User): Mono<User> = async { this.userRepository.save(user) }

    @Transactional
    override fun delete(id: Long): Mono<Unit> = async { userRepository.deleteById(id) }

    private fun <T> async(callable: () -> T): Mono<T> {
        return Mono.fromCallable(callable).publishOn(scheduler)
    }

}