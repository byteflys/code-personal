package x.spring.hello.repository

import org.springframework.data.repository.CrudRepository
import x.spring.hello.entity.UserInfo

interface UserRepository : CrudRepository<UserInfo, String> {

    fun findByName(name: String): UserInfo?
}