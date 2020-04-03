package week2

import chisel3._

class MyOperatorsBundle(width: Int) extends Bundle {
  val in      = Input(UInt(width.W))
  val out_add = Output(UInt(width.W))
  val out_sub = Output(UInt(width.W))
  val out_mul = Output(UInt(width.W))
}
class MyOperators extends Module {
  val io = IO(new MyOperatorsBundle(4))

  io.out_add  := 1.U + 4.U
  io.out_sub  := 2.U - 1.U
  io.out_mul  := 4.U * 2.U
}
