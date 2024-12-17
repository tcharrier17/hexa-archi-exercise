package secondary_adapters.mongodb.repository

import core.domain.models.UserEntity
import core.ports.UsersRepository
import secondary_adapters.mongodb.MongoService
import secondary_adapters.mongodb.mapper.UserMapper
import secondary_adapters.mongodb.models.UserDocument

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

class MongoUsersRepository @Inject()(mongoService: MongoService) extends UsersRepository {
  // get all users to know all users and their items
  override def getUsers(user: Option[String]): Seq[UserEntity] = {

    // TODO make the tags filter (add field tags to todos entities and make function to find many tags in mongo list)
    val searchFilters: Option[List[(String, String)]] = user match {
      case Some(value) => Some(List(("username", value)))
      case None => None
    }

    val collection: Future[Seq[UserDocument]] = mongoService.findInMongo[UserDocument]("users", UserDocument.codecRegistry, searchFilters)
    Await.result(collection, 10.seconds)
      .map(doc => UserMapper.userMapper(doc))
  }

  override def createUser(userEntity: UserEntity): Boolean = {
    mongoService.createInMongo[UserDocument]("users", UserMapper.userDocumentMapper(userEntity), UserDocument.codecRegistry)
  }

  override def getUserById(id: String): UserEntity = ???

  override def getUserByUsername(username: String): Option[UserEntity] = {
    // TODO make the tags filter (add field tags to todos entities and make function to find many tags in mongo list)
    val searchFilters: List[(String, String)] = List(("username", username))

    val collection: Future[Seq[UserDocument]] = mongoService.findInMongo[UserDocument]("users", UserDocument.codecRegistry, Some(searchFilters))
    Await.result(collection, 10.seconds)
      .map(doc => UserMapper.userMapper(doc)).headOption
  }

  override def updateUser(userEntity: UserEntity): Boolean = ???

  override def deleteUser(id: String): Boolean = ???

  override def deleteALl(): Boolean = mongoService.clearCollection("users", UserDocument.codecRegistry)
}
