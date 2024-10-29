package adapters.models

import adapters.models.entity.TodoDocument
import lunatech.entities.{FilterEntity, TodoEntity}
import lunatech.services.ports.TodosRepository
import org.mongodb.scala.bson.ObjectId
import play.api.Configuration

import javax.inject.Inject
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

class MongoTodosAccess @Inject()(mongoService: MongoService, config: Configuration) extends TodosRepository {

  val collectionName = config.get[String]("mongo.collection.todos")

  // get all users to know all users and their items
  override def getTodos(tagsFilter: Option[List[String]], user: Option[String]): Seq[TodoEntity] = {
    // TODO make the tags filter (add field tags to todos entities and make function to find many tags in mongo list)
    val searchFilters: Option[List[FilterEntity]] = user match {
      case Some(value) => Some(List(FilterEntity("owner", value)))
      case None => None
    }

    val collection: Future[Seq[TodoDocument]] = mongoService.findInMongo[TodoDocument](collectionName, TodoDocument.codecRegistry, searchFilters)
    Await.result(collection, 10.seconds)
      .map(doc => TodoMapper(doc))
  }

  override def getTodo(id: String, owner: Option[String]): TodoEntity = {
    val searchFilters: List[FilterEntity] = owner match {
      case Some(value) => List(FilterEntity("_id", id), FilterEntity("owner", value))
      case _ => List(FilterEntity("_id", id))
    }

    val collection: Future[Seq[TodoDocument]] = mongoService.findInMongo[TodoDocument](collectionName, TodoDocument.codecRegistry, Some(searchFilters))
    Await.result(collection, 10.seconds)
      .map(doc => TodoMapper(doc)).head
  }

  override def createTodo(todo: TodoEntity): Boolean = {
    mongoService.createInMongo[TodoDocument](collectionName, TodoDocumentMapper(todo), TodoDocument.codecRegistry)
  }

  override def updateTodo(id: String): Boolean = ???

  override def deleteTodo(id: String): Boolean = ???

  def TodoMapper(todoDocument: TodoDocument): TodoEntity = {
    TodoEntity(todoDocument.owner, todoDocument.title, todoDocument.tags, todoDocument.done)
  }

  def TodoDocumentMapper(todoEntity: TodoEntity): TodoDocument = {
    TodoDocument(todoEntity.owner, todoEntity.title, todoEntity.tags, todoEntity.done)
  }
}
