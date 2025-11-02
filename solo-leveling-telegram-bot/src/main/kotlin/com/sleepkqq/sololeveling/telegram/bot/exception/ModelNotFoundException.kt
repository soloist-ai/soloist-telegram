package com.sleepkqq.sololeveling.telegram.bot.exception

import com.sleepkqq.sololeveling.telegram.model.entity.Model
import kotlin.reflect.KClass

class ModelNotFoundException(klass: KClass<out Model>, id: Any) :
	RuntimeException("${klass.simpleName} not found id=$id")
