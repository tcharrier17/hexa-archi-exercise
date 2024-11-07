package primary_adapters.rest.controllers

import com.google.inject.{Inject, Singleton}
import core.domain.models.UserEntity
import core.resources.Role
import core.services.TodosService
import play.api.libs.json._
import play.api.mvc._
import play.silhouette.api.{Authenticator, Silhouette}
import play.silhouette.impl.providers.BasicAuthProvider
import primary_adapters.rest.authentification.AuthService
import primary_adapters.rest.mapper.TodoJsonMapper._


//import lunatech.security.Role;
//import lunatech.entities.TodoEntity;
//import lunatech.entities.UserEntity;
//import lunatech.services.AuthService;
//import lunatech.services.UserService;
//import lunatech.services.UserService.UserFilters;

/**
 * CRUD for TodoEntity
 * NB: todos are stored in users
 * NB: Regular users are allowed to get/modify/delete their own todos
 * NB: Admin users are allowed to get/modify/delete every todos
 */
@Singleton
class TodosController @Inject()(val controllerComponents: ControllerComponents, val todosService: TodosService, val authService: AuthService) extends BaseController {

  val authError: Result = Results.Unauthorized.withHeaders("WWW-Authenticate" -> """Basic realm="Secured"""")

  def getAll(tagsFilter: Option[List[String]] = None, user: Option[String] = None): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val userInfos: UserEntity = userAuth(request.headers.get("Authorization").getOrElse(throw new Error("Unauthorized")))

    userInfos.role match {
      case Role.ADMIN => Ok(toJsonList(todosService.getTodos(tagsFilter, user))).as("application/json")
      case Role.REGULAR => Ok(toJsonList(todosService.getTodos(tagsFilter, Some(userInfos.username)))).as("application/json")
      case _ => authError
    }
  }

