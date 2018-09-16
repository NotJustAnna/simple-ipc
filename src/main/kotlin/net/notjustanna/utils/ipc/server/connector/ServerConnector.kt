package net.notjustanna.utils.ipc.server.connector

import net.notjustanna.utils.io.DataPipe
import net.notjustanna.utils.ipc.proto.Protocol
import net.notjustanna.utils.ipc.server.dsl.ServerBuilder

interface ServerConnectorFactory<TBuilder : ServerBuilder<TInstance>, TInstance> {
    fun makeBuilder(protocol: Protocol): TBuilder

    fun buildConnector(builder: TBuilder): ServerConnector<TInstance>
}

interface ServerConnector<T> {
    fun awaitNextConnection(): ServerConnection<T>
    fun shutdown()
}

data class ServerConnection<T>(val connection: T, val pipe: DataPipe)