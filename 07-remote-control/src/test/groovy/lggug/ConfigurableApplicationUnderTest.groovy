package lggug

import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.impose.ImpositionsSpec
import ratpack.impose.ServerConfigImposition
import ratpack.impose.UserRegistryImposition
import ratpack.registry.Registry
import ratpack.remote.RemoteControl
import ratpack.server.ServerConfigBuilder

import java.util.function.Consumer

class ConfigurableApplicationUnderTest extends GroovyRatpackMainApplicationUnderTest {

    Consumer<ServerConfigBuilder> serverConfigImpositionAction = {}
    Registry imposedRegistry = Registry.empty()

    ConfigurableApplicationUnderTest withConfig(Map<String, ?> config) {
        serverConfigImpositionAction = serverConfigImpositionAction.andThen {
            it.add(new MapConfigSource(config))
        }
        this
    }

    ConfigurableApplicationUnderTest withRemoteControl() {
        imposedRegistry = Registry.single(RemoteControl.handlerDecorator())
        this
    }

    @Override
    protected void addImpositions(ImpositionsSpec impositions) {
        impositions.add(ServerConfigImposition.of(serverConfigImpositionAction))
        impositions.add(UserRegistryImposition.of(imposedRegistry))
    }

}
