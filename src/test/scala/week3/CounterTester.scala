package week3

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

object CounterTester {
    def apply() {
        Driver(() => new Module {
            // Example circuit using Mux1H
            val io = IO(new Bundle {
                val count = Input(Bool())
                val out = Output(UInt(2.W))
            })
            val counter = Counter(3)  // 3-count Counter (outputs range [0...2])
            when(io.count) {
            counter.inc()
            }
            io.out := counter.value
        }) { c => new PeekPokeTester(c) {
            poke(c.io.count, 1)
            println(s"start: counter value=${peek(c.io.out)}")
        
            step(1)
            println(s"step 1: counter value=${peek(c.io.out)}")
        
            step(1)
            println(s"step 2: counter value=${peek(c.io.out)}")
        
            poke(c.io.count, 0)
            step(1)
            println(s"step without increment: counter value=${peek(c.io.out)}")
        
            poke(c.io.count, 1)
            step(1)
            println(s"step again: counter value=${peek(c.io.out)}")
            } 
        }
    }
}
