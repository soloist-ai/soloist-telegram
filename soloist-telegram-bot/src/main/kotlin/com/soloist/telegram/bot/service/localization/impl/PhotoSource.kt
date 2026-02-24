package com.soloist.telegram.bot.service.localization.impl

import com.soloist.telegram.image.Image

sealed class PhotoSource {
	data class FileId(val id: String) : PhotoSource()
	data class Resource(val image: Image) : PhotoSource()
}
