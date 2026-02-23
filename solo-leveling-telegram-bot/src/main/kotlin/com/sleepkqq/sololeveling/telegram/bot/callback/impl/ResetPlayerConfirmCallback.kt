package com.sleepkqq.sololeveling.telegram.bot.callback.impl

import com.sleepkqq.sololeveling.telegram.bot.annotation.TelegramCallback
import com.sleepkqq.sololeveling.telegram.bot.callback.Callback
import com.sleepkqq.sololeveling.telegram.bot.grpc.client.PlayerApi
import com.sleepkqq.sololeveling.telegram.bot.service.localization.impl.PhotoSource
import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageFactory
import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageSender
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.bot.service.user.impl.UserInfoService
import com.sleepkqq.sololeveling.telegram.callback.CallbackData
import com.sleepkqq.sololeveling.telegram.image.Image
import com.sleepkqq.sololeveling.telegram.localization.ErrorCode
import com.sleepkqq.sololeveling.telegram.localization.InfoCode
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.player.ResetPlayerConfirmationState
import io.grpc.Status
import io.grpc.StatusRuntimeException
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import java.util.*

@TelegramCallback(CallbackData.RESET_PLAYER_CONFIRM)
class ResetPlayerConfirmCallback(
	private val userSessionService: UserSessionService,
	private val playerApi: PlayerApi,
	private val userInfoService: UserInfoService,
	private val telegramMessageFactory: TelegramMessageFactory,
	private val telegramMessageSender: TelegramMessageSender
) : Callback {

	private val log = LoggerFactory.getLogger(javaClass)

	override fun handle(callbackQuery: CallbackQuery, session: UserSession): BotApiMethod<*>? {
		val userId = callbackQuery.from.id
		val messageId = callbackQuery.message.messageId

		val state = session.state() as? ResetPlayerConfirmationState
			?: return telegramMessageFactory.deleteMessage(userId, messageId)

		return executeReset(userId, messageId, state)
	}

	private fun executeReset(
		userId: Long,
		messageId: Int,
		state: ResetPlayerConfirmationState
	): BotApiMethod<*>? {
		val resetUserId = state.id
		log.info("Resetting player={}, requestedBy={}", resetUserId, userId)

		try {
			playerApi.resetPlayer(resetUserId)

		} catch (e: StatusRuntimeException) {
			if (e.status.code == Status.Code.NOT_FOUND) {
				userSessionService.idleState(userId)

				return telegramMessageFactory.editMessageText(
					chatId = userId,
					messageId = messageId,
					code = ErrorCode.USER_NOT_FOUND,
					params = listOf(resetUserId)
				)
			}

			return null
		}

		log.info("Successfully reset player={}", resetUserId)

		notifyResetPlayer(resetUserId)
		userSessionService.idleState(userId)

		return telegramMessageFactory.editMessageText(
			chatId = userId,
			messageId = messageId,
			localized = state.onExitLocalized()!!
		)
	}

	private fun notifyResetPlayer(resetUserId: Long) {
		val tag = userInfoService.getUserAdditionalInfo(resetUserId).locale.tag
		telegramMessageSender.send(
			telegramMessageFactory.sendPhoto(
				chatId = resetUserId,
				source = PhotoSource.Resource(Image.RESET_PLAYER),
				code = InfoCode.PLAYER_RESET,
				locale = Locale.forLanguageTag(tag)
			)
		)
	}
}
