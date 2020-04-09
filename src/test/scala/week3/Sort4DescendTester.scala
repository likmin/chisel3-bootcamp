package week3

import chisel3.iotesters.PeekPokeTester

class Sort4DescendingTester(c: Sort4) extends PeekPokeTester(c) {
  poke(c.io.in0, 3)
  poke(c.io.in1, 6)
  poke(c.io.in2, 9)
  poke(c.io.in3, 12)
  expect(c.io.out0, 12)
  expect(c.io.out1, 9)
  expect(c.io.out2, 6)
  expect(c.io.out3, 3)

  poke(c.io.in0, 13)
  poke(c.io.in1, 4)
  poke(c.io.in2, 6)
  poke(c.io.in3, 1)
  expect(c.io.out0, 13)
  expect(c.io.out1, 6)
  expect(c.io.out2, 4)
  expect(c.io.out3, 1)
    
  poke(c.io.in0, 1)
  poke(c.io.in1, 6)
  poke(c.io.in2, 4)
  poke(c.io.in3, 13)
  expect(c.io.out0, 13)
  expect(c.io.out1, 6)
  expect(c.io.out2, 4)
  expect(c.io.out3, 1)

}