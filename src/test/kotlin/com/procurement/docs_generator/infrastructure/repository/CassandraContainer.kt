package com.procurement.docs_generator.infrastructure.repository

import org.testcontainers.containers.wait.strategy.Wait
import java.io.File

object CassandraContainer {

    private val initialScripts = File("docs/cassandra/data.cql").readText(Charsets.UTF_8)

    val container: CassandraTestContainer = CassandraTestContainer("3.11").apply { run() }
        get() {
            CassandraContainerInteractor(field).cqlsh(initialScripts)
            return field
        }

    private fun CassandraTestContainer.run() {
        setWaitStrategy(Wait.forListeningPort())
        start()
    }
}