package com.soloist.telegram.bot.config

import com.soloist.telegram.bot.config.properties.TelegramBotProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.meta.generics.TelegramClient

@Configuration
class TelegramClientConfig {

	@Bean
	fun telegramClient(telegramBotProperties: TelegramBotProperties): TelegramClient =
		OkHttpTelegramClient(telegramBotProperties.token)
}
