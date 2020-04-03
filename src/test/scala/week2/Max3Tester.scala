package week2

import chisel3.iotesters.PeekPokeTester

class Max3Tester(c: Max3) extends PeekPokeTester(c){
  chisel3.Driver.emitVerilog(new Max3)
  poke(c.io.in1, 6)
  poke(c.io.in2, 4)
  poke(c.io.in3, 2)
  expect(c.io.out, 6)  // input 1 should be biggest
  poke(c.io.in2, 7)
  expect(c.io.out, 7)  // now input 2 is
  poke(c.io.in3, 11)
  expect(c.io.out, 11) // and now input 3
  poke(c.io.in3, 3)
  expect(c.io.out, 7)  // show that decreasing an input works as well
  poke(c.io.in1, 9)
  poke(c.io.in2, 9)
  poke(c.io.in3, 6)
  expect(c.io.out, 9)  // still get max with tie
}
