package com.creytech.agregadorinvestimentos.entity;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

//Criando essa classe java corretamente, o hibernate cria a tabela automaticamente
//  ao se iniciar o projeto

@Entity //Indica que essa entidade usuário, deve ser mapeada em uma tabela no banco de dados
@Table(name = "tb_users") //Indica a tabela do banco de dados em que a entidade será mapeada
public class User {

    @Id //Indica que esse atributo da classe é um identificador do banco de dados
    @GeneratedValue(strategy = GenerationType.UUID) //Faz com que esse identificador seja gerada de forma automática
    private UUID userId;

    @Column(name="username") //Indica a coluna que essa informação deve ser colocada na tabela
    private String username;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="creation_timestamp")    
    private Instant creationTimestamp;

    @PrePersist //Executa sempre antes de inserir
    public void onPrePersist() {
        this.creationTimestamp = Instant.now();
    }
    
    @Column(name="update_timestamp") 
    private Instant updateTimestamp;

    @PreUpdate //Executa sempre antes de atualizar
    public void onPreUpdate() {
        this.creationTimestamp = Instant.now();
    }

    @OneToMany(mappedBy = "user") //Um usuário pode ter várias contas
    private List<Account> accounts;

    public User() {}

    public User(String username, String email, String password, Instant creationTimestamp, Instant updateTimestamp) {
        //this.userId = userId; não precisamos inserir isso pois o próprio banco de dados insere pra gente.
        this.username = username;
        this.email = email;
        this.password = password;
        this.creationTimestamp = creationTimestamp;
        this.updateTimestamp = updateTimestamp;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public Instant getUpdateTimestamp() {
        return updateTimestamp;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreationTimestamp(Instant timestamp) {
        this.creationTimestamp = timestamp;
    }

    public void setUpdateTimestamp(Instant timestamp) {
        this.updateTimestamp = timestamp;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
