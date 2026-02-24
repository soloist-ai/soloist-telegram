package com.soloist.telegram.bot.service.localization.impl

import com.soloist.telegram.bot.service.localization.LocalizationMessageService
import com.soloist.telegram.model.entity.Immutables
import com.soloist.telegram.model.entity.localization.LocalizedMessage
import com.soloist.telegram.model.entity.user.state.newsletter.LocalizedMessageDto
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class LocalizationMessageServiceImpl : LocalizationMessageService {

	override fun initialize(dto: LocalizedMessageDto): LocalizedMessage =
		Immutables.createLocalizedMessage {
			it.setId(UUID.randomUUID())
				.setLocale(dto.locale)
				.setText(dto.text)
		}
}