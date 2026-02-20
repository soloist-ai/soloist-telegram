package com.sleepkqq.sololeveling.telegram.bot.config.properties

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
	TelegramBotProperties::class,
	TelegramRateLimitProperties::class,
	RedisCacheProperties::class,
	GrpcPlayerServiceProperties::class
)
class AppPropertiesConfig
