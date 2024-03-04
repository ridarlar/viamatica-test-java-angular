package com.consulti.api.controller;

import com.consulti.api.dto.CreateUserDto;
import com.consulti.api.dto.EditUserDto;
import com.consulti.api.model.User;
import com.consulti.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping(path="/user")
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @CrossOrigin("*")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAllUser() {
        System.out.println("User list all");
        return this.userService.findAllUsers();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<User> findAllUsersPaged(Pageable pageable) {
        System.out.println("Paged User list");
        return this.userService.findAllUsersPaged(pageable);
    }

    @CrossOrigin("*")
    @GetMapping("/userName/{userName}")
    @ResponseStatus(HttpStatus.OK)
    public User findUserByUserName(@PathVariable String userName) {
        return this.userService.findUserByUserName(userName);
    }

    @CrossOrigin("*")
    @GetMapping("/dateOfBirth/{dateOfBirth}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findUserByDateOfBirth(@PathVariable String dateOfBirth) {
        return this.userService.findUsersByDateOfBirth(dateOfBirth);
    }

    @CrossOrigin("*")
    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public User signUp(@RequestBody CreateUserDto body)
    {
        return this.userService.addNewUser(body);
    }


    @CrossOrigin("*")
    @PutMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public User updateUser(@RequestBody EditUserDto body) {
        return userService.updateUserByBody(body);
    }

    @CrossOrigin("*")
    @DeleteMapping("/{userName}")
    public String deleteUer(@PathVariable String userName){
        return this.userService.removeUser(userName);
    }
}
