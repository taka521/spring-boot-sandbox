package jp.co.taka521.sandbox.repository

import jp.co.taka521.sandbox.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>