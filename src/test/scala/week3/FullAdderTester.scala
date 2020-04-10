package week3

import chisel3.iotesters.PeekPokeTester

class FullAdderTester(c: HalfFullAdder) extends PeekPokeTester(c) {
  require(c.hasCarry, "DUT must be half adder")
  poke(c.io.carryIn.get, 0)
  // 0 + 0 + 0 = 0
  poke(c.io.a, 0)
  poke(c.io.b, 0)
  expect(c.io.s, 0)
  expect(c.io.carryOut, 0)
  // 0 + 0 + 1 = 1
  poke(c.io.b, 1)
  expect(c.io.s, 1)
  expect(c.io.carryOut, 0)
  // 0 + 1 + 1 = 2
  poke(c.io.a, 1)
  expect(c.io.s, 0)
  expect(c.io.carryOut, 1)
  // 0 + 1 + 0 = 1
  poke(c.io.b, 0)
  expect(c.io.s, 1)
  expect(c.io.carryOut, 0)

  poke(c.io.carryIn.get, 1)
  // 1 + 0 + 0 = 1
  poke(c.io.a, 0)
  poke(c.io.b, 0)
  expect(c.io.s, 1)
  expect(c.io.carryOut, 0)
  // 1 + 0 + 1 = 2
  poke(c.io.b, 1)
  expect(c.io.s, 0)
  expect(c.io.carryOut, 1)
  // 1 + 1 + 1 = 3
  poke(c.io.a, 1)
  expect(c.io.s, 1)
  expect(c.io.carryOut, 1)
  // 1 + 1 + 0 = 2
  poke(c.io.b, 0)
  expect(c.io.s, 0)
  expect(c.io.carryOut, 1)
}
