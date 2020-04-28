package week3

import chisel3._
import chisel3.util._
import chisel3.iotesters.PeekPokeTester

class IntegratorSIntTester(c: Integrator[SInt]) extends PeekPokeTester(c) {
    poke(c.io.in, 3)
    expect(c.io.out, 0)
    step(1)
    poke(c.io.in, -4)
    expect(c.io.out, 3)
    step(1)
    poke(c.io.in, 6)
    expect(c.io.out, -1)
    step(1)
    expect(c.io.out, 5)
}