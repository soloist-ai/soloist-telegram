package com.sleepkqq.sololeveling.telegram.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

	@GetMapping
	fun health(): ResponseEntity<Void> = ResponseEntity.ok().build()
}