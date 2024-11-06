package x.spring.hello.redis

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisOptions {

    @Autowired
    private lateinit var stringOptions: StringRedisTemplate

    fun putString(key: String, value: String) {
        stringOptions.opsForValue().set(key, value)
    }

    fun putString(key: String, value: String, duration: Long) {
        val expired = System.currentTimeMillis() + duration
        stringOptions.opsForValue().set(key, value, expired)
    }

    fun getString(key: String, default: String = ""): String {
        return stringOptions.opsForValue().get(key) ?: default
    }
}