package com.sleepkqq.sololeveling.telegram

import org.babyfish.jimmer.spring.repository.EnableJimmerRepositories
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableJimmerRepositories("com.sleepkqq.sololeveling.telegram.model.repository")
@SpringBootApplication(scanBasePackages = ["com.sleepkqq.sololeveling.telegram"])
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
