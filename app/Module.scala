import adapters.mongodb.repository.{MongoTodosAccess, MongoUserRepository}
import application.ports.incoming.UsersPort
import application.ports.outcoming.{TodosRepository, UsersRepository}
import application.services.UsersServices
import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}

class Module(environment: Environment, configuration: Configuration) extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[UsersRepository]).to(classOf[MongoUserRepository])
    bind(classOf[TodosRepository]).to(classOf[MongoTodosAccess])
    bind(classOf[UsersPort]).to(classOf[UsersServices])
  }
}
