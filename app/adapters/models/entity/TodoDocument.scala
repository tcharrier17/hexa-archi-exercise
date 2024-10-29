package adapters.models.entity

import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.ObjectId

case class TodoDocument(_id: ObjectId = new ObjectId, owner: String, title: String, tags: Seq[String], done: Boolean) {

}

object TodoDocument {
  def apply(owner: String, title: String, tags: Seq[String], done: Boolean): TodoDocument = {
    TodoDocument(new ObjectId, owner, title, tags, done)
  }

  import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
  import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
  import org.mongodb.scala.bson.codecs.Macros._

  // Save of codec for MongoDB serialize and deserialized
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[TodoDocument]), DEFAULT_CODEC_REGISTRY)
}