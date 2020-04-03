package week2

import chisel3._

/**
  * 2.4_sequential_logic
  * Exercises
  * Exercise: Shift Register
  * 输入的值in将作为以为寄存器的下一个值
  * Note: 在Chisel中subword assignment是非法的，如out(0) := in将不会有效
  *
  */
class MyShiftRegister(val init: BigInt = 1, val n: Int) extends Module {
    val io = IO(new Bundle {
        val en = Input(Bool())
        val in = Input(Bool())
        val out = Output(UInt(n.W))
    })

    /**
      * 等价于
      * val state = RegInit(UInt(n.W), init.U)
      */
    val state = RegInit(init.U(n.W))
    val nextstate = state << 1 | io.in
    when(io.en) {
        state := nextstate
    }
    
    
    /**
    一次错误示例
    前面已经说了，subwords assignment是非法的
    [error] chisel3.internal.ChiselException: Cannot reassign to read-only Bool(OpResult in MyShiftRegister)
    
    when(io.in) {
        state(0) := 1.U
    } .otherwise {
        state(0) := 0.U
    }
    
    state(1) := state(0)
    state(2) := state(1)
    state(3) := state(2)
    */

    io.out := state
}