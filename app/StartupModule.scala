import com.google.inject.AbstractModule
import play.api.{Environment, Configuration}

class StartupModule(environment: Environment, configuration: Configuration) extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[Startup]).asEagerSingleton() // Créé l'instance au démarrage de l'application
  }
}
