package x.spring.hello.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import x.spring.hello.repository.UserRepository
import x.kotlin.commons.serialize.JSON.toJsonOrNull

@RestController
class JPAController {

    @Autowired
    private lateinit var userRepository: UserRepository

    @GetMapping("/jpa")
    fun jpa(): String {
        val user = userRepository.findByName("b")
        return user.toJsonOrNull("null").orEmpty()
    }
}