package jp.co.taka521.sandbox.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

import java.util.concurrent.Executors

@Configuration
class SchedulerConfiguration @Autowired
constructor(
    @param:Value("\${spring.datasource.hikari.maximum-pool-size}") private val connectionPoolSize: Int) {

    @Bean
    fun jdbcScheduler(): Scheduler {
        return Schedulers.fromExecutor(Executors.newFixedThreadPool(this.connectionPoolSize))
    }
}
