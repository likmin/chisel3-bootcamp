package week3

import chisel3._

class Bundle1 extends Bundle {
    val a = UInt(8.W)
    override def cloneType = (new Bundle1).asInstanceOf[this.type]
}

class Bundle2 extends Bundle {
    val a = UInt(16.W)
    override def cloneType = (new Bundle2).asInstanceOf[this.type]
}

class BadTypeModule extends Module {
    val io = IO(new Bundle {
        val c   = Input(Clock())
        val in  = Input(UInt(2.W))
        val out = Output(Bool())

        val bundleIn = Input(new Bundle2)
        val bundleOut = Output(new Bundle1)
    })

    // io.out := io.c // illegal,different types

    io.out := io.in // io.in的宽度会变为1

    io.bundleOut := io.bundleIn
}