package com.soloist.telegram.bot.command.info

import com.soloist.telegram.bot.annotation.TelegramCommand
import com.soloist.telegram.bot.command.info.InfoCommand.InfoCommandResult
import com.soloist.telegram.bot.client.UserClient
import com.soloist.telegram.bot.service.user.UserFeedbackService
import com.soloist.telegram.localization.CommandCode
import com.soloist.telegram.localization.CommandDescriptionCode
import org.telegram.telegrambots.meta.api.objects.message.Message

@TelegramCommand("users_stats", CommandDescriptionCode.USERS_STATS)
class UsersStatsCommand(
	private val userClient: UserClient,
	private val userFeedbackService: UserFeedbackService
) : InfoCommand {

	override fun handle(message: Message): InfoCommandResult {
		val usersStats = userClient.getUsersStats()

		val feedbackCount = userFeedbackService.getUserFeedbackCount()

		val params = listOf(
			usersStats.total,
			usersStats.returning,
			usersStats.todayTotal,
			usersStats.todayReturning,
			usersStats.todayNew,
			usersStats.weekTotal,
			usersStats.weekReturning,
			usersStats.weekNew,
			usersStats.monthTotal,
			usersStats.monthReturning,
			usersStats.monthNew,
			feedbackCount.userCount,
			feedbackCount.feedbackCount,
		)

		return InfoCommandResult(CommandCode.USERS_STATS, params)
	}
}