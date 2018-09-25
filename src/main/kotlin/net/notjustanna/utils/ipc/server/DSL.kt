@file:JvmName("IPC")
@file:JvmMultifileClass

package net.notjustanna.utils.ipc.server

import net.notjustanna.utils.ipc.proto.DefaultProtocol
import net.notjustanna.utils.ipc.proto.Protocol
import net.notjustanna.utils.ipc.server.connector.ServerConnectorFactory
import net.notjustanna.utils.ipc.server.dsl.ServerBuilder
import net.notjustanna.utils.ipc.server.impl.ServerImpl

/**
 * Creates and configures an [IServer].
 *
 * @param factory The connector factory.
 * @param block The configuration of the server.
 * @return the configurated and running [IServer].
 */
fun <B : ServerBuilder<T>, T> ipcServer(
    factory: ServerConnectorFactory<B, T>, protocol: Protocol = DefaultProtocol, block: B.() -> Unit
): IServer {
    val builder = factory.makeBuilder(protocol).apply(block)
    val connector = factory.buildConnector(builder)

    return ServerImpl(
        protocol,
        builder.serverName,
        connector,
        builder.executor(),
        builder.calls,
        builder.extensions
    )
}