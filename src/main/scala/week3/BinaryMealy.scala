package week3

import chisel3._

/**
  * 3.1_parameters
  * Generator Example
  * Example: Mealy Machine
  * 
  * Mealy machine introduction: 
  *     一个有限状态机(finite-state machine),该状态机的输出值取决于当前的状态和当前的输入值，
  * 与之相对应的是Moore machine，Moore machine的输出值只取决于当前的状态。
  *     
  */

case class BinaryMealyParams (
    // number of states
    nStates: Int,
    // initial states
    s0: Int,
    // function describing state transition
    stateTransition: (Int, Boolean) => Int,
    // function describing output
    output: (Int, Boolean) => Int

) {
    require(nStates >= 0)
    require(s0 < nStates && s0 > 0)
}


class BinaryMealy(val mp: BinaryMealyParams) extends Module {
  val io = IO(new Bundle {
    val in = Input(Bool())
    val out = Output(UInt())
  })

  val state = RegInit(UInt(), mp.s0.U)

  // output zero if no states
  io.out := 0.U
  for (i <- 0 until mp.nStates) {
    when (state === i.U) {
      when (io.in) {
        state  := mp.stateTransition(i, true).U
        io.out := mp.output(i, true).U
      }.otherwise {
        state  := mp.stateTransition(i, false).U
        io.out := mp.output(i, false).U
      }
    }
  }
}