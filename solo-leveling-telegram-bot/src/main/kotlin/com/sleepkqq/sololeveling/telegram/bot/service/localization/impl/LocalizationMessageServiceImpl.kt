package com.sleepkqq.sololeveling.telegram.bot.service.localization.impl

import com.sleepkqq.sololeveling.telegram.bot.service.localization.LocalizationMessageService
import com.sleepkqq.sololeveling.telegram.model.entity.Immutables
import com.sleepkqq.sololeveling.telegram.model.entity.localziation.LocalizedMessage
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.newsletter.LocalizedMessageDto
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