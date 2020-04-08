package week2

import chisel3._
import chisel3.util._
import chisel3.experimental._
import chisel3.experimental.BundleLiterals._
import chisel3.tester._
import chisel3.tester.RawTester.test

object DecoupledGcdTester{
    def apply() {
        test(new DecoupledGcd(16)) { dut =>
            dut.input.initSource().setSourceClock(dut.clock)
            dut.output.initSink().setSinkClock(dut.clock)

            val testValues = for { x <- 1 to 10; y <- 1 to 10} yield (x, y)
            val inputSeq = testValues.map { case (x, y) =>
                (new GcdInputBundle(16)).Lit(_.value1 -> x.U, _.value2 -> y.U)
            }
            val resultSeq = testValues.map { case (x, y) =>
                new GcdOutputBundle(16).Lit(_.value1 -> x.U, _.value2 -> y.U, _.gcd -> BigInt(x).gcd(BigInt(y)).U)
            }

            fork {
                dut.input.enqueueSeq(inputSeq)
            }.fork {
                dut.output.expectDequeueSeq(resultSeq)
            }.join()
        }
    }
}