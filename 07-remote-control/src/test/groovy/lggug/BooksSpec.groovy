package lggug

import com.fasterxml.jackson.databind.ObjectMapper
import ratpack.groovy.test.embed.GroovyEmbeddedApp
import ratpack.test.CloseableApplicationUnderTest
import ratpack.test.http.TestHttpClient
import ratpack.test.remote.RemoteControl
import spock.lang.AutoCleanup
import spock.lang.Specification

import static ratpack.http.MediaType.APPLICATION_JSON
import static ratpack.jackson.Jackson.json

class BooksSpec extends Specification {

    private final static ISBN_API_KEY = "secret"
    private final static GINA_ISBN = "1932394842"

    ObjectMapper mapper = new ObjectMapper()

    @AutoCleanup
    CloseableApplicationUnderTest isbnDbMock = GroovyEmbeddedApp.of {
        handlers {
            get("api/v2/json/$ISBN_API_KEY/book/$GINA_ISBN") {
                 render(json(
                     data: [
                         [
                             title: "Groovy in Action",
                             author_data: [
                                 [name: "Dierk Koenig"],
                                 [name: "Guillaume Laforge"],
                                 [name: "Andrew Glover"],
                                 [name: "Gosling, James"]
                             ]
                         ]
                     ]
                 ))
            }
        }
    }

    @AutoCleanup
    def aut = new ConfigurableApplicationUnderTest().withRemoteControl().withConfig(
        isbnDb: new IsbnDbConfig(
            uri: isbnDbMock.address,
            apiKey: ISBN_API_KEY
        )
    )

    @Delegate
    TestHttpClient client = aut.httpClient

    RemoteControl remoteControl = new RemoteControl(applicationUnderTest)

    void updateInventory(String isbn, int quantity, BigDecimal price) {
        requestSpec {
            def json = mapper.writeValueAsString([quantity: quantity, price: price])
            it.body.type(APPLICATION_JSON).text(json)
        }
        post("books/$isbn")
    }

    Book retrieveBook(String isbn) {
        remoteControl.exec {
            get(BookStore).get(isbn)
        }
    }

    def "can update book inventory"() {
        when:
        updateInventory(GINA_ISBN, quantity, price)

        then:
        retrieveBook(GINA_ISBN) == new Book(
            GINA_ISBN,
            quantity,
            price,
            "Groovy in Action",
            ["Dierk Koenig", "Guillaume Laforge", "Andrew Glover", "Gosling, James"]
        )

        where:
        quantity = 10
        price = 28.33
    }

}