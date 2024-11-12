package x.spring.hello.repository

import org.springframework.data.repository.CrudRepository
import x.spring.hello.entity.User

interface UserRepository : CrudRepository<User, String> {

    fun findByName(name: String): User?
}