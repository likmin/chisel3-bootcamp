module LastConnect(
  input        clock,
  input        reset,
  input  [3:0] io_in,
  output [3:0] io_out
);
  assign io_out = 4'h4; // @[LastConnect.scala 10:11 LastConnect.scala 11:11 LastConnect.scala 12:11 LastConnect.scala 13:11]
endmodule
