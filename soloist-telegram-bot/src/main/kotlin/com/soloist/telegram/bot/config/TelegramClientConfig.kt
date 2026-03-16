package com.soloist.telegram.bot.config

import com.soloist.telegram.bot.config.properties.TelegramBotProperties
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.meta.generics.TelegramClient
import java.time.Duration

@Configuration
class TelegramClientConfig {

	@Bean
	fun telegramClient(telegramBotProperties: TelegramBotProperties): TelegramClient {
		val httpClient = OkHttpClient.Builder()
			.connectTimeout(Duration.ofSeconds(30))
			.writeTimeout(Duration.ofMinutes(3))
			.readTimeout(Duration.ofMinutes(3))
			.build()
		return OkHttpTelegramClient(httpClient, telegramBotProperties.token)
	}
}
