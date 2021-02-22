package com.procurement.docs_generator.infrastructure.repository

import com.datastax.driver.core.BoundStatement
import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Session
import com.procurement.docs_generator.exception.database.ReadOperationException
import com.procurement.docs_generator.exception.database.SaveOperationException


fun BoundStatement.executeRead(session: Session): ResultSet = try {
    session.execute(this)
} catch (expected: Exception) {
    throw ReadOperationException(message = "Error read from the database. " + (expected.message ?: ""), cause = expected)
}

fun BoundStatement.executeWrite(session: Session): ResultSet = try {
    session.execute(this)
} catch (expected: Exception) {
    throw SaveOperationException(message = "Error writing to the database. " + (expected.message ?: ""), cause = expected)
}