package com.sleepkqq.sololeveling.telegram.bot.config.grpc

import com.sleepkqq.sololeveling.proto.config.DefaultGrpcClientConfig
import com.sleepkqq.sololeveling.proto.config.interceptor.LocaleClientInterceptor
import com.sleepkqq.sololeveling.proto.user.UserServiceGrpc
import io.grpc.ClientInterceptor
import io.grpc.ManagedChannel
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Suppress("unused")
@Configuration
@EnableConfigurationProperties(GrpcPlayerServiceProperties::class)
class GrpcConfigClient(
	private val properties: GrpcPlayerServiceProperties
) : DefaultGrpcClientConfig() {

	@Bean
	fun playerManagedChannel(): ManagedChannel = createManagedChannel(properties)

	@Bean
	fun localeClientInterceptor(): ClientInterceptor = LocaleClientInterceptor()

	@Bean
	fun userServiceBlockingStub(): UserServiceGrpc.UserServiceBlockingStub =
		UserServiceGrpc.newBlockingStub(playerManagedChannel())
			.withInterceptors(localeClientInterceptor())
}
