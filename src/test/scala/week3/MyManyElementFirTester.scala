package week3

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}


object MyManyElementFirTester {

    def r():Int = scala.util.Random.nextInt(1024)

    def runOneTest(taps: Seq[Int]) {

        /**
          * ScalaFirFilter用Scala软件的形式实现了FIR，
          */
        val goldenModel = new ScalaFirFilter(taps)

        Driver(() => new MyManyElementFir(taps, 32)) {
            c => new PeekPokeTester(c) {
                for(i <- 0 until 2 * taps.length) {
                    val input = r()

                    val goldenModelResult = goldenModel.poke(input)

                    poke(c.io.in, input)

                    expect(c.io.out, goldenModelResult, s"i $i, input $input, gm $goldenModelResult, ${peek(c.io.out)}")

                    step(1)
                }
            }
        }
    }

    def apply() {
        for(tapSize <- 2 until 100 by 10) {
            val taps = Seq.fill(tapSize)(r())

            runOneTest(taps)
        }
    }
}