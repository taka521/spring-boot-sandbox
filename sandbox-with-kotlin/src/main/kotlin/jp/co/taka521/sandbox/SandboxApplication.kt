package jp.co.taka521.sandbox

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

//        for(i in 1..10000) {
//            val username = "test$i"
//            val password = "password"
//            val email = "$username@email.com"
//            userRepository.save(User(name = username, password = password, email = email))
//        }

        // logger.info(userRepository.findAll().toString())
    }
}

fun main(args: Array<String>) {
    runApplication<SandboxApplication>(*args)
}

