package lggug

import com.google.inject.AbstractModule
import com.google.inject.Singleton

class BooksModule extends AbstractModule {

    protected void configure() {
        bind(BookStore).in(Singleton)
        bind(BookDetails)
    }

}