;buildInfoPackage: chisel3, version: 3.2.4, scalaVersion: 2.12.10, sbtVersion: 1.3.2
circuit FindMax : 
  module FindMax : 
    input clock : Clock
    input reset : UInt<1>
    output io : {flip in1 : UInt<10>, flip in2 : UInt<10>, out : UInt<10>}
    
    reg max : UInt<10>, clock with : (reset => (reset, UInt<10>("h00"))) @[FindMax.scala 22:22]
    reg max2 : UInt<10>, clock with : (reset => (reset, UInt<10>("h00"))) @[FindMax.scala 23:23]
    max2 <= io.in1 @[FindMax.scala 25:10]
    max2 <= io.in2 @[FindMax.scala 26:10]
    node _T = gt(io.in1, io.in2) @[FindMax.scala 28:17]
    when _T : @[FindMax.scala 28:27]
      max <= io.in1 @[FindMax.scala 29:13]
      skip @[FindMax.scala 28:27]
    else : @[FindMax.scala 30:18]
      max <= io.in2 @[FindMax.scala 31:13]
      skip @[FindMax.scala 30:18]
    io.out <= max2 @[FindMax.scala 34:12]
    
