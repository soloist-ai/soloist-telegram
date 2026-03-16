package com.soloist.telegram.bot.client

import com.soloist.proto.user.GetUserRequest
import com.soloist.proto.user.UserServiceGrpc.UserServiceBlockingStub
import com.soloist.proto.user.UserView
import org.springframework.stereotype.Service

@Service
class UserClient(
	private val userStub: UserServiceBlockingStub
) {

	fun getUser(id: Long): UserView =
		userStub.getUser(GetUserRequest.newBuilder().setUserId(id).build()).user
}
