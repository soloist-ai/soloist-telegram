package com.soloist.telegram.bot.config.properties

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
	TelegramBotProperties::class,
	TelegramRateLimitProperties::class,
	TelegramRetryProperties::class,
	RedisCacheProperties::class,
	GrpcPlayerServiceProperties::class,
	ScheduledBroadcastTaskProperties::class
)
class AppPropertiesConfig
