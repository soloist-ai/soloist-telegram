package com.sleepkqq.sololeveling.telegram.bot.condition

import com.sleepkqq.sololeveling.telegram.bot.extensions.toKebabCase
import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata

abstract class TelegramHandlerCondition(
	private val prefix: String,
	private val annotationClassName: String
) : Condition {

	override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
		val attrs = metadata.getAnnotationAttributes(annotationClassName) ?: return true
		val value = resolveValue(attrs["value"]) ?: return true
		return context.environment.getProperty("$prefix.$value.enabled", Boolean::class.java, true)
	}

	private fun resolveValue(raw: Any?): String? = when (raw) {
		is String -> raw.toKebabCase()
		is Array<*> -> (raw.getOrNull(1) as? String)?.toKebabCase()
		else -> null
	}
}


