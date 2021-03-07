simple-boot  [v1.0.0]
===========

애플리케이션 및 서비스를 최소의 요구 사항으로 생성할 수 있도록 도와줍니다.


## 주요기능
* 개발을 위해 획기적으로 빠르고 광범위하게 액세스할 수 있는 시작 경험을 제공합니다. 비지니스에만 집중하세요.
  * 객체관리 (생성, 라이프 사이클)
  * 디펜던시 인젝션
  * 등..
  
* 의존관계 설정으로만으로 추가 기능(modules)을 쉽고 빠르게 적용할수 있습니다.

## 설치 및 시작
참조 문서에는 자세한 설치 지침과 포괄적인 시작 가이드가 포함되어 있습니다.  
다음은 Java의 완전한 simple-boot 애플리케이션에 대한 간단한 티저입니다.
* version: >= java8


### example project
* 다음은 Java의 완전한 simple-boot 애플리케이션에 대한 간단한 web project 입니다.
  * gradle
    ```groovy
    dependencies {
        implementation project(':simple')
        implementation project(':simple-db')
        implementation project(':simple-db-h2')
        implementation project(':simple-db-hibernate')
        implementation project(':simple-web')
        implementation project(':simple-web-thymeleaf')
        implementation project(':simple-web-netty')
    }
    ```
  * java
    ```java
    public class ExampleApplication {
        public static void main(String[] args) {
            new SimpleApplication().run(ExampleApplication.class);
        }
    }
    ```
    ```java
    @Controller
    public class TodoController {
        private final AdminService adminService;
        @Injection
        public TodoController(AdminService adminService) {
            this.userService = userService;
        }
        @GetMapping("/admins")
        public List<Admin> admins(Request request, Response response){
            return adminService.admins();
        }
        @PostMapping("/admin")
        public Serializable saveAdmin(Request request, Response response) throws ProcessingException {
            Admin admin = request.body(Admin.class);
            return adminService.save(admin);
        }
        @GetMapping("/index")
        public View index(Request request, Response response){
            View view = new View("views/index.html");
            view.put("name", "visualkhh");
            return view;
        }
    }
    ```
    ```java
    @Service
    public class AdminService {
        private DatabaseAccessor databaseAccessor;
        @Injection
        public AdminService(DatabaseAccessor databaseAccessor) {
            this.databaseAccessor = databaseAccessor;
        }
        @Transactional
        public Serializable save(Admin admin) {
            return databaseAccessor.save(admin);
        }
        @Transactional
        public List<Admin> admins() {
            return databaseAccessor.resultList(Admin.class);
        }
    }
    ```

## Modules
* cores
  * simple [core]
* dbs
  * simple-db [core]
    * simple-db-h2 [<img src="https://www.h2database.com/html/images/h2-logo-2.png" height="10"/>](h2.png)
    * simple-db-hibernate [<img src="https://hibernate.org/images/hibernate-logo.svg" height="10"/>](hibernate.png)
* webs
  * simple-web [core]
    * simple-web-thymeleaf [<img src="https://www.thymeleaf.org/images/thymeleaf.png" height="10"/>](thymeleaf.png)
    * simple-web-netty [<img src="https://netty.io/images/logo.png" height="10"/>](netty.png)
```
┌─ cores ─────────────┐     ┌── dbs ─────────────────────────────────┐
│   ┌──────────────┐  │     │   ┌────────────┐    ┌──────────────┐   │
│   │    simple    ◄──┼─────┤   │ simple-db  ◄────┤ simple-db-h2 │   │
│   └──────▲───────┘  │     │   └──────▲─────┘    └──────────────┘   │
└──────────┼──────────┘     │          │                             │
           │                │   ┌──────┴──────────────────────┐      │
           │                │   │        * DatabaseAccessor   │      │
           │                │   │        * TransactionManager │      │
           │                │   │                             │      │
           │                │   │     simple-db-hibernate     │      │
           │                │   └─────────────────────────────┘      │
┌─ webs ───┴─────────┐      └────────────────────────────────────────┘
│   ┌────────────┐   └───────────────────────────────────────────────┐
│   │ simple-web ◄────────┐     ┌─────────────────────────────┐      │
│   └──────▲─────┘        │     │           * ViewResolver    │      │
│          │              │     │                             │      │
│          │              │     │     simple-web-thymeleaf    │      │
│          │              └─────┤     ...                     │      │
│   ┌──────┴─────────────────┐  └─────────────────────────────┘      │
│   │         * WebServer    │                                       │
│   │                        │                                       │
│   │    simple-web-netty    │                                       │
│   │    ...                 │                                       │
│   └────────────────────────┘                                       │
└────────────────────────────────────────────────────────────────────┘

-─ name ─- : group name 
    *      : implements 대상 interface
 ◄ ▲ ▼ ►   : dependency arrow
   ...     : more
```

## Modules 추가
- package 위치: com.simple.boot.{모듈이름}
- public class 모듈이름 extends Starter { ... }
- @PostConstruct Method쪽 구동 로직 구현.
  
  ```java
  @Config
  public class ModuleStarter extends Starter {
    private final ModuleConfig config;
    private final SimstanceManager simstanceManager;
    @Injection
    public ModuleStarter(SimstanceManager simstanceManager, ModuleConfig config) {
        this.simstanceManager = simstanceManager;
        this.config = config;
    }
    @PostConstruct
    public void init() {
      // something...
    }
  }
  ```
  ```java
  @Config
  public class ModuleConfig {
     // something... public Map<String, Object> property;
      @Injection
      public ModuleConfig(ConfigLoader configLoader) {
          configLoader.map("module", this);
      }
  }
  ```

## TODO Modules
* 같이 만들어 나갈 개발자분 참여가 필요합니다.


### 구현체 interface (Type 으로 구성필요한 항목) 
* simple-web
  * com.simple.boot.web.server.WebServer
    * 웹서버
  * com.simple.boot.web.view.ViewResolver
    * 뷰엔진

## 도움 받기
* 많은 참여가 필요합니다.

## 보고된 문제
* 많은 참여가 필요합니다.

## License
* simple-boot is Open Source software released under the Apache 2.0 license.
* visualkhh@gmail.com
