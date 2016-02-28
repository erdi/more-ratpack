import lggug.ApplicationModule
import lggug.persistence.Todo
import lggug.persistence.TodoStore
import ratpack.error.ServerErrorHandler
import ratpack.h2.H2Module

import static io.netty.handler.codec.http.HttpHeaderNames.LOCATION
import static io.netty.handler.codec.http.HttpResponseStatus.CREATED
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND
import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json

ratpack {
    bindings {
        module(H2Module)
        module(ApplicationModule)
    }
    handlers { TodoStore store, ServerErrorHandler originalServerErrorHandler ->
        prefix "api/todos", {
            path {
                byMethod {
                    post {
                        parse(Todo).flatMap { todo ->
                            todo.id = null
                            store.add(todo)
                        }.then { id ->
                            response.headers.add(LOCATION, "/api/todos/" + id)
                            response.status(CREATED.code()).send()
                        }
                    }
                    get {
                        store.all().then { todos ->
                            render(json(todos))
                        }
                    }
                }
            }
            get ":id", {
                store.fetch(pathTokens.id).then { todo ->
                    todo ? render(json(todo)) : response.status(NOT_FOUND.code()).send()
                }
            }
            post "clear", {
                store.clear().then {
                    redirect "/"
                }
            }
        }
    }
}