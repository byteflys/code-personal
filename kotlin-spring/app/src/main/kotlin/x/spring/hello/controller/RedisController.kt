package x.spring.hello.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import x.spring.hello.redis.RedisOptions
import java.util.UUID

@RestController
class RedisController {

    @Autowired
    private lateinit var redis: RedisOptions

    @GetMapping("/redis")
    fun redis(): String {
        redis.putString("redis.key", UUID.randomUUID().toString())
        val key = redis.getString("redis.key")
        return key
    }
}