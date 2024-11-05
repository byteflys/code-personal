package x.spring.hello

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController {

    @GetMapping("/index")
    fun index(): String {
        return "Hello World!"
    }
}