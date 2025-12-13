package com.sleepkqq.sololeveling.telegram.bot.command.info

import com.sleepkqq.sololeveling.telegram.bot.command.info.InfoCommand.InfoCommandResult
import com.sleepkqq.sololeveling.telegram.bot.grpc.client.UserApi
import com.sleepkqq.sololeveling.telegram.bot.service.user.UserFeedbackService
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.message.Message

@Component
class UsersStatsCommand(
	private val userApi: UserApi,
	private val userFeedbackService: UserFeedbackService
) : InfoCommand {

	override val command: String = "/users_stats"

	override val forList: Boolean = false

	@PreAuthorize("hasAuthority('MANAGER')")
	override fun handle(message: Message): InfoCommandResult {
		val usersStats = userApi.getUsersStats()

		val feedbackCount = userFeedbackService.getUserFeedbackCount()

		val params = mapOf(
			"total" to usersStats.total,
			"returning" to usersStats.returning,
			"todayTotal" to usersStats.todayTotal,
			"todayReturning" to usersStats.todayReturning,
			"todayNew" to usersStats.todayNew,
			"weekTotal" to usersStats.weekTotal,
			"weekReturning" to usersStats.weekReturning,
			"weekNew" to usersStats.weekNew,
			"monthTotal" to usersStats.monthTotal,
			"monthReturning" to usersStats.monthReturning,
			"monthNew" to usersStats.monthNew,
			"userFeedbackCount" to feedbackCount.userCount,
			"feedbackCount" to feedbackCount.feedbackCount,
		)

		return InfoCommandResult(LocalizationCode.CMD_USERS_STATS, params)
	}
}