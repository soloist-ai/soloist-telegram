package com.sleepkqq.sololeveling.telegram.bot.service.localization

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service

@Service
class I18nService(
	private val messageSource: MessageSource
) {

	fun getMessage(key: String, vararg args: Any?): String = messageSource.getMessage(
		key,
		if (args.isEmpty()) null else args,
		LocaleContextHolder.getLocale()
	)
}
