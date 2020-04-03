package week2

import chisel3._

class Passthrough () extends Module{
  val io = IO(new Bundle (){
    /**
     * TODO
     * 为什么import chisel3.UInt 后使用UInt不可以，必须使用chisel3才可以呢
     */
    val in = Input(UInt(4.W))
    val out = Output(UInt(4.W))
  })
  io.out := io.in
}

