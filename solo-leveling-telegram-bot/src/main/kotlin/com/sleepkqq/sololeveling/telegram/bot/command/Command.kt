package com.sleepkqq.sololeveling.telegram.bot.command

import com.sleepkqq.sololeveling.telegram.bot.annotation.TelegramCommand
import com.sleepkqq.sololeveling.telegram.localization.CommandDescriptionCode
import org.springframework.aop.support.AopUtils
import org.springframework.core.annotation.AnnotationUtils
import java.util.concurrent.ConcurrentHashMap

interface Command

private val commandValueCache = ConcurrentHashMap<Class<*>, String>()
private val commandDescriptionCache = ConcurrentHashMap<Class<*>, CommandDescriptionCode>()

fun Command.value(): String =
	commandValueCache.getOrPut(AopUtils.getTargetClass(this)) {
		AnnotationUtils.findAnnotation(AopUtils.getTargetClass(this), TelegramCommand::class.java)
			?.value
			?: error("${javaClass.simpleName} missing @TelegramCommand")
	}

fun Command.description(): CommandDescriptionCode =
	commandDescriptionCache.getOrPut(AopUtils.getTargetClass(this)) {
		AnnotationUtils.findAnnotation(AopUtils.getTargetClass(this), TelegramCommand::class.java)
			?.description
			?: error("${javaClass.simpleName} missing @TelegramCommand")
	}
