package jp.co.taka521.sandbox.service

import jp.co.taka521.sandbox.entity.User

interface UserService {

    fun findAll(): List<User>

    fun findById(id: Long): User?

    fun update(user: User): User

    fun delete(id: Long)
}