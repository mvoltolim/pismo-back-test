# Pismo Back Test 3.0
## Description:
I implemented the classes: **CrudController** and **CrudServiceImpl** with the standard methods of CRUD operations, as shown in the **CrudService** interface below:
* CrudService:
 ``` java
public interface CrudService<I, D> {

	D create(D dto);

	D update(D dto);

	Optional<D> findById(I id);

	Page<D> findAll(Pageable pageable);

	boolean deleteById(I id);

}
```

Therefore, to create a new entity it is only necessary to implement a new Repository and the controller and the service extending from the respective abstract classes and all CRUD operations will be automatically available, as in the examples:
* AccountRepository:
``` java
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {}
```
* AccountController:
``` java
@RequestMapping(Constants.API_V_1 + "/accounts")
@RestController
public class AccountController extends CrudController<AccountService, Long, AccountDto> {

	public AccountController(AccountService service) {
		super(service);
	}

}
```
* AccountService:
``` java
@Slf4j
@Service
public class AccountService extends CrudServiceImpl<AccountRepository, Account, Long, AccountDto> {

	public AccountService(AccountRepository repository, ModelMapper modelMapper) {
		super(repository, Account.class, AccountDto.class, modelMapper);
	}

}
```

## Validations:
The **javax.validation.constraints** are in the DTOs to be captured in the controller layer, thus avoiding reaching the persistence layer for them to be validated.
It is also possible to customize a group validation:
* PersistableDto:
``` java
@Getter
@Setter
public abstract class PersistableDto<I> implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "ID entity")
	@JsonView(JsonViews.Create.class)
	@Null(groups = ValidationGroups.Create.class)
	@NotNull(groups = ValidationGroups.Update.class)
	private I id;

}
```
* CrudController:
``` java
public abstract class CrudController<S extends CrudService<I, D>, I, D> {

	protected final S service;

	public CrudController(@NonNull S service) {
		this.service = service;
	}

	@JsonView(JsonViews.Create.class)
	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<D> create(@Validated({Default.class, ValidationGroups.Create.class}) @RequestBody D dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
	}

	@PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<D> update(@Validated({Default.class, ValidationGroups.Update.class}) @RequestBody D dto) {
		return ResponseEntity.ok(service.update(dto));
	}
}
```

## Exceptions:
All exceptions are caught and handled in **DefaultControllerAdvice** in order to return a friendly and restfull-compliant response.

## Run it:
* Import this project into your IDE as a MAVEN project, preferably intelliJ;
* After all dependencies are downloaded, run the tests;
* Finally, run the Application, using the **PismoApplication** class, by default it will start on port 8080.
* Test the endpoints using the Swagger interface at: http://localhost:8080/swagger-ui/
  or use any Client-Rest of your choice, such as: Postman or Insomnia

## Deploy:
* Heroku:
  * https://pismo-back-test.herokuapp.com/swagger-ui/
* Artefatos:
  * https://github.com/orgs/mvoltolim/packages?repo_name=pismo-back-test

## Technologies:
* JAVA 11
* SpringBoot 2.4.0
* ModelMapper (org.modelmapper) 2.3.8
* H2 1.4.200 (simplification for testing and demonstration)
* JUnit 5