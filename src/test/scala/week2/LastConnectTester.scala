package week2

import chisel3.iotesters.PeekPokeTester

class LastConnectTester(c: LastConnect) extends PeekPokeTester(c){
  println("LastConnectTester Start")
  chisel3.Driver.emitVerilog(new LastConnect)
  expect(c.io.out, 4)
}
