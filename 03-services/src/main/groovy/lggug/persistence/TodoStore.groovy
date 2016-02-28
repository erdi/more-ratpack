package lggug.persistence

import org.jooq.DSLContext
import ratpack.exec.Blocking
import ratpack.exec.Operation
import ratpack.exec.Promise

import javax.inject.Inject

import static java.util.UUID.randomUUID
import static lggug.persistence.TodoTable.TODO

class TodoStore {

    private final DSLContext jooq

    @Inject
    TodoStore(DSLContext jooq) {
        this.jooq = jooq
    }

    Promise<String> add(Todo todo) {
        def id = todo.id ?: randomUUID().toString()
        todo.id = id
        def record = jooq.newRecord(TODO, todo)
        Blocking.get {
            jooq.executeInsert(record)
        }.map {
            id
        }
    }

    private final Promise<TodoRecord> withId(String id) {
        def select = jooq.selectFrom(TODO).where(TODO.ID.equal(id))
        Blocking.get {
            select.fetchOne()
        }
    }

    Promise<Todo> fetch(String id) {
        withId(id).map {
            it?.into(Todo)
        }
    }

    Promise<List<Todo>> all() {
        Blocking.get {
            jooq.fetch(TODO)
        }.map {
            it.into(Todo)
        }
    }

    Operation clear() {
        def delete = jooq.deleteFrom(TODO)
        Blocking.op {
            jooq.execute(delete)
        }
    }
}
