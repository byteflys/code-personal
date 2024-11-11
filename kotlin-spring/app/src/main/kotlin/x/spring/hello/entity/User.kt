package x.spring.hello.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: String

    var name: String = ""
}
