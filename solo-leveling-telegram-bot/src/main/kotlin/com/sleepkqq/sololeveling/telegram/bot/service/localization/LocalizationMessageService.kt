package com.sleepkqq.sololeveling.telegram.bot.service.localization

import com.sleepkqq.sololeveling.telegram.model.entity.localziation.LocalizedMessage
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.newsletter.LocalizedMessageDto

interface LocalizationMessageService {

	fun initialize(dto: LocalizedMessageDto): LocalizedMessage
}