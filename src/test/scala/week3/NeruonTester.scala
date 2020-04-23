package week3

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import chisel3.experimental._
import chisel3.internal.firrtl.KnownBinaryPoint

object NeruonTester {
    def apply() {
        val Step: FixedPoint => FixedPoint = x => Mux(x <= 0.F(8.BP), 0.F(8.BP), 1.F(8.BP))
        val ReLU: FixedPoint => FixedPoint = x => Mux(x <= 0.F(8.BP), 0.F(8.BP), x)

        Driver(() => new Neuron(2, Step)) {
            c => new PeekPokeTester(c) {
                val inputs = Seq(Seq(-1, -1), Seq(-1, 1), Seq(1, -1), Seq(1, 1))

                val weights = Seq(1.0, 1.0)

                // push data through our Neuron and check the result (AND gate)
                reset(5)

                for(i <- inputs) {
                    pokeFixedPoint(c.io.in(0), i(0))
                    pokeFixedPoint(c.io.in(1), i(1))
                    pokeFixedPoint(c.io.weights(0), weights(0))
                    pokeFixedPoint(c.io.weights(1), weights(1))
                    expectFixedPoint(c.io.out, if(i(0) + i(1) > 0) 1 else 0, "ERROR")
                    step(1)
                }
            }
        }

        Driver(() => new Neuron(2, ReLU)) {
            c => new PeekPokeTester(c) {
                val inputs = Seq(Seq(-1, -1), Seq(-1, 1), Seq(1, -1), Seq(1, 1))

                val weights = Seq(1.0, 1.0)

                // push data through our Neuron and check the result (AND gate)
                reset(5)

                for(i <- inputs) {
                    pokeFixedPoint(c.io.in(0), i(0))
                    pokeFixedPoint(c.io.in(1), i(1))
                    pokeFixedPoint(c.io.weights(0), weights(0))
                    pokeFixedPoint(c.io.weights(1), weights(1))
                    expectFixedPoint(c.io.out, if(i(0) + i(1) > 0) 1 else 0, "ERROR")
                    step(1)
                }
            }
        }       
    }
}
