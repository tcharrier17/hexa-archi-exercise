package lunatech.entities

import org.mongodb.scala.bson.ObjectId
import play.api.libs.json.{JsArray, JsObject, JsValue, Json, OFormat, Writes}

/**
     * username:
     * Username used for HTTP auth
     * Warning: it should be unique in the whole collection
 *
     * @example "Nicolas"
     *
     * password:
     * Password used for HTTP auth
     * @example "1234"
     *
     * role:
     * Role of the user
     * @example "admin"
     *
     * todos:
     * Associated todos
     * @example List.empty<TodoEntity>
     */

case class UserEntity(username: String, password: String, role: String, todos: List[TodoEntity] = List.empty)

object UserEntity {
  def apply(username: String, password: String, role: String, todos: List[TodoEntity] = List.empty): UserEntity = {
    require(username.length > 3 && username.length < 10, "Username length should be between 3 and 30 characters")
    require(password.length > 3 && password.length < 10, "Password length should be between 3 and 30 characters")
    require(role.length > 3 && role.length < 10, "Role length should be between 3 and 30 characters")

    new UserEntity(username, password, role, todos)

    // TODO : verifier dans la base si username n'existe pas
  }

  implicit val userJson: Writes[UserEntity] = Json.writes[UserEntity]

  implicit val jsonUser: OFormat[UserEntity] = Json.format[UserEntity]

  //  implicit val userWrites: Writes[UserEntity] = new Writes[UserEntity] {
//    def writes(user: UserEntity): JsObject = Json.obj(
//      "username" -> user.username,
//      "password" -> user.password,
//      "role" -> user.role,
//      "todos" -> user.todos
//    )
//  }
}

//    Note: This empty constructor should not be deleted as it is required by the app
//    public UserEntity() {}