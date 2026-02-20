package com.sleepkqq.sololeveling.telegram.bot.config.properties

import com.sleepkqq.sololeveling.proto.config.GrpcServiceProperties
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.grpc.services.player")
data class GrpcPlayerServiceProperties(
	override val host: String,
	override val port: Int
) : GrpcServiceProperties