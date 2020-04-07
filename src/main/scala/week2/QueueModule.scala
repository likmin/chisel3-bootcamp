package week2

import chisel3._
import chisel3.util._


class QueueModule[T <: Data](ioType: T, entries: Int) extends MultiIOModule {
    val in = IO(Flipped(Decoupled(ioType)))
    val out = IO(Decoupled(ioType))

    out <> Queue(in, entries)
}