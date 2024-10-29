package adapters.models.entity

import lunatech.entities.TodoEntity
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.ObjectId

case class UserDocument(_id: ObjectId = new ObjectId ,username: String, password: String, role: String, todos: List[TodoEntity])

object UserDocument {
  import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
  import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
  import org.mongodb.scala.bson.codecs.Macros._

  // Save of codec for MongoDB serialize and deserialized
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[UserDocument]), DEFAULT_CODEC_REGISTRY)
}