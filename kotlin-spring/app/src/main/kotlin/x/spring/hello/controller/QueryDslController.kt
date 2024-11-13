package x.spring.hello.controller

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import x.kotlin.commons.serialize.JSON.toJsonOrNull
import x.spring.hello.entity.QUserInfo
import x.spring.hello.entity.UserInfo

@RestController
class QueryDslController {

    @Autowired
    private lateinit var queryFactory: JPAQueryFactory

    @GetMapping("/jpa/dsl")
    fun dsl(): String {
        val target = QUserInfo.userInfo
        val condition = target.name.eq("b")
        val query = queryFactory.selectFrom<UserInfo>(target)
        val user = query.where(condition).fetchOne()
        return user.toJsonOrNull().orEmpty()
    }
}