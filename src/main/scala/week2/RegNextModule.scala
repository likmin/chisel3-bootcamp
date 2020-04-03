package week2

import chisel3._

/**
  * 2.4_sequential_logic
  * Registers
  * Example: RegNext
  * 和RegisterModule效果相同
  */

class RegNextModule extends Module {
  val io = IO(new Bundle {
    val in  = Input(UInt(12.W))
    val out = Output(UInt(12.W))
  })
  
  // register bitwidth is inferred from io.out
  io.out := RegNext(io.in + 1.U)
}