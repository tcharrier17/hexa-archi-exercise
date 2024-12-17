package core.domain.models

/**
  * username:
  * Username used for HTTP auth
  * Warning: it should be unique in the whole collection
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

case class UserEntity(id: String, username: String, password: String, role: String, todos: List[TodoEntity] = List.empty)

object UserEntity {
  def apply(id: String, username: String, password: String, role: String, todos: List[TodoEntity]): UserEntity = {
    require(username.length >= 3 && username.length < 10, "Username length should be between 3 and 30 characters")
    require(password.length >= 3 && password.length < 10, "Password length should be between 3 and 30 characters")
    require(role.length >= 3 && role.length < 10, "Role length should be between 3 and 30 characters")

    new UserEntity(id, username, password, role, todos)

    // TODO : verifier dans la base si username n'existe pas
  }

  def apply(username: String, password: String, role: String): UserEntity = {
    require(username.length >= 3 && username.length < 10, "Username length should be between 3 and 30 characters")
    require(password.length >= 3 && password.length < 10, "Password length should be between 3 and 30 characters")
    require(role.length >= 3 && role.length < 10, "Role length should be between 3 and 30 characters")

    new UserEntity("", username, password, role, List.empty)

    // TODO : verifier dans la base si username n'existe pas
  }

  def apply(): UserEntity = {
    new UserEntity("", "", "", "", List.empty)
  }
}