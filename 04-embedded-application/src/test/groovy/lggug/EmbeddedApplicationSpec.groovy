package lggug

import ratpack.groovy.test.embed.GroovyEmbeddedApp
import ratpack.test.CloseableApplicationUnderTest
import ratpack.test.http.TestHttpClient
import spock.lang.AutoCleanup
import spock.lang.Specification

class EmbeddedApplicationSpec extends Specification {

    @AutoCleanup
    CloseableApplicationUnderTest aut = GroovyEmbeddedApp.of {
        registryOf {
            add([])
        }
        handlers {
            all { ctx ->
                byMethod {
                    post {
                        request.body.then { ctx.get(List) << it.text }
                    }
                    get {
                        render ctx.get(List).toString()
                    }
                }
            }
        }
    }

    @Delegate
    TestHttpClient client = aut.httpClient

    void postText(String path, String text) {
        requestSpec {
            it.body.text(text)
        }
        post(path)
        resetRequest()
    }

    def "can define an inline application for test purposes"() {
        when:
        postText("/", "foo")
        postText("/", "bar")
        postText("/", "fizz")
        postText("/", "buzz")

        then:
        getText("/") == "[foo, bar, fizz, buzz]"
    }

}