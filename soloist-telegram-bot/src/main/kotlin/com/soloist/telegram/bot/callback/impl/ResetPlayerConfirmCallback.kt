package com.soloist.telegram.bot.callback.impl

import com.soloist.telegram.bot.annotation.TelegramCallback
import com.soloist.telegram.bot.callback.Callback
import com.soloist.telegram.bot.grpc.client.PlayerApi
import com.soloist.telegram.bot.service.localization.impl.PhotoSource
import com.soloist.telegram.bot.service.message.TelegramMessageFactory
import com.soloist.telegram.bot.service.message.TelegramMessageSender
import com.soloist.telegram.bot.service.user.UserSessionService
import com.soloist.telegram.bot.service.user.impl.UserInfoService
import com.soloist.telegram.callback.CallbackData
import com.soloist.telegram.image.Image
import com.soloist.telegram.localization.ErrorCode
import com.soloist.telegram.localization.InfoCode
import com.soloist.telegram.model.entity.user.UserSession
import com.soloist.telegram.model.entity.user.state.player.ResetPlayerConfirmationState
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
