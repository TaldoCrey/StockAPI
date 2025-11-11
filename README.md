# Entendendo como funciona uma API Spring-Boot

Vamos entender como funciona a API de estudos criada com o auxilio de um vídeo no youtube.

# Estrutura genérica de uma API Spring-Boot

## Componentes

* ## Controller
    - Parte da API responsável por mapear e rotear as requisições, enviando as requests para que um **serviço** a processe e enviando a resposta para o cliente.
    - Costura a request com o processamento dela.

    ```java
    @RestController
    @RequestMapping("/rota/")

    public class Controller {

        private Service service; //Dependency Injection

        public Controller(Service service) {
            this.service = service;
        }

        @PostMapping("/endpoint/{var}")
        public ResponseEntity<Void> doSomething(@PathVariable("var") String var) {
            //<...>

            return null;
        }

        @GetMapping("/endpoint")
        public ResponseEntity<Void> answerSomething(@RequestBody Dto dto) {
            //<...>

            return ResponseEntity.ok().build();
        }

    }
    ```

* ## Service
    - Parte da API responsável por processar os dados da requisição baseado na rota da request.
    - 'Backend verdadeiro'

    ```java
    @Service
    public class Service {

        private Repository rep; //Dependency Injection

        public Service(Repository rep) {
            this.rep = rep;
        }

        public void doSomething(Dto dto) {
            //<...>
        }
    }
    ```

* ## Entity
    - Parte da API responsável por informar, tanto o código em Java quanto o código SQL, como os elementos do banco dados devem ser criados: parametros, tipos, valores, etc.
    - Mapeia classes Java em Tabelas SQL

    ```java
    @Entity
    @Table(name="tablename")
    public class Entity {
        @Id //Indica que esse atributo da classe é o identificador da tabela
        @GeneratedValue(strategy GenerationType.Type) //Faz esse valor ser gerado automáticamente
        private Type id;

        @Column(name="name") //Define o nome da coluna que contém esse atributo
        private String name;

        @Column(name="timestamp")
        private Instante timestamp;

        @PrePersist //Define que essa função deve ser executada sempre antes de um insert
        public void onPrePersist() {
            this.timestamp = Instant.now();
        }

        @PreUpdate //Define que essa função deve ser executada sempre antes de um update
        public void onPreUpdate() {
            this.timestamp = Instant.now();
        }

        @OneToMany(mappedBy = "atribute") //Define uma relação com outra entidade, e define que essa relação está mapeada no atributo "atribute" na outra entidade.
        private Type atribute;

        public Entity() {} // Construtor vazio obrigatório

        public Entity(String name) {
            this.name = name;
            //Como o id e timestamp são gerado sautomáticamente, não precisamos deles no construtor.

            //Como atribute é um atributo que mapeia uma relação ele também não precisa ser mapeado pois quando definirmos a outra ponta da relação na outra entidade, ele será automáticamente mapeado.
            
        }
    }
    ```

* ## Repository
    - Parte da API responsável por integrar o código Java com o banco de dados SQL, permitindo o acesso e edição do banco de dados.
    - Utiliza entidades para manipular os dados da tabela, nela contida.

    ```java
    @Repository
    public interface Reporitory extends JpaRepository<Entity, id> {
        //O primeiro campo é a entidade que será mapeada para o banco de dados
        //O segundo campo é o tipo do identificador
    }
    ```

* ## DTO (Data Transfer Object)
    - Objeto da API reponsável por padronizar e facilitar a troca de dados entre diferentes partes do código.
    - Se uma entidade de 10 parâmetros, podemos usar um DTO para definir quais dos 10 parâmetros desejamos que trafeguem internamente e para fora da API.

    ```java
    public record CreateDto(ARGS) {}
    //Args devem ser os argumentos de uma classe que você deseja que sejam transmitidos ao DTO. Vale ressaltar que os argumentos do DTO **DEVEM** ter o mesmo nome dos atributos da classe.
    ```

## Conceitos

* ## Dependency Injection
    - Ao invés de um objeto criar suas próprias dependências, ele as recebe prontas.
    - Facilita o código pois cria-se cada dependência separadamente e depois apenas injetamos nos objetos em que elas serão usadas.

* ## Relações entre objetos
    * ### Many To Many
        - Quando um objeto se relaciona com outro na forma: vários objetos pais tem vários objetos filhos cada.

    * ### One To Many
        - Quando um objeto se relaciona com outro na forma: um objeto pai tem vários objetos filhos.
    
    * ### Many To One
        - Quando um objeto se relaciona com outro na forma: vários objetos pais tem apenas um objeto filho cada.

# Dissecando a API do Agregador de Investimentos

