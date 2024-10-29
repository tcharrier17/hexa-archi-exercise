package adapters.models

import adapters.models.entity.UserDocument
import lunatech.entities.{FilterEntity, UserEntity}
import lunatech.services.ports.UsersRepository

import javax.inject.Inject
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

class MongoUserAccess @Inject()(mongoService: MongoService) extends UsersRepository {
  // get all users to know all users and their items
  override def getUsers(user: Option[String]): Seq[UserEntity] = {

    // TODO make the tags filter (add field tags to todos entities and make function to find many tags in mongo list)
    val searchFilters: Option[List[FilterEntity]] = user match {
      case Some(value) => Some(List(FilterEntity("username", value)))
      case None => None
    }

    val collection: Future[Seq[UserDocument]] = mongoService.findInMongo[UserDocument]("users", UserDocument.codecRegistry, searchFilters)
    Await.result(collection, 10.seconds)
      .map(doc => UserMapper(doc))
  }

  def UserMapper(userDocument: UserDocument): UserEntity = {
    UserEntity(userDocument.username, userDocument.password, userDocument.role, userDocument.todos)
  }

  override def verifyUser(username: String, password: String): Option[UserEntity] = {

    // TODO make the tags filter (add field tags to todos entities and make function to find many tags in mongo list)
    val searchFilters: List[FilterEntity] = List(FilterEntity("username", username), FilterEntity("password", password))

    val collection: Future[Seq[UserDocument]] = mongoService.findInMongo[UserDocument]("users", UserDocument.codecRegistry, Some(searchFilters))
    Await.result(collection, 10.seconds)
      .map(doc => UserMapper(doc)).headOption
  }
}
