package week3

import chisel3._
import chisel3.util._

class ConstantSum(in1: Data, in2: Data) extends Module {
    val io = IO(new Bundle {
        val out = Output(in1.cloneType)
    })

    (in1, in2) match {
        case (x: UInt, y: UInt) => io.out := x + y
        case (x: SInt, y: SInt) => io.out := x + y
        case _ => throw new Exception("I give up!")
    }
}
object ConstantSum {
    def apply() {
        println(chisel3.Driver.emitVerilog(new ConstantSum(3.U, 4.U)))
        /**
        module ConstantSum(
            input        clock,
            input        reset,
            output [1:0] io_out
        );| => chisel3-bootcamp / Test / runMain 1s
            assign io_out = 2'h3; // @[ConstantSum.scala 12:43]
        endmodule
        */
        println(chisel3.Driver.emitVerilog(new ConstantSum(-3.S, 4.S)))
        /**
        module ConstantSum(
            input        clock,
            input        reset,
            output [2:0] io_out
        );| => chisel3-bootcamp / Test / runMain 1s
            assign io_out = 3'sh1; // @[ConstantSum.scala 13:43]
        endmodule
        */
        println(chisel3.Driver.emitVerilog(new ConstantSum(3.U, 4.S)))
    }
}