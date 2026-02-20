package com.sleepkqq.sololeveling.telegram.bot.service.localization.impl

import com.sleepkqq.sololeveling.telegram.image.Image

sealed class PhotoSource {
	data class FileId(val id: String) : PhotoSource()
	data class Resource(val image: Image) : PhotoSource()
}
