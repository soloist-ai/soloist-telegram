package com.soloist.telegram.bot.service.auth

import com.soloist.config.interceptor.UserContextHolder
import com.soloist.telegram.bot.callback.Callback
import com.soloist.telegram.bot.callback.value
import com.soloist.telegram.bot.command.Command
import com.soloist.telegram.bot.command.value
import com.soloist.telegram.bot.config.properties.TelegramBotProperties
import com.soloist.telegram.bot.extensions.toKebabCase
import com.soloist.telegram.bot.mapper.ProtoMapper
import com.soloist.telegram.bot.model.UserRole
import com.soloist.telegram.bot.service.user.impl.UserInfoService
import org.slf4j.LoggerFactory
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User as TgUser

@Service
class AuthService(
	private val userInfoService: UserInfoService,
	private val protoMapper: ProtoMapper,
	private val roleHierarchy: RoleHierarchy,
	private val telegramBotProperties: TelegramBotProperties
) {

	private val log = LoggerFactory.getLogger(javaClass)

	fun login(update: Update) {
		val telegramUser = extractTelegramUser(update) ?: run {
			log.warn("Auth skipped: cannot extract Telegram user from update: {}", update.updateId)
			return
		}

		val userId = telegramUser.id
		UserContextHolder.setUserId(userId)

		val additionalUserInfo = userInfoService.getUserAdditionalInfo(
			userId,
			telegramUser.languageCode
		)

		val roles = protoMapper.map(additionalUserInfo.rolesList)
		val locale = protoMapper.map(additionalUserInfo.locale)

		val user = User("$userId", "", roles)
		val auth = UsernamePasswordAuthenticationToken(user, user.password, user.authorities)
		SecurityContextHolder.getContext().authentication = auth
		LocaleContextHolder.setLocale(locale)
	}


	private fun extractTelegramUser(update: Update): TgUser? = when {
		update.hasMessage() -> update.message?.from
		update.hasCallbackQuery() -> update.callbackQuery?.from
		update.hasInlineQuery() -> update.inlineQuery?.from
		update.hasEditedMessage() -> update.editedMessage?.from
		update.hasChannelPost() -> update.channelPost?.from
		update.hasEditedChannelPost() -> update.editedChannelPost?.from
		update.hasMyChatMember() -> update.myChatMember?.from
		update.hasChatMember() -> update.chatMember?.from
		else -> null
	}

	fun getCurrentUser(): User? =
		SecurityContextHolder.getContext().authentication.principal as? User

	fun hasRole(role: UserRole): Boolean {
		val user = getCurrentUser() ?: return false
		return roleHierarchy
			.getReachableGrantedAuthorities(user.authorities)
			.any { it.authority == role.name }
	}

	fun hasAccess(command: Command, targetRole: UserRole? = null): Boolean {
		val role = telegramBotProperties.commands[command.value().toKebabCase()]
			?.role
			?: UserRole.USER
		return targetRole?.let { it == role } ?: hasRole(role)
	}

	fun hasAccess(callback: Callback, targetRole: UserRole? = null): Boolean {
		val commandText = callback.value()
		val role = telegramBotProperties.callbacks[commandText]
			?.role
			?: UserRole.USER
		return targetRole?.let { it == role } ?: hasRole(role)
	}
}
