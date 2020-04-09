package week3

import chisel3._
import chisel3.util._

/**
  * 3.1_parameters
  * Examples: Parameterized Chisel Object
  *
  *
  */

class ParameterizedWidthAdder(in0Width: Int, in1Width: Int, sumWidth: Int) extends Module {
    require(in0Width >= 0)
    require(in1Width >= 0)
    require(sumWidth >= 0)
    val io = IO(new Bundle {
        val in0 = Input(UInt(in0Width.W))
        val in1 = Input(UInt(in1Width.W))
        val sum = Output(UInt(sumWidth.W))
    })

    io.sum := io.in0 +& io.in1
}