package com.sleepkqq.sololeveling.telegram.bot.service.image

import com.sleepkqq.sololeveling.telegram.image.Image
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class ImageResourceService {

	fun getPhotoStream(image: Image): InputStream = ClassPathResource(image.filePath).inputStream

	fun getPhotoBytes(image: Image): ByteArray = getPhotoStream(image).readBytes()
}