package secondary_adapters.mongodb.repository

import core.domain.models.TodoEntity
import core.ports.TodosRepository
import play.api.Configuration
import secondary_adapters.mongodb.MongoService
import secondary_adapters.mongodb.mapper.{TodoMapper, UserMapper}
import secondary_adapters.mongodb.models.{TodoDocument, UserDocument}

import javax.inject.Inject
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

class MongoTodosAccess @Inject()(mongoService: MongoService, config: Configuration) extends TodosRepository {

  private val COLLECTION_NAME: String = config.get[String]("mongo.collection.users")

  // get all users to know all users and their items
  override def getTodos(tagsFilter: Option[List[String]], user: Option[String]): Seq[TodoEntity] = {
    // TODO make the tags filter (add field tags to todos entities and make function to find many tags in mongo list)
    val searchFilters: Option[List[(String, String)]] = user match {
      case Some(value) => Some(List(("username", value)))
      case None => None
    }

    val collection = mongoService.findInMongo[UserDocument](COLLECTION_NAME, UserDocument.codecRegistry, searchFilters)
    Await.result(collection, 10.seconds)
      .flatMap(doc => UserMapper.userMapper(doc).todos)
  }

  override def getTodo(id: String, user: Option[String]): TodoEntity = {
    val objectInfos: Option[(String, String)] =
      user match {
        case Some(value) => Some(("username", value))
        case None => None
      }


    val collection: Future[Seq[UserDocument]] = mongoService.findInMongoObject[UserDocument](COLLECTION_NAME, UserDocument.codecRegistry, ("todos", "_id", id), objectInfos)
    Await.result(collection, 10.seconds)
      .map(doc => UserMapper.userMapper(doc).todos.head).head
  }

  override def createTodo(todo: TodoEntity, username: String): Boolean = {
    val objectInfos: (String, String) = ("username", username)
    val elementInfos: (String, TodoDocument) = ("todos", TodoMapper.todoDocumentMapper(todo))
    mongoService.addInMongoObject(COLLECTION_NAME, UserDocument.codecRegistry, objectInfos, elementInfos)
  }

  override def updateTodo(todo: TodoEntity, username: String): Boolean = {
    val objectInfos: (String, String) = ("username", username)
    val elementInfos: (String, String) = ("todos._id", todo.id)
    val elementChanges: (String, TodoDocument) = ("todos.$", TodoMapper.todoDocumentMapper(todo))
    mongoService.updateInMongoObject(COLLECTION_NAME, UserDocument.codecRegistry, objectInfos, elementInfos, elementChanges)
  }

  override def deleteTodo(id: String, username: String): Boolean = {
    val objectInfos: (String, String) = ("username", username)
    val elementInfos: (String, String, String) = ("todos", "_id", id)
    mongoService.deleteInMongoObject(COLLECTION_NAME, UserDocument.codecRegistry, objectInfos, elementInfos)
  }
}
