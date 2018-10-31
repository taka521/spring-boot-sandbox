package jp.co.taka521.sandbox.service.impl

import jp.co.taka521.sandbox.entity.User
import jp.co.taka521.sandbox.repository.UserRepository
import jp.co.taka521.sandbox.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    @Transactional(readOnly = true)
    override fun findAll(): List<User> = userRepository.findAll()

    @Transactional(readOnly = true)
    override fun findById(id: Long): User? = userRepository.findById(id).get()

    @Transactional
    override fun update(user: User): User = userRepository.save(user)

    @Transactional
    override fun delete(id: Long): Unit = userRepository.deleteById(id)

}