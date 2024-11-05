package adapters.mongodb.models

import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.{BsonObjectId, ObjectId}

case class TodoDocument(_id: ObjectId, title: String, tags: Seq[String], done: Boolean) {

}

object TodoDocument {
  def apply(title: String, tags: Seq[String], done: Boolean): TodoDocument = {
    TodoDocument(new ObjectId, title, tags, done)
  }

  def apply(id: String, title: String, tags: Seq[String], done: Boolean): TodoDocument = {
    val objectId: ObjectId = new ObjectId(id)
    new TodoDocument(objectId, title, tags, done)
  }

  import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
  import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
  import org.mongodb.scala.bson.codecs.Macros._

  // Save of codec for MongoDB serialize and deserialized
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[TodoDocument]), DEFAULT_CODEC_REGISTRY)
}