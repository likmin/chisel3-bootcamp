package week2

import chisel3._

class MyBundle(width: Int) extends Bundle {
  val in = Input(UInt(width.W))
  val out = Output(UInt(width.W))
}
class MyModule extends Module{
  val io = IO(new MyBundle(4))

  io.out := io.in

  val two = 1 + 1
  println(Console.BLUE + "[MyModule info] " + two)
  val utwo = 1.U + 1.U
  println("[MyModule info] " + utwo)
}
