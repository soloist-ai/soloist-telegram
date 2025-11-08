package com.sleepkqq.sololeveling.telegram.bot.service.auth

import com.sleepkqq.sololeveling.telegram.bot.grpc.client.UserGrpcApi
import com.sleepkqq.sololeveling.telegram.bot.service.redis.LocaleCacheService
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class AuthService(
	private val localeCacheService: LocaleCacheService,
	private val userGrpcApi: UserGrpcApi
) {

	fun login(update: Update) {
		val userId = extractUserId(update)
		if (userId != null) {
			val user = User("$userId", "", emptyList())
			val auth = UsernamePasswordAuthenticationToken(user, user.password, user.authorities)
			SecurityContextHolder.getContext().authentication = auth

			localeCacheService.getOrCacheLocale(userId)
			{ userGrpcApi.getUserLocale(userId) }
				.let { LocaleContextHolder.setLocale(it) }
		}
	}

	private fun extractUserId(update: Update): Long? = when {
		update.hasMessage() -> update.message?.from?.id
		update.hasCallbackQuery() -> update.callbackQuery?.from?.id
		update.hasInlineQuery() -> update.inlineQuery?.from?.id
		update.hasEditedMessage() -> update.editedMessage?.from?.id
		update.hasChannelPost() -> update.channelPost?.from?.id
		update.hasEditedChannelPost() -> update.editedChannelPost?.from?.id
		update.hasMyChatMember() -> update.myChatMember?.from?.id
		update.hasChatMember() -> update.chatMember?.from?.id
		else -> null
	}

	fun getCurrentUser(): User? =
		SecurityContextHolder.getContext().authentication.principal as? User
}