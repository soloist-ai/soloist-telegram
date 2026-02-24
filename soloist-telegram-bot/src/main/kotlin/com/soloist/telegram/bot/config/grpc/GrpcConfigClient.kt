package com.soloist.telegram.bot.config.grpc

import com.soloist.proto.config.DefaultGrpcClientConfig
import com.soloist.proto.config.interceptor.UserClientInterceptor
import com.soloist.proto.player.PlayerServiceGrpc
import com.soloist.proto.user.UserServiceGrpc
import com.soloist.telegram.bot.config.properties.GrpcPlayerServiceProperties
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
