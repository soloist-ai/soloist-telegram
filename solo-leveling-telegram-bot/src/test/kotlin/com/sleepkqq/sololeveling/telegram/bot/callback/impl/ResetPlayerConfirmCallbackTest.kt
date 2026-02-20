package com.sleepkqq.sololeveling.telegram.bot.callback.impl

import com.sleepkqq.sololeveling.telegram.bot.grpc.client.PlayerApi
import com.sleepkqq.sololeveling.telegram.bot.service.localization.impl.PhotoSource
import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageFactory
import com.sleepkqq.sololeveling.telegram.bot.service.message.TelegramMessageSender
import com.sleepkqq.sololeveling.telegram.bot.service.user.impl.UserInfoService
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserSessionService
import com.sleepkqq.sololeveling.telegram.callback.CallbackAction
import com.sleepkqq.sololeveling.telegram.image.Image
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import com.sleepkqq.sololeveling.telegram.localization.Localized
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserAdditionalInfo
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserLocale
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.player.ResetPlayerConfirmationState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.message.Message
import java.util.*

class ResetPlayerConfirmCallbackTest {

	private lateinit var userSessionService: UserSessionService
	private lateinit var playerApi: PlayerApi
	private lateinit var userInfoService: UserInfoService
	private lateinit var telegramMessageFactory: TelegramMessageFactory
	private lateinit var telegramMessageSender: TelegramMessageSender
	private lateinit var callback: ResetPlayerConfirmCallback

	@BeforeEach
	fun setUp() {
		userSessionService = mock()
		playerApi = mock()
		userInfoService = mock()
		telegramMessageFactory = mock()
		telegramMessageSender = mock()
		callback = ResetPlayerConfirmCallback(
			userSessionService,
			playerApi,
			userInfoService,
			telegramMessageFactory,
			telegramMessageSender
		)
	}

	@Test
	fun `action should return RESET_PLAYER_CONFIRM`() {
		// When/Then
		assertThat(callback.action).isEqualTo(CallbackAction.RESET_PLAYER_CONFIRM)
	}

	@Test
	fun `handle should reset player and send notifications`() {
		// Given
		val adminId = 123456L
		val adminUsername = "admin_user"
		val playerId = 789L
		val messageId = 111
		val playerLocale = Locale.ENGLISH

		val callbackQuery = mock<CallbackQuery>()
		val user = mock<User>()
		val message = mock<Message>()
		val session = mock<UserSession>()
		val state = mock<ResetPlayerConfirmationState>()
		val botState = mock<BotSessionState>()
		val localized = mock<Localized>()
		val additionalInfo = mock<UserAdditionalInfo>()
		val userLocale = mock<UserLocale>()
		val sendPhoto = mock<SendPhoto>()
		val expectedResult = mock<EditMessageText>()

		whenever(callbackQuery.from).thenReturn(user)
		whenever(user.id).thenReturn(adminId)
		whenever(user.userName).thenReturn(adminUsername)
		whenever(callbackQuery.message).thenReturn(message)
		whenever(message.messageId).thenReturn(messageId)
		whenever(session.state()).thenReturn(state).thenReturn(botState)
		whenever(state.id).thenReturn(playerId)
		whenever(botState.onExitLocalized()).thenReturn(localized)
		whenever(userInfoService.getUserAdditionalInfo(playerId)).thenReturn(additionalInfo)
		whenever(additionalInfo.locale).thenReturn(userLocale)
		whenever(userLocale.tag).thenReturn(playerLocale.toLanguageTag())
		whenever(telegramMessageFactory.sendPhoto(
			chatId = playerId,
			source = any<PhotoSource.Resource>(),
			code = LocalizationCode.INFO_PLAYER_RESET,
			locale = playerLocale
		)).thenReturn(sendPhoto)
		whenever(telegramMessageFactory.editMessageText(
			chatId = adminId,
			messageId = messageId,
			localized = localized
		)).thenReturn(expectedResult)

		// When
		val result = callback.handle(callbackQuery, session)

		// Then
		assertThat(result).isEqualTo(expectedResult)
		verify(playerApi).resetPlayer(playerId)
		verify(userSessionService).idleState(adminId)
		verify(telegramMessageSender).send(sendPhoto)
		verify(telegramMessageFactory).sendPhoto(
			chatId = playerId,
			source = any<PhotoSource.Resource>(),
			code = LocalizationCode.INFO_PLAYER_RESET,
			locale = playerLocale
		)
	}

