package week2

import chisel3._

/**
  * 2.4_sequential_logic
  * Control Flow
  * Example: Register Control Flow
  *
  * Registers和Wires在控制流上非常相似：
  * 1.Registers也有last connect
  *   最后一个赋值是有效的，但是与wire不同的是，Register的赋值语句是在always模块中的，而wire不是
  * 2.可以用when,elsewhen,otherwise有条件的被赋值
  *   Register条件赋值会被转化成一堆“与或语句”，而这里会转会为always模块中的if...else语句
  */

class FindMax extends Module {
    val io = IO(new Bundle{
        val in1 = Input(UInt(10.W))
        val in2 = Input(UInt(10.W))
        val out = Output(UInt(10.W))
    })

    val max = RegInit(0.U(10.W))
    val max2 = RegInit(0.U(10.W))

    max2 := io.in1
    max2 := io.in2

    /** Register 也有last connect
      always @(posedge clock) begin
        if (reset) begin
            max2 <= 10'h0;
        end else begin
            max2 <= io_in2;
        end
      end
      */


    when(io.in1 > io.in2) {
        max := io.in1
    } .otherwise {
        max := io.in2
    }

    /** 
      always @(posedge clock) begin
        if (reset) begin
            max <= 10'h0;
        end else begin
            max <= io_in1;
        end else begin
            max <= io_in2;
        end
      end
      
      */

    /**
      * 如果这里是max，那么Verilog代码片段1将不会生成
      * 如果这里是max2，那么Verilog代码片段2同样也不会生成
      */
    io.out := max2
}