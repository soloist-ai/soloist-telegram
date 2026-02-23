package com.sleepkqq.sololeveling.telegram.bot.command

import com.sleepkqq.sololeveling.telegram.bot.annotation.TelegramCommand
import com.sleepkqq.sololeveling.telegram.localization.CommandDescriptionCode
import org.springframework.aop.support.AopUtils
import org.springframework.core.annotation.AnnotationUtils
import java.util.concurrent.ConcurrentHashMap

interface Command

private val commandValueCache = ConcurrentHashMap<Class<*>, String>()
private val commandDescriptionCache = ConcurrentHashMap<Class<*>, CommandDescriptionCode>()

fun Command.value(): String = AopUtils.getTargetClass(this).let {
	commandValueCache.getOrPut(it) {
		AnnotationUtils.findAnnotation(it, TelegramCommand::class.java)
			?.value
			?: error("${javaClass.simpleName} missing @TelegramCommand")
	}
}

fun Command.description(): CommandDescriptionCode = AopUtils.getTargetClass(this).let {
	commandDescriptionCache.getOrPut(it) {
		AnnotationUtils.findAnnotation(it, TelegramCommand::class.java)
			?.description
			?: error("${javaClass.simpleName} missing @TelegramCommand")
	}
}
