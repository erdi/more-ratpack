package lggug

import groovy.transform.EqualsAndHashCode
import ratpack.groovy.test.embed.GroovyEmbeddedApp
import ratpack.test.http.TestHttpClient
import ratpack.test.remote.RemoteControl
import spock.lang.AutoCleanup
import spock.lang.Specification

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE

class YamlRendererSpec extends Specification {

    @AutoCleanup
    def aut = GroovyEmbeddedApp.of {
        registryOf {
            add(List, [])
            add(new YamlParser())
            add(ratpack.remote.RemoteControl.handlerDecorator())
        }
        handlers {
            post { ctx ->
                parse(Entity).then {
                    ctx.get(List).add(it)
                    response.send()
                }
            }
        }
    }

    @Delegate
    TestHttpClient client = aut.httpClient

    def remote = new RemoteControl(aut)

    List getEntities() {
        remote.exec {
            get(List)
        }
    }

    def "parsing yaml using a parser"() {
        when:
        requestSpec {
            it.body.text("""---
values:
- "foo"
- "bar"
- "fizz"
- "buzz"
""")
            it.headers.set(CONTENT_TYPE, YamlParser.MIME_TYPE)
        }
        post "/"

        then:
        entities == [new Entity(values: ["foo", "bar", "fizz", "buzz"])]
    }

    @EqualsAndHashCode
    private static class Entity implements Serializable {
        List<String> values
    }

}