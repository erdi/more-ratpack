package lggug

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.ContextConfiguration
import ratpack.test.ApplicationUnderTest
import ratpack.test.http.TestHttpClient
import spock.lang.Specification

@ContextConfiguration(loader = SpringApplicationContextLoader, classes = [BootApp])
@WebIntegrationTest(randomPort = true)
class BootAppSpec extends Specification {

    @Value('${local.server.port}')
    int port

    TestHttpClient client

    void setup() {
        ApplicationUnderTest aut = { new URI("http://localhost:$port") }
        client = aut.httpClient
    }

    def "responds with a greeting from root of the application"() {
        expect:
        client.getText("/") == "Hello World!"
    }

}
