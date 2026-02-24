package com.soloist.telegram.bot.service.localization

import com.soloist.telegram.model.entity.localization.LocalizedMessage
import com.soloist.telegram.model.entity.user.state.newsletter.LocalizedMessageDto

interface LocalizationMessageService {

	fun initialize(dto: LocalizedMessageDto): LocalizedMessage
}