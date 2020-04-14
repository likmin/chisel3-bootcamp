package week3

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

object ArbiterTester {
    def apply(){
        Driver(() => new Module {
            // Example circuit using a priority arbiter
            val io = IO(new Bundle {
                val in = Flipped(Vec(2, Decoupled(UInt(8.W)))) // 两个输入
                val out = Decoupled(UInt(8.W))                 // 一个输出
            })

            // Arbiter doesn't have a convenience constructor, so it's built like any Module
            val arbiter = Module(new Arbiter(UInt(8.W), 2))  // 2 to 1 Priority Arbiter

            arbiter.io.in <> io.in
            io.out <> arbiter.io.out

        }) { c => new PeekPokeTester(c) {
                poke(c.io.in(0).valid, 0)
                poke(c.io.in(1).valid, 0)
                println(s"Start:")
                println(s"\tin(0).ready=${peek(c.io.in(0).ready)}, in(1).ready=${peek(c.io.in(1).ready)}")
                println(s"\tout.valid=${peek(c.io.out.valid)}, out.bits=${peek(c.io.out.bits)}")
                /**
                  * in(0).ready = 1, in(1).ready = 1 两个输入端都没有数据占据着，所以ready都为1
                  * out.valid = 0, out.bits = 39 两个输入端都没有数据，当然valid = 0了
                  */
                

                poke(c.io.in(1).valid, 1)  // Valid input 1
                poke(c.io.in(1).bits, 42)
                // What do you think the output will be?
                println(s"valid input 1:")
                println(s"\tin(0).ready=${peek(c.io.in(0).ready)}, in(1).ready=${peek(c.io.in(1).ready)}")
                println(s"\tout.valid=${peek(c.io.out.valid)}, out.bits=${peek(c.io.out.bits)}")
                /**
                  * in(0).ready = 1, in(1).ready = 1
                  * out.valid = 1, out.bits = 42 输入端1有数据了，输出端也便可以输出了
                  */


                poke(c.io.in(0).valid, 1)  // Valid inputs 0 and 1
                poke(c.io.in(0).bits, 43)
                // What do you think the output will be? Which inputs will be ready?
                println(s"valid inputs 0 and 1:")
                println(s"\tin(0).ready=${peek(c.io.in(0).ready)}, in(1).ready=${peek(c.io.in(1).ready)}")
                println(s"\tout.valid=${peek(c.io.out.valid)}, out.bits=${peek(c.io.out.bits)}")
                /**
                  * in(0).ready = 1, in(1).ready = 0
                  * out.valid = 1, out.bits = 43 系数低的优先级高，所以有限输出in(0)
                  */


                poke(c.io.in(1).valid, 0)  // Valid input 0
                // What do you think the output will be?
                println(s"valid input 0:")
                println(s"\tin(0).ready=${peek(c.io.in(0).ready)}, in(1).ready=${peek(c.io.in(1).ready)}")
                println(s"\tout.valid=${peek(c.io.out.valid)}, out.bits=${peek(c.io.out.bits)}")
                /**
                  * in(0).ready = 1, in(1).ready = 0
                  * out.valid = 1, out.bits = 42 此时只有in(0)有效，所以输出in(0)的数据
                  */


            } 
        }
    }
}