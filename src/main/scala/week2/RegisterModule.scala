package week2

import chisel3._

/**
  * 2.4_sequential_logic
  * Registers
  * Example: Using a Register
  * Reg在上升沿之前保存着它的输出值，同时在上升沿的时候取值作为其输入
  */
class RegisterModule extends Module {
    val io = IO(new Bundle{
        val in = Input(UInt(12.W))
        val out = Output(UInt(12.W))
    })
    /**
      * 这里要区分types(比如 UInt )和hardware nodes(2.U或者myReg的输出)
      */
    val myReg = Reg(UInt(2.W))  /*正确！Reg需要一个data type作为model*/
    // val myReg2 = Reg(2.U)    /*错误！2.U以及两个是一个hardware(硬件节点)了，不可以用于model*/   
    io.out := RegNext(io.in + 1.U)
}

/**
  * 生成的Verilog
    module RegisterModule(
    input         clock,
    input         reset,
    input  [11:0] io_in,
    output [11:0] io_out
    );
    reg [11:0] _T_2; // @[RegisterModule.scala 17:22]
    reg [31:0] _RAND_0;
    assign io_out = _T_2; // @[RegisterModule.scala 17:12]
    `ifdef RANDOMIZE_GARBAGE_ASSIGN
    `define RANDOMIZE
    `endif
    `ifdef RANDOMIZE_INVALID_ASSIGN
    `define RANDOMIZE
    `endif
    `ifdef RANDOMIZE_REG_INIT
    `define RANDOMIZE
    `endif
    `ifdef RANDOMIZE_MEM_INIT
    `define RANDOMIZE
    `endif
    `ifndef RANDOM
    `define RANDOM $random
    `endif
    `ifdef RANDOMIZE_MEM_INIT
    integer initvar;
    `endif
    `ifndef SYNTHESIS
    initial begin
    `ifdef RANDOMIZE
        `ifdef INIT_RANDOM
        `INIT_RANDOM
        `endif
        `ifndef VERILATOR
        `ifdef RANDOMIZE_DELAY
            #`RANDOMIZE_DELAY begin end
        `else
            #0.002 begin end
        `endif
        `endif
    `ifdef RANDOMIZE_REG_INIT
    _RAND_0 = {1{`RANDOM}};
    _T_2 = _RAND_0[11:0];
    `endif // RANDOMIZE_REG_INIT
    `endif // RANDOMIZE
    end // initial
    `endif // SYNTHESIS
    always @(posedge clock) begin
        _T_2 <= io_in + 12'h1;
    end
    endmodule
  *
  */