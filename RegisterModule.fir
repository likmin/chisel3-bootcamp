;buildInfoPackage: chisel3, version: 3.2.4, scalaVersion: 2.12.10, sbtVersion: 1.3.2
circuit RegisterModule : 
  module RegisterModule : 
    input clock : Clock
    input reset : UInt<1>
    output io : {flip in : UInt<12>, out : UInt<12>}
    
    reg myReg : UInt<2>, clock @[RegisterModule.scala 19:20]
    node _T = add(io.in, UInt<1>("h01")) @[RegisterModule.scala 21:29]
    node _T_1 = tail(_T, 1) @[RegisterModule.scala 21:29]
    reg _T_2 : UInt, clock @[RegisterModule.scala 21:22]
    _T_2 <= _T_1 @[RegisterModule.scala 21:22]
    io.out <= _T_2 @[RegisterModule.scala 21:12]
    
