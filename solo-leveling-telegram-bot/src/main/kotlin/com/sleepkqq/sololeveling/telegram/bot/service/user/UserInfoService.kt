package com.sleepkqq.sololeveling.telegram.bot.service.user

import com.sleepkqq.sololeveling.config.interceptor.UserContextHolder
import com.sleepkqq.sololeveling.proto.user.GetUserAdditionalInfoResponse
import com.sleepkqq.sololeveling.proto.user.UserLocale
import com.sleepkqq.sololeveling.proto.user.UserRole
import com.sleepkqq.sololeveling.telegram.bot.grpc.client.UserApi
import io.grpc.StatusRuntimeException
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import java.util.Locale

@Service
class UserInfoService(
	private val userApi: UserApi
) {

	private val log = LoggerFactory.getLogger(javaClass)

	@Cacheable(
		value = ["user-info"],
		key = "#userId",
		unless = "#result == null"
	)
	@Retryable(
		retryFor = [StatusRuntimeException::class],
		maxAttempts = 3,
		recover = "recoverUserAdditionalInfo"
	)
	fun getUserAdditionalInfo(): GetUserAdditionalInfoResponse {
		return userApi.getUserAdditionalInfo()
	}

	@Recover
	fun recoverUserAdditionalInfo(ex: StatusRuntimeException): GetUserAdditionalInfoResponse {
		log.warn(
			"Failed to fetch user info for userId={} after retries, using defaults",
			UserContextHolder.getUserId(), ex
		)

		return GetUserAdditionalInfoResponse.newBuilder()
			.addRoles(UserRole.USER)
			.setLocale(UserLocale.newBuilder().setTag(Locale.ENGLISH.language).setIsManual(false))
			.build()
	}
}
