package adapters.mongodb.repository

import adapters.mongodb.MongoService
import adapters.mongodb.mapper.UserMapper
import adapters.mongodb.models.UserDocument
import application.ports.outcoming.UsersRepository
import domain.models.UserEntity

import javax.inject.Inject
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

class MongoUserRepository @Inject()(mongoService: MongoService) extends UsersRepository {
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

  override def verifyUser(username: String, password: String): Option[UserEntity] = {

    // TODO make the tags filter (add field tags to todos entities and make function to find many tags in mongo list)
    val searchFilters: List[(String, String)] = List(("username", username), ("password", password))

    val collection: Future[Seq[UserDocument]] = mongoService.findInMongo[UserDocument]("users", UserDocument.codecRegistry, Some(searchFilters))
    Await.result(collection, 10.seconds)
      .map(doc => UserMapper.userMapper(doc)).headOption
  }
}
