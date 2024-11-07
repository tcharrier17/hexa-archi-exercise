package secondary_adapters.mongodb

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections.{fields, include}
import com.mongodb.client.model.Updates.{pull, push, set}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.result.UpdateResult
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import play.api.Configuration

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

@Singleton
class MongoService @Inject()(config: Configuration) (ec: ExecutionContext) {
  // Récupération des variables de conf
  val mongoHost: String = config.get[String]("mongo.host")
  val mongoPort: String = config.get[String]("mongo.port")
  val databaseName: String = config.get[String]("mongo.database")

  // Initialize connection with the database
  val mongoClient: MongoClient = MongoClient(mongoHost + mongoPort)
  val database: MongoDatabase = mongoClient.getDatabase(databaseName)

  def findInMongo[A](collectionName: String, codecRegistry: CodecRegistry, filters: Option[List[(String, String)]] = None)(implicit ct: ClassTag[A]): Future[Seq[A]] = {
    val mongoCollection = getCollectionWithCodecRegistry(collectionName, codecRegistry)

    filters match {
      case Some(filter) if filter.nonEmpty => mongoCollection.find(createFilter(filter)).toFuture()
      case None => mongoCollection.find().toFuture()
    }
  }

  def createInMongo[A](collectionName: String, item: A, codecRegistry: CodecRegistry)(implicit ct: ClassTag[A]): Boolean = {
    // TODO : make new item in mongo and return if the creation are work or failed
    getCollectionWithCodecRegistry(collectionName, codecRegistry).insertOne(item).toFuture().isCompleted
  }

  def findInMongoObject[A](collectionName: String, codecRegistry: CodecRegistry, elementInfos: (String, String, String),
                           objectInfos: Option[(String, String)] = None)(implicit ct: ClassTag[A]): Future[Seq[A]] = {
    (objectInfos match {
      case Some((field, value)) =>
        getCollectionWithCodecRegistry(collectionName, codecRegistry)
          .find(
            and(createFilter(List((field, value))),
              elemMatch(elementInfos._1, createFilter(List((elementInfos._2, elementInfos._3))))
            )
          ).projection(fields(include("_id", "username", "password", "role", "todos.$")))
      case None => getCollectionWithCodecRegistry(collectionName, codecRegistry)
        .find(
          elemMatch(
            elementInfos._1,
            createFilter(List((elementInfos._2, elementInfos._3)))
          )
        ).projection(fields(include("_id", "username", "password", "role", "todos.$")))
    }).toFuture()
  }

  def addInMongoObject[A](collectionName: String, codecRegistry: CodecRegistry,
                          objectInfos: (String, String), elementInfos: (String, Any))(implicit ct: ClassTag[A]): Boolean = {

    val addResult: Future[UpdateResult] = getCollectionWithCodecRegistry(collectionName, codecRegistry).updateOne(
      createFilter(List(objectInfos)),
      push(elementInfos._1, elementInfos._2)
    ).toFuture()

    addResult.isCompleted
  }

  def updateInMongoObject[A](collectionName: String, codecRegistry: CodecRegistry, objectInfos: (String, String), elementInfos: (String, String),
                             elementChanges: (String, Any))(implicit ct: ClassTag[A]): Boolean = {

    val updateTodoResult: Future[UpdateResult] = getCollectionWithCodecRegistry(collectionName, codecRegistry).updateOne(
      createFilter(List(objectInfos, elementInfos)),
      set(elementChanges._1, elementChanges._2)
    ).toFuture()

    updateTodoResult.isCompleted

  }

  def deleteInMongoObject[A](collectionName: String, codecRegistry: CodecRegistry, objectInfos: (String, String),
                             elementInfos: (String, String, String))(implicit ct: ClassTag[A]): Boolean = {
    val removeTodoResult: Future[UpdateResult] = getCollectionWithCodecRegistry(collectionName, codecRegistry).updateOne(
      createFilter(List(objectInfos)),
      pull(elementInfos._1, createFilter(List((elementInfos._2, elementInfos._3))))
    ).toFuture()

    removeTodoResult.isCompleted
  }

  def clearCollection(collectionName: String, codecRegistry: CodecRegistry): Boolean = {
    getCollectionWithCodecRegistry(collectionName, codecRegistry).deleteMany(Filters.empty()).toFuture().isCompleted
  }

  def getCollectionWithCodecRegistry[A](collectionName: String, codecRegistry: CodecRegistry)(implicit ct: ClassTag[A]): MongoCollection[A] = {
    database.getCollection[A](collectionName).withCodecRegistry(codecRegistry)
  }

//  def getMongoResult(updateResult: Future[UpdateResult]): String = {
//
//    val result: String =  updateResult.onComplete {
//      case Success(result) => result.getModifiedCount
//      case Failure(e) => e.getMessage
//    }
//  }

  def createFilter(filters: List[(String, String)]): Bson = {
    val individualFilters: List[Bson] = filters.map { case (field, value) =>
      if (field.contains("_id")) Filters.eq(field, new ObjectId(value)) else Filters.eq(field, value)
    }

    individualFilters.length match {
      case 1 => individualFilters.head
      case _ => Filters.and(individualFilters: _*)
    }
  }
}
/*
val collectionName: String = config.get[String]("mongo.collection.users")

val mongoCollection: MongoCollection[UserDocument] = database.getCollection[UserDocument](collectionName).withCodecRegistry(UserDocument.codecRegistry)

  def findInMongo(filters: Option[List[FilterEntity]] = None): Future[Seq[UserDocument]] = {
    filters match {
      case Some(filter) => mongoCollection.find(createFilter(filter)).toFuture()
      case None => mongoCollection.find().toFuture()
    }
  }

  def createInMongo(item: UserDocument): Boolean = {
    // TODO : make new item in mongo and return if the creation are work or failed
    mongoCollection.insertOne(item).toFuture().isCompleted
  }
 */
