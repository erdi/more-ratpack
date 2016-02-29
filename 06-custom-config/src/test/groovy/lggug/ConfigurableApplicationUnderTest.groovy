package lggug

import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.impose.ImpositionsSpec
import ratpack.impose.ServerConfigImposition
import ratpack.server.ServerConfigBuilder

import java.util.function.Consumer

class ConfigurableApplicationUnderTest extends GroovyRatpackMainApplicationUnderTest {

    Consumer<ServerConfigBuilder> serverConfigImpositionAction = {}

    ConfigurableApplicationUnderTest withConfig(Map<String, ?> config) {
        serverConfigImpositionAction = serverConfigImpositionAction.andThen {
            it.add(new MapConfigSource(config))
        }
        this
    }

    @Override
    protected void addImpositions(ImpositionsSpec impositions) {
        impositions.add(ServerConfigImposition.of(serverConfigImpositionAction))
    }
}
