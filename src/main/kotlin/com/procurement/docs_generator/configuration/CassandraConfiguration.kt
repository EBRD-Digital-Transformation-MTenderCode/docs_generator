package com.procurement.docs_generator.configuration

import com.datastax.driver.core.Session
import com.procurement.docs_generator.configuration.properties.CassandraProperties
import com.procurement.docs_generator.infrastructure.cassandra.CassandraClusterBuilder
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    value = [
        CassandraProperties::class
    ]
)
@ComponentScan(
    basePackages = [
        "com.procurement.docs_generator.infrastructure.repository"
    ]
)
class CassandraConfiguration(
    private val cassandraProperties: CassandraProperties
) {

    @Bean
    fun session(): Session {
        val cluster = CassandraClusterBuilder
            .build(cassandraProperties)
        return if (cassandraProperties.keyspaceName != null)
            cluster.connect(cassandraProperties.keyspaceName)
        else
            cluster.connect()
    }
}