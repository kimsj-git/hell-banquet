package com.hellsfood.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "uploadService", url = "http://localhost:8061/janban")
interface UploadServiceClient {

	@GetMapping("/hasJanbani")
	fun hasJanbani(@RequestParam userId: String, @RequestParam today: String): Boolean

}