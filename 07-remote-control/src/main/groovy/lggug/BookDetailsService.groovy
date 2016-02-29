package lggug

import com.fasterxml.jackson.databind.ObjectMapper
import ratpack.exec.Promise
import ratpack.http.HttpUrlBuilder
import ratpack.http.client.HttpClient

import javax.inject.Inject

class BookDetailsService {

    private final HttpClient client
    private final ObjectMapper mapper
    private final IsbnDbConfig config

    @Inject
    BookDetailsService(HttpClient client, ObjectMapper mapper, IsbnDbConfig config) {
        this.client = client
        this.mapper = mapper
        this.config = config
    }

    Promise<BookDetails> get(String isbn) {
        def uri = HttpUrlBuilder.base(config.uri)
            .path("api/v2/json")
            .segment(config.apiKey)
            .path("book")
            .segment(isbn)
            .build()

        client.get(uri).map {
            def json = mapper.readTree(it.body.inputStream)
            def bookDetails = json.get("data").get(0)
            new BookDetails(
                bookDetails.get("title").textValue(),
                bookDetails.get("author_data")*.get("name")*.textValue()
            )
        }
    }

}
