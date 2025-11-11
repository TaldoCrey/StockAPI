package com.creytech.agregadorinvestimentos.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creytech.agregadorinvestimentos.controller.dto.AccountResponseDto;
import com.creytech.agregadorinvestimentos.controller.dto.CreateAccountDto;
import com.creytech.agregadorinvestimentos.controller.dto.CreateUserDto;
import com.creytech.agregadorinvestimentos.controller.dto.UpdateUserDto;
import com.creytech.agregadorinvestimentos.entity.Account;
import com.creytech.agregadorinvestimentos.entity.User;
import com.creytech.agregadorinvestimentos.service.UserService;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


//Porta de entrada do projeto, que chama a service, e a service chama o db

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping //Mapeia uma request POST
    public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto) {
        var userId = userService.createUser(createUserDto);
        return ResponseEntity.created(URI.create("/v1/users/" + userId.toString())).build();
    }
    
    @GetMapping("/{userId}") //Mapeia uma request GET
    public ResponseEntity<User> getUserById(@PathVariable("userId") String userId) { //Troca a variável do decorador pelo parametro da função
        var user = userService.getUserById(userId);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        var users = userService.listUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUserById(@PathVariable("userId") String userId, @RequestBody UpdateUserDto updateUserDto) {
        userService.updateUserById(userId, updateUserDto);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable("userId") String userId) {
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/accounts")
    public ResponseEntity<Void> createAccount(@PathVariable("userId") String userId, @RequestBody CreateAccountDto createAccountDto) {
        userService.createAccount(userId, createAccountDto);
        
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/accounts")
    public ResponseEntity<List<AccountResponseDto>> listAccounts(@PathVariable("userId") String userId) {
        var accounts = userService.listAccounts(userId);
        
        return ResponseEntity.ok(accounts);
    }
}
