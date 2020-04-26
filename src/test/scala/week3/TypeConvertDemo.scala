package week3

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class TypeConvertDemo extends Module {
    val io = IO(new Bundle {
        val in = Input(UInt(4.W))
        val out = Output(SInt(4.W))
    })

    io.out := io.in.asTypeOf(io.out)
}

object TypeConvertDemo {
    def apply() {

        val x: UInt = 3.U

        try {
            println(x.asInstanceOf[Int])
        } catch {
            case e: java.lang.ClassCastException => println("As expected, we can't cast UInt to Int")
        }

        println(x.asInstanceOf[Data])


        Driver(() => new TypeConvertDemo) {
            c => new PeekPokeTester(c) {
                poke(c.io.in, 3)
                expect(c.io.out, 3)
                poke(c.io.in, 15)
                expect(c.io.out, -1)
            }
        }
    }
}