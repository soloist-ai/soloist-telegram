package com.soloist.telegram.bot.config.grpc

import com.soloist.proto.admin.AdminServiceGrpc
import com.soloist.proto.admin.AdminServiceGrpc.AdminServiceBlockingStub
import com.soloist.proto.config.GrpcChannelFactory
import com.soloist.proto.media.MediaServiceGrpc
import com.soloist.proto.media.MediaServiceGrpc.MediaServiceBlockingStub
import com.soloist.proto.task.TaskServiceGrpc
import com.soloist.proto.task.TaskServiceGrpc.TaskServiceBlockingStub
import com.soloist.proto.user.UserServiceGrpc
import com.soloist.proto.user.UserServiceGrpc.UserServiceBlockingStub
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcStubConfig(private val grpc: GrpcChannelFactory) {

	@Bean
	fun userStub(): UserServiceBlockingStub =
		grpc.stub("player") { UserServiceGrpc.newBlockingStub(it) }

	@Bean
	fun adminStub(): AdminServiceBlockingStub =
		grpc.stub("player") { AdminServiceGrpc.newBlockingStub(it) }

	@Bean
	fun taskStub(): TaskServiceBlockingStub =
		grpc.stub("player") { TaskServiceGrpc.newBlockingStub(it) }

	@Bean
	fun mediaStub(): MediaServiceBlockingStub =
		grpc.stub("media") { MediaServiceGrpc.newBlockingStub(it) }
}
