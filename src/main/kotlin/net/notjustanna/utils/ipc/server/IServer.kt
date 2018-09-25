package net.notjustanna.utils.ipc.server

import net.notjustanna.utils.ipc.client.IClient
import java.io.Closeable

/**
 * Represents a server that can be connected by [IClient]s.
 */
interface IServer : Closeable