package lggug.persistence

import liquibase.Contexts
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import ratpack.server.Service
import ratpack.server.StartEvent

import javax.inject.Inject
import javax.sql.DataSource
import java.sql.Connection

class DatabaseInitializationService implements Service {

    private static final String CHANGELOG_RESOURCE_URI = "db-master-changelog.xml"

    private final DataSource dataSource

    @Inject
    DatabaseInitializationService(DataSource dataSource) {
        this.dataSource = dataSource
    }

    void onStart(StartEvent event) throws Exception {
        migrate()
    }

    private void migrate() {
        Connection connection = dataSource.getConnection()
        try {
            migrateWithConnection(connection)
        } finally {
            connection.close()
        }
    }

    private void migrateWithConnection(Connection connection) {
        def dbConnection = new JdbcConnection(connection)
        def resourceAccessor = new ClassLoaderResourceAccessor()

        def liquibase = new Liquibase(CHANGELOG_RESOURCE_URI, resourceAccessor, dbConnection)
        liquibase.update(new Contexts())
    }
}
