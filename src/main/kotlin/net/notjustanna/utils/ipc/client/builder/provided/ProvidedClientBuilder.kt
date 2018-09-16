package net.notjustanna.utils.ipc.client.builder.provided

import net.notjustanna.utils.io.DataPipe
import net.notjustanna.utils.ipc.client.builder.ClientBuilder
import net.notjustanna.utils.ipc.client.connector.ClientConnection
import net.notjustanna.utils.ipc.client.connector.ClientConnector

class ProvidedClientBuilder(private val provider: () -> DataPipe) : ClientBuilder<Nothing?>() {
    override fun buildConnector() = ProvidedConnector(provider)
}

class ProvidedConnector(private val provider: () -> DataPipe) : ClientConnector<Nothing?> {
    override fun createConnection() = ClientConnection(null, provider())

    override fun toString() = "ProvidedConnector"
}