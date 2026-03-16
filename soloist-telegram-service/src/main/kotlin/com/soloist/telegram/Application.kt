package com.soloist.telegram

import org.babyfish.jimmer.spring.repository.EnableJimmerRepositories
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@EnableJimmerRepositories("com.soloist.telegram.model.repository")
@ConfigurationPropertiesScan("com.soloist.telegram.bot.config.properties")
@SpringBootApplication(scanBasePackages = ["com.soloist.telegram"])
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
