;buildInfoPackage: chisel3, version: 3.2.4, scalaVersion: 2.12.10, sbtVersion: 1.3.2
circuit ClockExamples : 
  module ClockExamples : 
    input clock : Clock
    input reset : UInt<1>
    output io : {flip in : UInt<10>, flip alternateReset : UInt<1>, flip alternateClock : Clock, outImplicit : UInt, outAlternateReset : UInt, outAlternateClock : UInt, outAlternateBoth : UInt}
    
    reg imp : UInt<10>, clock with : (reset => (reset, UInt<10>("h00"))) @[ClockExamples.scala 34:22]
    imp <= io.in @[ClockExamples.scala 35:9]
    io.outImplicit <= imp @[ClockExamples.scala 36:20]
    reg _T : UInt<10>, clock with : (reset => (io.alternateReset, UInt<10>("h00"))) @[ClockExamples.scala 39:29]
    _T <= io.in @[ClockExamples.scala 40:16]
    io.outAlternateReset <= _T @[ClockExamples.scala 41:30]
    reg _T_1 : UInt<10>, io.alternateClock with : (reset => (reset, UInt<10>("h00"))) @[ClockExamples.scala 45:29]
    _T_1 <= io.in @[ClockExamples.scala 46:16]
    io.outAlternateClock <= _T_1 @[ClockExamples.scala 47:30]
    reg _T_2 : UInt<10>, io.alternateClock with : (reset => (io.alternateReset, UInt<10>("h00"))) @[ClockExamples.scala 51:26]
    _T_2 <= io.in @[ClockExamples.scala 52:13]
    io.outAlternateBoth <= _T_2 @[ClockExamples.scala 53:29]
    
