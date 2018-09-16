package net.notjustanna.utils.ipc.client.builder

import net.notjustanna.utils.ipc.client.IClient
import net.notjustanna.utils.ipc.client.IClientPool
import net.notjustanna.utils.ipc.client.connector.ClientConnector
import net.notjustanna.utils.ipc.client.impl.ClientImpl
import net.notjustanna.utils.ipc.client.impl.ClientPoolImpl
import net.notjustanna.utils.ipc.proto.DefaultProtocol
import net.notjustanna.utils.ipc.proto.Protocol

abstract class ClientBuilder<T> {
    private var protocol: Protocol = DefaultProtocol

    open fun protocol(protocol: Protocol) = apply {
        this.protocol = protocol
    }

    protected abstract fun buildConnector(): ClientConnector<T>

    fun build(): IClient<T> = ClientImpl(protocol, buildConnector())

    fun buildPool(queueSize: Int = Int.MAX_VALUE, buildInitial: Boolean = true): IClientPool<T> = ClientPoolImpl(protocol, buildConnector(), queueSize, buildInitial)
}

