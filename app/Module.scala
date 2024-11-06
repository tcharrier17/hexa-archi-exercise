import core.ports.incoming.UsersPort
import core.ports.outcoming.{TodosRepository, UsersRepository}
import core.services.UsersServices
import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}
import secondary_adapters.mongodb.repository.{MongoTodosAccess, MongoUserRepository}

class Module(environment: Environment, configuration: Configuration) extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[UsersRepository]).to(classOf[MongoUserRepository])
    bind(classOf[TodosRepository]).to(classOf[MongoTodosAccess])
    bind(classOf[UsersPort]).to(classOf[UsersServices])
  }
}
