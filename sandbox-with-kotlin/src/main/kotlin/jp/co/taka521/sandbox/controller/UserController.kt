package jp.co.taka521.sandbox.controller

import jp.co.taka521.sandbox.entity.User
import jp.co.taka521.sandbox.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["user"])
class UserController(private val userService: UserService) {

    @GetMapping("/")
    fun get(): List<User> = userService.findAll()

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: Long): User? = userService.findById(id)

    @PutMapping("/add")
    fun put(@RequestBody user: User) = userService.update(user)

    @PostMapping("/update")
    fun update(@RequestBody user: User) = userService.update(user)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long) = userService.delete(id)
}