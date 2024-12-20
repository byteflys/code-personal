package x.spring.hello.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "user_info")
class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: String

    var name: String = ""
}
