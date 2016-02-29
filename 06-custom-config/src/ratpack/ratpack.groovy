import lggug.BookStore
import lggug.BooksModule
import lggug.IsbnDbConfig

import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json
import static ratpack.jackson.Jackson.jsonNode

ratpack {
    serverConfig {
        props(
            "isbnDb.uri": "http://isbndb.com",
            "isbnDb.apiKey": "OVD0YYGX"
        )
        require("/isbnDb", IsbnDbConfig)
    }
    bindings {
        module(BooksModule)
    }
    handlers {
        path("books/:isbn") { ctx ->
            byMethod {
                post {
                    parse(jsonNode()).nextOp {
                        def quantity = it.get("quantity").intValue()
                        def price = it.get("price").decimalValue()
                        ctx.get(BookStore).update(allPathTokens.isbn, quantity, price)
                    }.then {
                        response.send()
                    }
                }
                get {
                    def book = ctx.get(BookStore).get(allPathTokens.isbn)
                    render(json(book))
                }
            }
        }
    }
}