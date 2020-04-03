package week2

import chisel3.iotesters.PeekPokeTester

class MyOperatorsTester(c: MyOperators) extends PeekPokeTester(c) {
  expect(c.io.out_add, 5)
  expect(c.io.out_sub, 1)
  expect(c.io.out_mul, 8)
}
