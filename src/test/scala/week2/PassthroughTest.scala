package week2

import chisel3.iotesters.PeekPokeTester

class PassthroughTester(c: Passthrough) extends PeekPokeTester(c){
  for (i <- 0 to 10) {
    poke(c.io.in, i)
    expect(c.io.out, i)
  }

}
