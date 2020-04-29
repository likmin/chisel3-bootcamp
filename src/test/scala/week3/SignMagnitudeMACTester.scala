package week3

import chisel3.iotesters.PeekPokeTester

class SignMagnitudeMACTester(c: Mac[SignMagnitude]) extends PeekPokeTester(c) {
  // 3 * 3 + 2 = 11
  poke(c.io.a.sign, 0)
  poke(c.io.a.magnitude, 3)
  poke(c.io.b.sign, 0)
  poke(c.io.b.magnitude, 3)
  poke(c.io.c.sign, 0)
  poke(c.io.c.magnitude, 2)
  expect(c.io.out.sign, 0)
  expect(c.io.out.magnitude, 11)

  // 3 * 3 - 2 = 7
  poke(c.io.c.sign, 1)
  expect(c.io.out.sign, 0)
  expect(c.io.out.magnitude, 7)

  // 3 * (-3) - 2 = -11
  poke(c.io.b.sign, 1)
  expect(c.io.out.sign, 1)
  expect(c.io.out.magnitude, 11)
}
