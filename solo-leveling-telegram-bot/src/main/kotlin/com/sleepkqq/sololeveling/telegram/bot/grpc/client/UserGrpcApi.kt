package com.sleepkqq.sololeveling.telegram.bot.grpc.client

import com.sleepkqq.sololeveling.proto.user.GetUserLocaleRequest
import com.sleepkqq.sololeveling.proto.user.UserServiceGrpc.UserServiceBlockingStub
import org.springframework.stereotype.Service
import java.util.Locale

@Service
class UserGrpcApi(
	private val userStub: UserServiceBlockingStub
) {

	fun getUserLocale(userId: Long) =
		userStub.getUserLocale(GetUserLocaleRequest.newBuilder().setUserId(userId).build())
			.locale
			.let { Locale.forLanguageTag(it) }
}
