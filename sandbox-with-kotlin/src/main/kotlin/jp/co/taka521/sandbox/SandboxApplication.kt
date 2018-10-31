package jp.co.taka521.sandbox

import jp.co.taka521.sandbox.entity.User
import jp.co.taka521.sandbox.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class SandboxApplication {

    companion object {
        private val logger = LoggerFactory.getLogger(SandboxApplication::class.java)
    }

    @Bean
    fun runner(userRepository: UserRepository): CommandLineRunner = CommandLineRunner {

        fun registerUsers(): List<User> {
            val password = "password"
            return listOf(User(name = "john", password = password, email = "john@email.com"),
                          User(name = "bob", password = password, email = "bob@email.com"))
        }

        userRepository.saveAll(registerUsers())

        logger.info(userRepository.findAll().toString())
    }
}

fun main(args: Array<String>) {
    runApplication<SandboxApplication>(*args)
}