  def getById(id: String, user: Option[String] = None): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val userInfos: UserEntity = userAuth(request.headers.get("Authorization").getOrElse(throw new Error("Unauthorized")))
    userInfos.role match {
      case Role.ADMIN => Ok.apply(toJson(todosService.getTodo(id, user))).as("application/json")
      case Role.REGULAR => Ok.apply(toJson(todosService.getTodo(id, Some(userInfos.username)))).as("application/json")
      case _ => authError
    }
  }

  def deleteOne(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val userInfos: UserEntity = userAuth(request.headers.get("Authorization").getOrElse(throw new Error("Unauthorized")))

    if (todosService.deleteTodo(id, userInfos.username)) Ok("element supprimer") else Ok(new JsObject(Map("work" -> JsBoolean(false)))).as("application/json")
  }

  def createOne(): Action[JsValue] = Action(parse.json) { implicit request: Request[JsValue] =>
    // TODO : add feature to add the name off the owner directly in
    val userInfos: UserEntity = userAuth(request.headers.get("Authorization").getOrElse(throw new Error("Unauthorized")))
    val body = request.body.as[JsObject] + ("id" -> JsString(""))
    fromJson(body) match {
      case Some(todo) =>
        if (todosService.createTodo(todo, userInfos.username))
          Ok (JsObject(body.fields ++ Seq("create" -> JsBoolean(true)))).as("application/json")
        else
          Ok (JsObject(body.fields ++ Seq("create" -> JsBoolean(false)))).as("application/json")
      case None => BadRequest("Invalid object sent")
    }
  }

  def modifyOne(): Action[JsValue] = Action(parse.json) { implicit request: Request[JsValue] =>
    val userInfos: UserEntity = userAuth(request.headers.get("Authorization").getOrElse(throw new Error("Unauthorized")))
    val body = request.body.as[JsObject]
    fromJson(body) match {
      case Some(todo) =>
        if (todosService.updateTodo(todo, userInfos.username))
          Ok (JsObject(body.fields ++ Seq("done" -> JsBoolean(true)))).as("application/json")
        else
          Ok (JsObject(body.fields ++ Seq("done" -> JsBoolean(false)))).as("application/json")
      case None => BadRequest("Invalid object sent")
    }
  }

  def userAuth(headerAuth: String): UserEntity = {
    authService.authenticate(headerAuth).getOrElse(throw new Error("Connexion failed"))
  }


}
/*
@Path("/api/todos")
@Consumes(MediaType.APPLICATION_JSON)
class TodoResource {
    private static final Logger logger = Logger.getLogger(TodoResource.class);

    @Inject Validator validator;
    @Inject AuthService authService;
    @Inject UserService userService;

    @GET
    @RolesAllowed({ Role.Names.ADMIN, Role.Names.REGULAR })
    public Response todos(
            @QueryParam("tags") Optional<String> tagsFilter,
            @QueryParam("user") Optional<String> userName
    ) {
        var userTarget =  userName.orElse(authService.userName());

        var todos = tagsFilter
                .map(tags -> {
                    // Tag query
                    var tagsList = Arrays.stream(tagsFilter.get().split(",")).toList();
                    return userService.queryUser(userTarget, Optional.of(UserFilters.todoTags(tagsList)));
                })
                .orElseGet(() -> {
                    // Basic query
                    return userService.queryUser(userTarget);
                })
                .map(res -> res.map(user -> user.todos).orElse(List.of()));

        return todos
                .map(Response::ok)
                .getOrElseGet(error -> Response.status(Response.Status.BAD_REQUEST).entity(error))
                .build();    }

    @GET
    @Path("/{id}")
    @RolesAllowed({ Role.Names.ADMIN, Role.Names.REGULAR })
    public Response todo(
            @PathParam("id") ObjectId id,
            @QueryParam("user") Optional<String> userName
    ) {
        var userTarget =  userName.orElse(authService.userName());

        var todo = userService.queryUser(userTarget,
    Optional.of(UserFilters.todoId(id))
        );

        return todo
                .map(Response::ok)
                .getOrElseGet(error -> Response.status(Response.Status.BAD_REQUEST).entity(error))
                .build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({ Role.Names.ADMIN, Role.Names.REGULAR })
    public Response deleteTodo(
            @PathParam("id") ObjectId id,
            @QueryParam("user") Optional<String> userName
        ) {
        var userTarget =  userName.orElse(authService.userName());

        var todos = userService.queryUser(userTarget)
            .flatMap(maybeUser -> {
                // Extract UserEntity
                return maybeUser
                        .map(Either::<String, UserEntity>right)
                        .orElse(Either.left("User not found"));
            })
            .flatMap(user -> {
                // Extract TodoEntity
                var maybeTodoToDelete = user.todos.stream().filter(t -> t.todoId.equals(id)).findFirst();
                return maybeTodoToDelete
                    .map(todoToDelete -> Either.<String, Pair<UserEntity, TodoEntity>>right(Pair.with(user, todoToDelete)))
                    .orElse(Either.left("Todo not found"));
            })
            .flatMap(pair -> {
                // Remove TodoEntity from UserEntity
                return userService.removeTodo(pair.getValue0(), pair.getValue1());
            })
            .map(user -> {
                // Persist changes
                user.persistOrUpdate();
                return user.todos;
            });

        return todos
                .map(e -> Response.noContent())
                .getOrElseGet(error -> Response.status(Response.Status.BAD_REQUEST).entity(error))
                .build();
    }

    @POST
    @RolesAllowed({ Role.Names.ADMIN, Role.Names.REGULAR })
    public Response addTodo(
            @QueryParam("user") Optional<String> userName,
            TodoEntity todoToAdd
    ) {
        var violations = validator.validate(todoToAdd);
        if (!violations.isEmpty()) {
            var messages = violations.stream().map(ConstraintViolation::getMessage);
            return Response.status(Response.Status.BAD_REQUEST).entity(messages).build();
        }

        var userTarget =  userName.orElse(authService.userName());

        var uri = userService.queryUser(userTarget)
                .flatMap(maybeUser -> {
                    // Extract UserEntity
                    return maybeUser
                        .map(Either::<String, UserEntity>right)
                        .orElse(Either.left("User not found"));
                })
                .flatMap(user -> {
                    // Extract TodoEntity
                    var todoAlreadyExists = user.todos.stream().anyMatch(t -> t.todoId.equals(todoToAdd.todoId));
                    return todoAlreadyExists ?
                        Either.left("Todo already exists") :
                        Either.right(user);
                })
                .flatMap(user -> {
                    // Adding TodoEntity to UserEntity
                    return userService.addTodo(user, todoToAdd);
                })
                .map(user -> {
                    // Persist changes
                    user.persistOrUpdate();
                    return URI.create(String.format("/api/todos/%s", todoToAdd.todoId));
                });

        return uri
                .map(r -> Response.created(r).entity(todoToAdd))
                .getOrElseGet(error -> Response.status(Response.Status.BAD_REQUEST).entity(error))
                .build();
    }

    @PUT
    @RolesAllowed({ Role.Names.ADMIN, Role.Names.REGULAR })
    public Response updateTodo(
            @QueryParam("user") Optional<String> userName,
            TodoEntity todoToUpdate
    ) {
        var userTarget = userName.orElse(authService.userName());

        var todos = userService.queryUser(userTarget)
                .flatMap(maybeUser -> {
                    // Extract UserEntity
                    return maybeUser
                            .map(Either::<String, UserEntity>right)
                            .orElse(Either.left("User not found"));
                })
                .flatMap(user -> {
                    // Extract TodoEntity
                    var existingTodo = user.todos
                            .stream()
                            .filter(t -> t.todoId.equals(todoToUpdate.todoId))
                            .findFirst();

                    return existingTodo
                        .map(todo -> Either.<String, Pair<UserEntity, TodoEntity>>right(Pair.with(user, todo)))
                        .orElse(Either.left("Todo not found"));
                })
                .flatMap(pair -> {
                    // Adding TodoEntity to UserEntity
                    return userService.updateTodo(pair.getValue0(), pair.getValue1());
                })
                .map(user -> {
                    // Persist changes
                    user.persistOrUpdate();
                    return user.todos;
                });

        return todos
                .map(Response::ok)
                .getOrElseGet(error -> Response.status(Response.Status.BAD_REQUEST).entity(error))
                .build();
    }
}
*/
