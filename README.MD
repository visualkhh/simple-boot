simple-boot  [v1.0.0]
===========

애플리케이션 및 서비스를 최소의 요구 사항으로 생성할 수 있도록 도와줍니다.


주요기능:
* 개발을 위해 획기적으로 빠르고 광범위하게 액세스할 수 있는 시작 경험을 제공합니다.
  * 객체관리 (생성, 라이프)
  * 디펜던시 인젝션
  
* 의존관계 설정으로만으로 추가 기능을 쉽고 빠르게 적용할수 있습니다.

## 설치 및 시작
참조 문서에는 자세한 설치 지침과 포괄적인 시작 가이드가 포함되어 있습니다.  
다음은 Java의 완전한 Spring Boot 애플리케이션에 대한 간단한 티저입니다.


### example project
* 다음은 Java의 완전한 simple-boot 애플리케이션에 대한 간단한 web project입니다.
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
    * simple-db-h2
    * simple-db-hibernate
* webs
  * simple-web [core]
    * simple-web-thymeleaf
    * simple-web-netty
  

## Create Modules 기능 모듈 추가
- package 위치: com.simple.boot.{모듈이름}
  ```java
  @Config
  public class ModuleStarter extends Starter {
    private final ModuleConfig config;
    private final SimstanceManager simstanceManager;
    @Injection
    public HibernateStarter(SimstanceManager simstanceManager, ModuleConfig config) {
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
      public HibernaterConfig(ConfigLoader configLoader) {
          configLoader.map("module", this);
      }
  }

  ```
### 구현체 interface
* simple-web
  * com.simple.boot.web.server.WebServer
  * com.simple.boot.web.view.ViewResolver

## 도움 받기
* 많은 참여가 필요합니다.

## 보고된 문제
* 많은 참여가 필요합니다.

## License
* Spring Boot is Open Source software released under the Apache 2.0 license.
