package com.soloist.telegram.bot.grpc.client

import com.google.protobuf.Empty
import com.soloist.proto.player.RequestPaging
import com.soloist.proto.user.GetUserAdditionalInfoResponse
import com.soloist.proto.user.GetUserRequest
import com.soloist.proto.user.GetUserResponse
import com.soloist.proto.user.GetUsersRequest
import com.soloist.proto.user.GetUsersResponse
import com.soloist.proto.user.GetUsersStatsResponse
import com.soloist.proto.user.UserServiceGrpc.UserServiceBlockingStub
import org.springframework.stereotype.Service

@Service
class UserApi(
	private val userStub: UserServiceBlockingStub
) {

	fun getUser(id: Long): GetUserResponse =
		userStub.getUser(GetUserRequest.newBuilder().setUserId(id).build())

	fun getUserAdditionalInfo(): GetUserAdditionalInfoResponse =
		userStub.getUserAdditionalInfo(Empty.newBuilder().build())

	fun getUsersStats(): GetUsersStatsResponse =
		userStub.getUsersStats(Empty.newBuilder().build())

	fun getUsers(page: Int, pageSize: Int): GetUsersResponse =
		userStub.getUsers(
			GetUsersRequest.newBuilder()
				.setPaging(RequestPaging.newBuilder().setPage(page).setPageSize(pageSize))
				.build()
		)
}
