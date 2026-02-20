package com.sleepkqq.sololeveling.telegram.bot.callback.impl

import com.sleepkqq.sololeveling.telegram.bot.callback.Callback
import com.sleepkqq.sololeveling.telegram.bot.grpc.client.PlayerApi
import com.sleepkqq.sololeveling.telegram.bot.service.localization.impl.PhotoSource
import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageFactory
import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageSender
import com.sleepkqq.sololeveling.telegram.bot.service.user.impl.UserInfoService
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.callback.CallbackAction
import com.sleepkqq.sololeveling.telegram.image.Image
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.player.ResetPlayerConfirmationState
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import java.util.Locale

@Component
class ResetPlayerConfirmCallback(
	private val userSessionService: UserSessionService,
	private val playerApi: PlayerApi,
	private val userInfoService: UserInfoService,
	private val telegramMessageFactory: TelegramMessageFactory,
	private val telegramMessageSender: TelegramMessageSender
) : Callback {

	override val action: CallbackAction = CallbackAction.RESET_PLAYER_CONFIRM

	private val log = LoggerFactory.getLogger(javaClass)

	@PreAuthorize("hasAuthority('ADMIN')")
	override fun handle(callbackQuery: CallbackQuery, session: UserSession): BotApiMethod<*>? {
		val adminId = callbackQuery.from.id
		val adminUsername = callbackQuery.from.userName ?: "unknown"
		val messageId = callbackQuery.message.messageId

		log.info("Admin reset confirmation received from adminId=$adminId (@$adminUsername)")

		val state = session.state() as? ResetPlayerConfirmationState
			?: return telegramMessageFactory.deleteMessage(adminId, messageId)

		val playerId = state.id

		log.info("Starting player reset: playerId=$playerId, requestedBy=adminId:$adminId (@$adminUsername)")

		playerApi.resetPlayer(playerId)

		log.info("Successfully reset player: playerId=$playerId, executedBy=adminId:$adminId (@$adminUsername)")

		val additionalInfo = userInfoService.getUserAdditionalInfo(playerId)
		val playerLocale = Locale.forLanguageTag(additionalInfo.locale.tag)

		val sendPhoto = telegramMessageFactory.sendPhoto(
			chatId = playerId,
			source = PhotoSource.Resource(Image.RESET_PLAYER),
			code = LocalizationCode.INFO_PLAYER_RESET,
			locale = playerLocale
		)
		telegramMessageSender.send(sendPhoto)

		userSessionService.idleState(adminId)

		return telegramMessageFactory.editMessageText(
			chatId = adminId,
			messageId = messageId,
			localized = session.state().onExitLocalized()!!
		)
	}
}
