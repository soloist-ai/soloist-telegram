package com.sleepkqq.sololeveling.telegram.bot.config.grpc

import com.sleepkqq.sololeveling.proto.config.DefaultGrpcClientConfig
import com.sleepkqq.sololeveling.proto.config.interceptor.UserClientInterceptor
import com.sleepkqq.sololeveling.proto.player.PlayerServiceGrpc
import com.sleepkqq.sololeveling.proto.user.UserServiceGrpc
import com.sleepkqq.sololeveling.telegram.bot.config.properties.GrpcPlayerServiceProperties
import io.grpc.ClientInterceptor
import io.grpc.ManagedChannel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcConfigClient(
	properties: GrpcPlayerServiceProperties
) : DefaultGrpcClientConfig(properties) {

	@Bean
	fun playerManagedChannel(): ManagedChannel = createManagedChannel()

	@Bean
	fun userClientInterceptor(): ClientInterceptor = UserClientInterceptor()

	@Bean
	fun userServiceBlockingStub(): UserServiceGrpc.UserServiceBlockingStub =
		UserServiceGrpc.newBlockingStub(playerManagedChannel())
			.withInterceptors(userClientInterceptor())

	@Bean
	fun playerServiceBlockingStub(): PlayerServiceGrpc.PlayerServiceBlockingStub =
		PlayerServiceGrpc.newBlockingStub(playerManagedChannel())
			.withInterceptors(userClientInterceptor())
}
