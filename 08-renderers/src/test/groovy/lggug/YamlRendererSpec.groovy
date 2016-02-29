package lggug

import ratpack.groovy.test.embed.GroovyEmbeddedApp
import ratpack.registry.Registry
import ratpack.test.http.TestHttpClient
import spock.lang.AutoCleanup
import spock.lang.Specification

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE
import static lggug.YamlRender.yaml

class YamlRendererSpec extends Specification {

    @AutoCleanup
    def aut = GroovyEmbeddedApp.of {
        registry(Registry.single(new YamlRenderer()))
        handlers {
            get {
                def entity = new Entity(values: ["foo", "bar", "fizz", "buzz"])
                render(yaml(entity))
            }
        }
    }

    @Delegate
    TestHttpClient client = aut.httpClient

    def "rendering yaml using a renderer"() {
        when:
        get "/"

        then:
        response.headers.get(CONTENT_TYPE) == YamlRender.MIME_TYPE
        response.body.text == """---
values:
- "foo"
- "bar"
- "fizz"
- "buzz"
"""
    }

    private static class Entity {
        List<String> values
    }

}
