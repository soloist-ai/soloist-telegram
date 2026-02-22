package com.sleepkqq.sololeveling.telegram.bot.mapper

import com.sleepkqq.sololeveling.proto.user.UserLocale
import com.sleepkqq.sololeveling.proto.user.UserRole
import org.mapstruct.*
import java.util.Locale

@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
	nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT,
	nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
abstract class ProtoMapper {

	fun map(input: UserRole): com.sleepkqq.sololeveling.telegram.bot.model.UserRole =
		com.sleepkqq.sololeveling.telegram.bot.model.UserRole.valueOf(input.name)

	abstract fun map(inputs: Collection<UserRole>):
			Collection<com.sleepkqq.sololeveling.telegram.bot.model.UserRole>

	fun map(input: UserLocale): Locale = Locale.forLanguageTag(input.tag)
}