	@Test
	fun `handle should return deleteMessage when state is not ResetPlayerConfirmationState`() {
		// Given
		val adminId = 123456L
		val messageId = 111

		val callbackQuery = mock<CallbackQuery>()
		val user = mock<User>()
		val message = mock<Message>()
		val session = mock<UserSession>()
		val expectedResult = mock<EditMessageText>()

		whenever(callbackQuery.from).thenReturn(user)
		whenever(user.id).thenReturn(adminId)
		whenever(user.userName).thenReturn("admin")
		whenever(callbackQuery.message).thenReturn(message)
		whenever(message.messageId).thenReturn(messageId)
		whenever(session.state()).thenReturn(mock()) // Wrong state type
		whenever(telegramMessageFactory.deleteMessage(adminId, messageId)).thenReturn(expectedResult)

		// When
		val result = callback.handle(callbackQuery, session)

		// Then
		assertThat(result).isEqualTo(expectedResult)
		verify(telegramMessageFactory).deleteMessage(adminId, messageId)
		verify(playerApi, never()).resetPlayer(any())
		verify(userSessionService, never()).idleState(any())
	}

	@Test
	fun `handle should handle admin without username`() {
		// Given
		val adminId = 123456L
		val playerId = 789L
		val messageId = 111
		val playerLocale = Locale.ENGLISH

		val callbackQuery = mock<CallbackQuery>()
		val user = mock<User>()
		val message = mock<Message>()
		val session = mock<UserSession>()
		val state = mock<ResetPlayerConfirmationState>()
		val botState = mock<BotSessionState>()
		val localized = mock<Localized>()
		val additionalInfo = mock<UserAdditionalInfo>()
		val userLocale = mock<UserLocale>()

		whenever(callbackQuery.from).thenReturn(user)
		whenever(user.id).thenReturn(adminId)
		whenever(user.userName).thenReturn(null) // No username
		whenever(callbackQuery.message).thenReturn(message)
		whenever(message.messageId).thenReturn(messageId)
		whenever(session.state()).thenReturn(state).thenReturn(botState)
		whenever(state.id).thenReturn(playerId)
		whenever(botState.onExitLocalized()).thenReturn(localized)
		whenever(userInfoService.getUserAdditionalInfo(playerId)).thenReturn(additionalInfo)
		whenever(additionalInfo.locale).thenReturn(userLocale)
		whenever(userLocale.tag).thenReturn(playerLocale.toLanguageTag())
		whenever(telegramMessageFactory.sendPhoto(any(), any<PhotoSource.Resource>(), any(), any())).thenReturn(mock())
		whenever(telegramMessageFactory.editMessageText(any(), any(), any())).thenReturn(mock())

		// When
		callback.handle(callbackQuery, session)

		// Then - should use "unknown" as default username
		verify(playerApi).resetPlayer(playerId)
	}

	@Test
	fun `handle should use correct image for reset notification`() {
		// Given
		val adminId = 123456L
		val playerId = 789L
		val messageId = 111
		val playerLocale = Locale.ENGLISH

		val callbackQuery = mock<CallbackQuery>()
		val user = mock<User>()
		val message = mock<Message>()
		val session = mock<UserSession>()
		val state = mock<ResetPlayerConfirmationState>()
		val botState = mock<BotSessionState>()
		val localized = mock<Localized>()
		val additionalInfo = mock<UserAdditionalInfo>()
		val userLocale = mock<UserLocale>()

		whenever(callbackQuery.from).thenReturn(user)
		whenever(user.id).thenReturn(adminId)
		whenever(user.userName).thenReturn("admin")
		whenever(callbackQuery.message).thenReturn(message)
		whenever(message.messageId).thenReturn(messageId)
		whenever(session.state()).thenReturn(state).thenReturn(botState)
		whenever(state.id).thenReturn(playerId)
		whenever(botState.onExitLocalized()).thenReturn(localized)
		whenever(userInfoService.getUserAdditionalInfo(playerId)).thenReturn(additionalInfo)
		whenever(additionalInfo.locale).thenReturn(userLocale)
		whenever(userLocale.tag).thenReturn(playerLocale.toLanguageTag())
		whenever(telegramMessageFactory.sendPhoto(any(), any<PhotoSource.Resource>(), any(), any())).thenReturn(mock())
		whenever(telegramMessageFactory.editMessageText(any(), any(), any())).thenReturn(mock())

		// When
		callback.handle(callbackQuery, session)

		// Then
		verify(telegramMessageFactory).sendPhoto(
			chatId = playerId,
			source = argThat { this is PhotoSource.Resource && this.image == Image.RESET_PLAYER },
			code = LocalizationCode.INFO_PLAYER_RESET,
			locale = playerLocale
		)
	}
}