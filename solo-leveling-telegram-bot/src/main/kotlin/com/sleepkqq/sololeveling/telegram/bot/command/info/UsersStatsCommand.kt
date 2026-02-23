package com.sleepkqq.sololeveling.telegram.bot.command.info

import com.sleepkqq.sololeveling.telegram.bot.annotation.TelegramCommand
import com.sleepkqq.sololeveling.telegram.bot.command.info.InfoCommand.InfoCommandResult
import com.sleepkqq.sololeveling.telegram.bot.grpc.client.UserApi
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserFeedbackService
import com.sleepkqq.sololeveling.telegram.localization.CommandCode
import com.sleepkqq.sololeveling.telegram.localization.CommandDescriptionCode
import org.telegram.telegrambots.meta.api.objects.message.Message

@TelegramCommand("users_stats", CommandDescriptionCode.USERS_STATS)
class UsersStatsCommand(
	private val userApi: UserApi,
	private val userFeedbackService: UserFeedbackService
) : InfoCommand {

	override fun handle(message: Message): InfoCommandResult {
		val usersStats = userApi.getUsersStats()

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