package net.notjustanna.utils.ipc.server.connector.provided

import net.notjustanna.utils.ipc.proto.Protocol
import net.notjustanna.utils.ipc.server.connector.ServerConnection
import net.notjustanna.utils.ipc.server.connector.ServerConnector
import net.notjustanna.utils.ipc.server.connector.ServerConnectorFactory
import net.notjustanna.utils.ipc.server.dsl.ServerBuilder

class Provided<T> : ServerConnectorFactory<ProvidedServerBuilder<T>, T> {
    override fun makeBuilder(protocol: Protocol) = ProvidedServerBuilder<T>(protocol)
    override fun buildConnector(builder: ProvidedServerBuilder<T>) = ProvidedConnector(
        builder.provider ?: throw IllegalStateException("No provider given.")
    )
}

open class ProvidedServerBuilder<T>(protocol: Protocol) : ServerBuilder<T>(protocol) {
    var provider: (() -> ServerConnection<T>)? = null
}

class ProvidedConnector<T>(val provider: () -> ServerConnection<T>) : ServerConnector<T> {
    override fun awaitNextConnection(): ServerConnection<T> = provider()
    override fun shutdown() = Unit
}