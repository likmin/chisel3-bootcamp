package week3

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class ShiftRegisterTester[T <: Bits](c: ShiftRegister[T]) extends PeekPokeTester(c) {
    println(s"Testing ShiftRegister of type ${c.io.in} and depth ${c.io.out.length}")

    for(i <- 0 until 10) {
        poke(c.io.in, i)
        println(s"$i: ${peek(c.io.out)}")
        step(1)
    }

}

object ShiftRegisterTester {
    def apply() {
        Driver(() => new ShiftRegister(UInt(4.W), 5)) {c => new ShiftRegisterTester(c)}
        Driver(() => new ShiftRegister(SInt(6.W), 3)) {c => new ShiftRegisterTester(c)}
    }
}
