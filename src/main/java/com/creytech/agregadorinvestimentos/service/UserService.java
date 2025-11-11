package com.creytech.agregadorinvestimentos.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.creytech.agregadorinvestimentos.controller.dto.AccountResponseDto;
import com.creytech.agregadorinvestimentos.controller.dto.CreateAccountDto;
import com.creytech.agregadorinvestimentos.controller.dto.CreateUserDto;
import com.creytech.agregadorinvestimentos.controller.dto.UpdateUserDto;
import com.creytech.agregadorinvestimentos.entity.Account;
import com.creytech.agregadorinvestimentos.entity.AccountStock;
import com.creytech.agregadorinvestimentos.entity.BillingAddress;
import com.creytech.agregadorinvestimentos.entity.Stock;
import com.creytech.agregadorinvestimentos.entity.User;
import com.creytech.agregadorinvestimentos.repository.AccountRepository;
import com.creytech.agregadorinvestimentos.repository.BillingAddresRepository;
import com.creytech.agregadorinvestimentos.repository.UserRepository;

@Service //Indica que essa classe é um componente da aplicação
public class UserService {

    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private BillingAddresRepository billingAddresRepository;

    public UserService(UserRepository userRepository, AccountRepository accountRepository, BillingAddresRepository billingAddresRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddresRepository = billingAddresRepository;
    }

    public UUID createUser(CreateUserDto createUserDto) {

        var entity = new User(
            createUserDto.username(), 
            createUserDto.email(), 
            createUserDto.password(), 
            Instant.now(), 
            null
        );
        // DTO -> ENTITY antes de salvar
        var userSaved = userRepository.save(entity);

        return userSaved.getUserId();
    }

    public Optional<User> getUserById(String userId) {

        return userRepository.findById(UUID.fromString(userId));

    } 

    public List<User> listUsers() {
        
        return userRepository.findAll();

    }

    public void updateUserById(String userId, UpdateUserDto updateUserDto) {
        var id = UUID.fromString(userId);
        var userEntity = userRepository.findById(id);

        if (userEntity.isPresent()) {
            var user = userEntity.get();
            if (updateUserDto.username() != null) {
                user.setUsername(updateUserDto.username());
            }

            if (updateUserDto.password() != null) {
                user.setPassword(updateUserDto.password());
            }

            userRepository.save(user);
        }
    }

    public void deleteById(String userId) {
        var id = UUID.fromString(userId);
        var userExists = userRepository.existsById(id);
        if (userExists) {
            userRepository.deleteById(id);
        }
    } 

    public void createAccount(String userId, CreateAccountDto createAccountDto) {
        var user = userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    
        //DTO -> ENTITY
        var account = new Account(
            createAccountDto.description(),
            user, 
            new ArrayList<AccountStock>()
        );

        var accountCreated = accountRepository.save(account);

        var billingAddress = new BillingAddress(
            account,
            createAccountDto.street(),
            createAccountDto.number()
        );

        billingAddresRepository.save(billingAddress);

    }

    public List<AccountResponseDto> listAccounts(String userId) {
        var user = userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return user.getAccounts()
                .stream()
                .map(ac -> new AccountResponseDto(ac.getAccountId().toString(), ac.getDescription()))
                .toList();

    }
}   
