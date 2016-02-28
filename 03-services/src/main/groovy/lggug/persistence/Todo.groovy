package lggug.persistence

import groovy.transform.ToString

@ToString(includePackage = false, includeNames = true)
class Todo {
    String id
    String contents
    Boolean completed
}
