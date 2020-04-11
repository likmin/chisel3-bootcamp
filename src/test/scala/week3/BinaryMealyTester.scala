package week3

import chisel3.iotesters.PeekPokeTester

class BinaryMealyTester(c: BinaryMealy) extends PeekPokeTester(c) {
  poke(c.io.in, false)
  expect(c.io.out, 0)
  step(1)
  poke(c.io.in, false)
  expect(c.io.out, 0)
  step(1)
  poke(c.io.in, false)
  expect(c.io.out, 0)
  step(1)
  poke(c.io.in, true)
  expect(c.io.out, 1)
  step(1)
  poke(c.io.in, true)
  expect(c.io.out, 0)
  step(1)
  poke(c.io.in, false)
  expect(c.io.out, 1)
  step(1)
  poke(c.io.in, true)
  expect(c.io.out, 1)
  step(1)
  poke(c.io.in, false)
  expect(c.io.out, 1)
  step(1)
  poke(c.io.in, true)
  expect(c.io.out, 1)
}