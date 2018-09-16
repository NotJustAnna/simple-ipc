package net.notjustanna.utils.ipc.server.dsl

import net.notjustanna.utils.io.DataPipe
import net.notjustanna.utils.ipc.proto.Protocol
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

open class ServerBuilder<T>(private val protocol: Protocol) {
    companion object {
        val count = AtomicInteger()
    }

    /**
     * Name of the Server
     */
    var serverName: String = "server=${count.getAndIncrement()}"

    /**
     * Executor creator
     */
    var executor: () -> ExecutorService = {
        Executors.newCachedThreadPool {
            thread(start = false, name = "$serverName/ExecutingThread-%d", block = it::run)
        }
    }

    /**
     * Map used to resolve the calls to their handlers.
     */
    var calls: MutableMap<String, T.(DataPipe) -> Unit> = LinkedHashMap()

    /**
     * Map used to resolve unknown opcode calls to their handlers.
     */
    var extensions: MutableMap<Byte, T.(DataPipe) -> Unit> = LinkedHashMap()

    /**
     * Registers a call and its handler to the map.
     */
    fun call(key: String, block: T.(DataPipe) -> Unit) {
        check(!calls.contains(key)) { "Key '$key' already exists." }
        calls[key] = block
    }

    /**
     * Registers an extension and its handler to the map.
     */
    fun extension(opcode: Byte, block: T.(DataPipe) -> Unit) {
        check(opcode != protocol.opExit.toByte() || opcode != protocol.opCall.toByte() || opcode != protocol.opList.toByte() || opcode != protocol.opListExt.toByte()) {
            "Opcode '0x${opcode.toString(16)}' is a reserved opcode by the Protocol."
        }
        check(!extensions.contains(opcode)) { "Opcode '0x${opcode.toString(16)}' already exists." }
        extensions[opcode] = block
    }
}