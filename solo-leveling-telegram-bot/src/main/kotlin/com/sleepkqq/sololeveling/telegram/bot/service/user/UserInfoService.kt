package com.sleepkqq.sololeveling.telegram.bot.service.user

import com.sleepkqq.sololeveling.proto.user.GetUserAdditionalInfoResponse
import com.sleepkqq.sololeveling.proto.user.UserLocale
import com.sleepkqq.sololeveling.proto.user.UserRole
import com.sleepkqq.sololeveling.telegram.bot.grpc.client.UserApi
import io.grpc.Status
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
		maxAttempts = 3
	)
	fun getUserAdditionalInfo(
		userId: Long,
		telegramLocaleTag: String? = null
	): GetUserAdditionalInfoResponse {
		return try {

			userApi.getUserAdditionalInfo()

		} catch (ex: StatusRuntimeException) {
			when (ex.status.code) {
				Status.Code.NOT_FOUND -> {
					log.info("User not found in user-api, using Telegram locale for userId={}", userId)
					additionalInfoResponse(telegramLocaleTag)
				}

				Status.Code.UNAVAILABLE -> {
					log.warn("User-api is UNAVAILABLE, using Telegram locale fallback without retries")
					additionalInfoResponse(telegramLocaleTag)
				}

				else -> {
					throw ex
				}
			}
		}
	}

	@Recover
	fun recoverUserAdditionalInfo(
		ex: StatusRuntimeException,
		userId: Long,
		telegramLocaleTag: String?
	): GetUserAdditionalInfoResponse {
		log.warn("Failed to fetch user info for userId={} after retries, using defaults", userId, ex)
		return additionalInfoResponse(telegramLocaleTag)
	}

	private fun additionalInfoResponse(
		localeTag: String? = Locale.ENGLISH.language
	): GetUserAdditionalInfoResponse =
		GetUserAdditionalInfoResponse.newBuilder()
			.addRoles(UserRole.USER)
			.setLocale(
				UserLocale.newBuilder()
					.setTag(localeTag)
					.setIsManual(false)
			)
			.build()

}
