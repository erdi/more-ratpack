package lggug

import ratpack.exec.Operation

import javax.inject.Inject
import java.util.concurrent.ConcurrentHashMap

class BookStore {

    private final Map<String, Book> store = new ConcurrentHashMap()

    private final BookDetailsService detailsService

    @Inject
    BookStore(BookDetailsService detailsService) {
        this.detailsService = detailsService
    }

    Operation update(String isbn, int quantity, BigDecimal price) {
        detailsService.get(isbn).operation {
            store[isbn] = new Book(
                isbn, quantity, price, it.title, it.authors
            )
        }
    }

    Book get(String isbn) {
        store[isbn]
    }
}
