package lggug

import groovy.transform.Immutable

@Immutable
class Book implements Serializable {
    String isbn
    int quantity
    BigDecimal price
    String title
    List<String> authors
}
