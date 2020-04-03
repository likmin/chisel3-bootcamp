package week2

import chisel3._

/**
  * 2.3_control_flow
  * Last Connect Semantics
  * Example: Reassignment
  * 如果有一个部件被多次使用:=赋值，那么最后一条语句的赋值将是有效的
  */
class LastConnect extends Module{
  val io = IO(new Bundle{
    val in  = Input(UInt(4.W))
    val out = Output(UInt(4.W))
  })
  io.out  :=  1.U
  io.out  :=  2.U
  io.out  :=  3.U
  io.out  :=  4.U
}

/**
  * 对应的Verilog代码
  module LastConnect(
    input        clock,
    input        reset,
    input  [3:0] io_in,
    output [3:0] io_out
  );
    assign io_out = 4'h4; // @[LastConnect.scala 10:11 LastConnect.scala 11:11 LastConnect.scala 12:11 LastConnect.scala 13:11]
  endmodule
*/