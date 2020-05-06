package week2

import chisel3.iotesters.PeekPokeTester

class ParameterizedAdderTester(c: ParameterizedAdder, saturate: Boolean) extends PeekPokeTester(c) {
    val cycles = 100

    import scala.util.Random
    import scala.math.min

    for(i <- 0 until cycles) {
        val in_a = Random.nextInt(16)
        val in_b = Random.nextInt(16)
        poke(c.io.in_a, in_a)
        poke(c.io.in_b, in_b)
        if(saturate) {
            expect(c.io.out, min(in_a + in_b, 15))
        } else {
            expect(c.io.out, (in_a + in_b) % 16)
        }
    }

    poke(c.io.in_a, 15)
    poke(c.io.in_b, 15)
    if(saturate) {
        expect(c.io.out, 15)
    } else {
        expect(c.io.out, 14)
    }
}