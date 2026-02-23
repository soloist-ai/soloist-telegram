package com.sleepkqq.sololeveling.telegram.keyboard

import com.sleepkqq.sololeveling.telegram.localization.ButtonCode

private const val SUCCESS_EMOJI_ID = "5206607081334906820"
private const val CANCEL_EMOJI_ID = "5210952531676504517"
private const val LIGHTNING_EMOJI_ID = "5456140674028019486"

enum class Button(
	val localizationCode: ButtonCode,
	val customEmojiId: String? = null,
	val style: ButtonStyle? = null
) {
	CONFIRM(ButtonCode.CONFIRM, SUCCESS_EMOJI_ID, ButtonStyle.SUCCESS),
	CANCEL(ButtonCode.CANCEL, CANCEL_EMOJI_ID, ButtonStyle.DANGER),
	MINI_APP_LINK(ButtonCode.MINI_APP_LINK, LIGHTNING_EMOJI_ID, ButtonStyle.PRIMARY)
}
