package week3

import chisel3._
import chisel3.experimental._
import dsptools.numbers._

class Mac[T <: Data : Ring](genIn: T, genOut: T) extends Module {
    val io = IO(new Bundle {
        val a = Input(genIn.cloneType)
        val b = Input(genIn.cloneType)
        val c = Input(genIn.cloneType)
        val out = Output(genOut.cloneType)
    })

    io.out := io.a * io.b + io.c
}
