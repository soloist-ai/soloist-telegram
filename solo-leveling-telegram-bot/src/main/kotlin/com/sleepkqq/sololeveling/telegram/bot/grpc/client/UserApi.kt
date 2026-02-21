package com.sleepkqq.sololeveling.telegram.bot.grpc.client

import com.google.protobuf.Empty
import com.sleepkqq.sololeveling.proto.player.RequestPaging
import com.sleepkqq.sololeveling.proto.user.GetUserAdditionalInfoResponse
import com.sleepkqq.sololeveling.proto.user.GetUserRequest
import com.sleepkqq.sololeveling.proto.user.GetUserResponse
import com.sleepkqq.sololeveling.proto.user.GetUsersRequest
import com.sleepkqq.sololeveling.proto.user.GetUsersResponse
import com.sleepkqq.sololeveling.proto.user.GetUsersStatsResponse
import com.sleepkqq.sololeveling.proto.user.UserServiceGrpc.UserServiceBlockingStub
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