## EndPoints:
| Método | Endpoint | Descrição |
| :---      | :---     | :---      |
| `POST`     | `/v1/users` | Cria um novo usuário
| `GET`     | `/v1/users/{userId}` | Lista o usuário dono do `userId`
| `GET`     | `/v1/users` | Lista todos os usuários
| `PUT`     | `/v1/users/{userId}` | Edita o usuário dono do `userId`
| `DELETE`     | `/v1/users/{userId}` | Deleta o usuário dono do `userId`
| `POST`     | `/v1/users/{userId}/accounts` | Cria uma conta para o usuário dono do `userId`
| `GET`     | `/v1/users/{userId}/accounts` | Lista todas as contas do usuário dono do `userId`
| `POST`     | `/v1/stocks` | Cria uma nova ação
| `POST`     | `/v1/accounts/{accountId}/stocks` | Associa uma ação à conta dona do `accountId`
| `GET`     | `/v1/accounts/{accountId}/stocks` | Lista todas as ações da conta dona do `accountId`

## Controladores

### UserController.java
Controlador Rest responsável por gerenciar a rota `/v1/users`. Recebe como dependência injetada o serviço `UserService` para processar as requisições.

### StockController.java
Controlador Rest reponsável por gerenciar a rota `/v1/stocks`. Recebe como dependência injetada o serviço `StockService` para processar as requisições.

### AccountController.java
Controlador Rest reponsável por gerenciar a rota `/v1/accounts`. Recebe como dependência injetada o serviço `AccountService` para processar as requisições.

## Serviços

### UserService.java

Serviço responsável por processar as requests relacionadas a usuários. Possuí 3 dependências injetadas:
* `UserRepository`;
* `AccountRepository`;
* `BillingAddresRepository`;

ambas responsáveis por integrar as alterações feitas nas entidades às suas respectivas tabelas no banco de dados.

### StockService.java

Serviço responsável por processar as requests relacionadas a usuários. Possuí 1 dependência injetadas:
* `StockRepository`;

responsável por integrar as alterações feitas nas entidades às suas respectivas tabelas no banco de dados.

### AccountService.java

Serviço responsável por processar as requests relacionadas a usuários. Possuí 3 dependências injetadas:
* `AccountRepository`;
* `AccountStockRepository`;
* `StockRepository`;

ambas responsáveis por integrar as alterações feitas nas entidades às suas respectivas tabelas no banco de dados.

## Entidades

### Account

Entidade responsável por mapear a tabela `tb_accounts` do banco de dados. A tabela possuí 5 elementos:
* `accountId`;
* `description`;
* `user`;
* `billingAddress`;
* `accountStocks`;

que mapeiam 5 colunas no banco de dados.

Alguns elementos dessa entidade possuem relações especiais com outros elementos do sistema:
* `user`: ManyToOne;
* `billingAddress`: OneToOne;
* `accontStocks`: OneToMany;

o que garante certas ligações com as outras tabelas de outras entidades do sistema.

### AccountStock

Entidade responsável por mapear a tabela `tb_accounts_stocks` do banco de dados. A tabela possuí 4 elementos:
* `id`*;
* `account`;
* `stock`;
* `quantity`;

*: o elemento **id** é um elemento especial.

que mapeiam 4 colunas no banco de dados.

Alguns elementos dessa entidade possuem relações especiais com outros elementos do sistema:
* `id`: EmbeddedId;
* `stock`: ManyToOne;
* `account`: ManyToOne;

o que garante certas ligações com as outras tabelas de outras entidades do sistema.

Além disso, o elemento `id` é naverdade um identificador de tabela composto, definido pela entidade **AccountStockId**.

### AccountStockId

Essa entidade é uma entidade especial, visto que ela não mapeia uma tabela no banco de dados, mas serve como um identificador composto para a entidade **AccountStock**, formado a partir de dois elementos:
* `accountId`;
* `stockId`;

### BillingAddress

Entidade responsável por mapear a tabela `tb_billingaddress` do banco de dados. A tabela possuí 4 elementos:
* `id`;
* `account`;
* `street`;
* `number`;

que mapeiam 4 colunas no banco de dados.

Alguns elementos dessa entidade possuem relações especiais com outros elementos do sistema:
* `account`: OneToOne*;

o que garante certas ligações com as outras tabelas de outras entidades do sistema.

*: Possuí o parametro `cascade = CascadeType.ALL` indicando que toda ação feita no elemento `account` deve ser aplicada nessa entidade automáticamente.

### Stock

Entidade responsável por mapear a tabela `tb_stocks` do banco de dados. A tabela possuí 2 elementos:
* `stockId`;
* `description`;

que mapeiam 2 colunas no banco de dados.

Esta entidade não possuí nenhuma relação com outras entidades do sistema.

### User

Entidade responsável por mapear a tabela `tb_users` do banco de dados. A tabela possuí 7 elementos:
* `userId`;
* `username`;
* `email`;
* `password`;
* `creationTimestamp`;
* `updateTimestamp`;
* `accounts`;

que mapeiam 7 colunas no banco de dados.

Alguns elementos dessa entidade possuem relações especiais com outros elementos do sistema:
* `accounts`: OneToMany;

o que garante certas ligações com as outras tabelas de outras entidades do sistema.

## Repositórios

Para simplificar, todas as entidades tem um repositório para si, **com exceção da entidade `AccountStockId`**.

# Considerações finais
Este repositório foi criado com o intúito de estudar Java Spring Boot.

