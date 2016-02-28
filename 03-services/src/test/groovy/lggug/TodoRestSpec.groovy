package lggug

import com.fasterxml.jackson.databind.ObjectMapper
import lggug.persistence.Todo
import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.test.ApplicationUnderTest
import ratpack.test.http.TestHttpClient
import spock.lang.AutoCleanup
import spock.lang.Specification

import static io.netty.handler.codec.http.HttpHeaderNames.LOCATION
import static io.netty.handler.codec.http.HttpResponseStatus.CREATED
import static io.netty.handler.codec.http.HttpResponseStatus.OK
import static ratpack.http.MediaType.APPLICATION_JSON

class TodoRestSpec extends Specification {

    ObjectMapper mapper = new ObjectMapper()

    @AutoCleanup
    ApplicationUnderTest aut = new GroovyRatpackMainApplicationUnderTest()

    @Delegate
    TestHttpClient client = aut.httpClient


    void cleanup() {
        resetRequest()
        post("api/todos/clear")
    }

    private void writeTodoIntoRequest(Todo todo) {
        requestSpec {
            it.body.type(APPLICATION_JSON).text(mapper.writeValueAsString(todo))
        }
    }

    private Todo parseTodoFromResponse() {
        mapper.readValue(response.body.inputStream, Todo)
    }

    def "adding todos"() {
        when:
        writeTodoIntoRequest(todo)
        post("api/todos")

        then:
        response.statusCode == CREATED.code()

        when:
        resetRequest()
        get(response.headers.get(LOCATION))

        then:
        response.statusCode == OK.code()

        when:
        def retrievedTodo = parseTodoFromResponse()

        then:
        retrievedTodo.contents == todo.contents
        retrievedTodo.completed == todo.completed

        where:
        todo = new Todo(contents: "My first todo", completed: false)
    }

}
