package week2

import chisel3.iotesters.{TesterOptionsManager, Driver, PeekPokeTester}

object My4ElementFirTester {
      def apply() {
        println("Test for My4Element start!")

        println("Test 1 : Simple sanity check: a element with all zero coefficients should always produce zero")

        Driver(() => new My4ElementFir(0, 0, 0, 0)) {
            c => new PeekPokeTester(c) {
            poke(c.io.in, 0)
            expect(c.io.out, 0)
            step(1)
            poke(c.io.in, 4)
            expect(c.io.out, 0)
            step(1)
            poke(c.io.in, 5)
            expect(c.io.out, 0)
            step(1)
            poke(c.io.in, 2)
            expect(c.io.out, 0)
            }
        }
        
        println("Test 2 : Simple 4-point moving average")
        Driver(() => new My4ElementFir(1, 1, 1, 1)) {
            c => new PeekPokeTester(c) {
            poke(c.io.in, 1)
            expect(c.io.out, 1)  // 1, 0, 0, 0
            step(1)
            poke(c.io.in, 4)
            expect(c.io.out, 5)  // 4, 1, 0, 0
            step(1)
            poke(c.io.in, 3)
            expect(c.io.out, 8)  // 3, 4, 1, 0
            step(1)
            poke(c.io.in, 2)
            expect(c.io.out, 10)  // 2, 3, 4, 1
            step(1)
            poke(c.io.in, 7)
            expect(c.io.out, 16)  // 7, 2, 3, 4
            step(1)
            poke(c.io.in, 0)
            expect(c.io.out, 12)  // 0, 7, 2, 3
            }
        }

        println("Test 3 : Nonsymmetric filter")
        Driver(() => new My4ElementFir(1, 2, 3, 4)) {
            c => new PeekPokeTester(c) {
            poke(c.io.in, 1)
            expect(c.io.out, 1)  // 1*1, 0*2, 0*3, 0*4
            step(1)
            poke(c.io.in, 4)
            expect(c.io.out, 6)  // 4*1, 1*2, 0*3, 0*4
            step(1)
            poke(c.io.in, 3)
            expect(c.io.out, 14)  // 3*1, 4*2, 1*3, 0*4
            step(1)
            poke(c.io.in, 2)
            expect(c.io.out, 24)  // 2*1, 3*2, 4*3, 1*4
            step(1)
            poke(c.io.in, 7)
            expect(c.io.out, 36)  // 7*1, 2*2, 3*3, 4*4
            step(1)
            poke(c.io.in, 0)
            expect(c.io.out, 32)  // 0*1, 7*2, 2*3, 3*4
            }
        }
        println("Test for My4Element end!")
      }
}