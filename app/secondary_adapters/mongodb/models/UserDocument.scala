package secondary_adapters.mongodb.models

import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.ObjectId

case class UserDocument(_id: ObjectId = new ObjectId ,username: String, password: String, role: String, todos: List[TodoDocument])

object UserDocument {
  def apply(username: String, password: String, role: String, todos: List[TodoDocument]): UserDocument = {
    new UserDocument(new ObjectId, username, password, role, todos)
  }
  import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
  import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
  import org.mongodb.scala.bson.codecs.Macros._

  // Save of codec for MongoDB serialize and deserialized
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[UserDocument], classOf[TodoDocument]), DEFAULT_CODEC_REGISTRY)
}