package com.sleepkqq.sololeveling.telegram.bot.command

import com.sleepkqq.sololeveling.telegram.bot.service.auth.RoleProtected
import com.sleepkqq.sololeveling.telegram.localization.CommandDescriptionCode

interface Command : RoleProtected {

	val command: String

	val description: CommandDescriptionCode
}
