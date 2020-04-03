package week2

import chisel3._

/**
  * 2.4_sequential_logic
  * Registers
  * Example: Initialized Register
  *
  * RegInit的两种使用方法
  * 1.val myReg = RegInit(UInt(12.W), 0.U)
  *   两个参数，第一个参数为datatype，第二参数为hardware node
  * 2.val myReg = RegInit(0.U(12.W))
  *   一个参数，直接指明了reset时的值
  */

class RegInitModule extends Module {
  val io = IO(new Bundle {
    val in  = Input(UInt(12.W))
    val out = Output(UInt(12.W))
  })
  
  val register = RegInit(0.U(12.W)) 

  register := io.in + 1.U
  io.out := register
}