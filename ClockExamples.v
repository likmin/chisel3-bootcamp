module ClockExamples(
  input        clock,
  input        reset,
  input  [9:0] io_in,
  input        io_alternateReset,
  input        io_alternateClock,
  output [9:0] io_outImplicit,
  output [9:0] io_outAlternateReset,
  output [9:0] io_outAlternateClock,
  output [9:0] io_outAlternateBoth
);
  reg [9:0] imp; // @[ClockExamples.scala 34:22]
  reg [31:0] _RAND_0;
  reg [9:0] _T; // @[ClockExamples.scala 39:29]
  reg [31:0] _RAND_1;
  reg [9:0] _T_1; // @[ClockExamples.scala 45:29]
  reg [31:0] _RAND_2;
  reg [9:0] _T_2; // @[ClockExamples.scala 51:26]
  reg [31:0] _RAND_3;
  assign io_outImplicit = imp; // @[ClockExamples.scala 36:20]
  assign io_outAlternateReset = _T; // @[ClockExamples.scala 41:30]
  assign io_outAlternateClock = _T_1; // @[ClockExamples.scala 47:30]
  assign io_outAlternateBoth = _T_2; // @[ClockExamples.scala 53:29]
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
  imp = _RAND_0[9:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  _T = _RAND_1[9:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  _T_1 = _RAND_2[9:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  _T_2 = _RAND_3[9:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      imp <= 10'h0;
    end else begin
      imp <= io_in;
    end
    if (io_alternateReset) begin
      _T <= 10'h0;
    end else begin
      _T <= io_in;
    end
  end
  always @(posedge io_alternateClock) begin
    if (reset) begin
      _T_1 <= 10'h0;
    end else begin
      _T_1 <= io_in;
    end
    if (io_alternateReset) begin
      _T_2 <= 10'h0;
    end else begin
      _T_2 <= io_in;
    end
  end
endmodule
