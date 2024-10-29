package adapters.models

import com.mongodb.client.model.Filters
import lunatech.entities.FilterEntity
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import play.api.Configuration

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import scala.reflect.ClassTag

@Singleton
class MongoService @Inject()(config: Configuration) {
  // Récupération des variables de conf
  val mongoHost: String = config.get[String]("mongo.host")
  val mongoPort: String = config.get[String]("mongo.port")
  val databaseName: String = config.get[String]("mongo.database")

  // Initialize connection with the database
  val mongoClient: MongoClient = MongoClient(mongoHost + mongoPort)
  val database: MongoDatabase = mongoClient.getDatabase(databaseName)

  def findInMongo[A](collectionName: String, codecRegistry: CodecRegistry, filters: Option[List[FilterEntity]] = None, sorts: Option[String] = None)(implicit ct: ClassTag[A]): Future[Seq[A]] = {
    val mongoCollection: MongoCollection[A] =
      database.getCollection[A](collectionName).withCodecRegistry(codecRegistry)

    (filters, sorts) match {
      case (Some(filter), None) => mongoCollection.find(createFilter(filter)).toFuture()
      // TODO : add sort method
      case (None, Some(sort)) => mongoCollection.find().toFuture()
      case (Some(filter), Some(sort)) => mongoCollection.find(createFilter(filter)).toFuture()
      case (None, None) => mongoCollection.find().toFuture()
    }
  }

  def createInMongo[A](collectionName: String, item: A, codecRegistry: CodecRegistry)(implicit ct: ClassTag[A]): Boolean = {
    // TODO : make new item in mongo and return if the creation are work or failed
    val mongoCollection: MongoCollection[A] =
      database.getCollection[A](collectionName).withCodecRegistry(codecRegistry)

    mongoCollection.insertOne(item).toFuture().isCompleted
  }

  def createFilter(filters: List[FilterEntity]): Bson = {
    val individualFilters: List[Bson] = filters.map(filter => {
      filter.field match {
        case "_id" => Filters.eq(filter.field, new ObjectId(filter.value))
        case _ => Filters.eq(filter.field, filter.value)
      }
    })

    Filters.and(individualFilters: _*)
  }
}