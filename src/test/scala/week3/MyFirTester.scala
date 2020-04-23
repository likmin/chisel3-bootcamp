package week3

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

import scala.math.{abs, round, cos, Pi, pow, sin}
import breeze.signal.{filter, OptOverhang}
import breeze.signal.support.{CanFilter, FIRKernel1D}
import breeze.linalg.DenseVector

object MyFirTester {

    def apply() {
        // TODO: https://en.wikipedia.org/wiki/Window_function. https://www.bilibili.com/video/BV1Ws411H7JY?p=45
        // simple triangular window
        val TriangularWindow: (Int, Int) => Seq[Int] = (length, bitwidth) => {
            val raw_coeffs = (0 until length).map( (x:Int) => 1-abs((x.toDouble-(length-1)/2.0)/((length-1)/2.0)) )
            val scaled_coeffs = raw_coeffs.map( (x: Double) => round(x * pow(2, bitwidth)).toInt)
            scaled_coeffs
        }

        // Hamming window
        val HammingWindow: (Int, Int) => Seq[Int] = (length, bitwidth) => {
            val raw_coeffs = (0 until length).map( (x: Int) => 0.54 - 0.46*cos(2*Pi*x/(length-1)))
            val scaled_coeffs = raw_coeffs.map( (x: Double) => round(x * pow(2, bitwidth)).toInt)
            scaled_coeffs
        }

        // test parameters
        val length = 7
        val bitwidth = 12 // must be less than 15, otherwise Int can't represent the data, need BigInt
        val window = TriangularWindow


        Driver( () => new MyFir(length, bitwidth, window)) {
            c => new PeekPokeTester(c) {
                // test data
                val n = 100 // input length
                val sine_freq = 10
                val samp_freq = 100

                // sample data, scale to between 0 and 2^bitwidth
                val max_value = pow(2, bitwidth) - 1
                val sine = (0 until n).map(i => (max_value / 2 + max_value / 2 * sin(2 * Pi * sine_freq / samp_freq * i)).toInt)
                println(s"input = ${sine.toArray.deep.mkString(", ")}")

                // coefficients
                val coeffs = window(length, bitwidth)
                println(s"coeffs = ${coeffs.toArray.deep.mkString(", ")}")

                val expected = filter(DenseVector(sine.toArray), 
                          FIRKernel1D(DenseVector(coeffs.reverse.toArray), 1.0, ""), 
                          OptOverhang.None)
                println(s"exp_out = ${expected.toArray.deep.mkString(", ")}")

                // push data through our FIR and check the result

                reset(5)
                for(i <- 0 until n) {
                    poke(c.io.in, sine(i))
                    if (i >= length - 1) {
                        expect(c.io.out, expected(i - length + 1))
                    }
                    step(1)
                }
            }
        }
    }
}