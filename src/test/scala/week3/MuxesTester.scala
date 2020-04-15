package week3

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

/**
  * 这什么玩意
  */
object MuxesTester {
    def apply() {

        Driver(() => new Module {
            // Example circuit using PriorityMux
            val io = IO(new Bundle {
                val in_sels = Input(Vec(2, Bool()))
                val in_bits = Input(Vec(2, UInt(8.W)))
                val out = Output(UInt(8.W))
            })
            io.out := PriorityMux(io.in_sels, io.in_bits)
        }) { c => new PeekPokeTester(c) {
            poke(c.io.in_bits(0), 10)
            poke(c.io.in_bits(1), 20)
        
            // Select higher index only
            // [info] [0.008] in_sels=Vector(0, 1), out=20
            poke(c.io.in_sels(0), 0)
            poke(c.io.in_sels(1), 1)
            println(s"in_sels=${peek(c.io.in_sels)}, out=${peek(c.io.out)}")
        
            // Select both - arbitration needed
            // [info] [0.008] in_sels=Vector(1, 1), out=10
            poke(c.io.in_sels(0), 1)
            poke(c.io.in_sels(1), 1)
            println(s"in_sels=${peek(c.io.in_sels)}, out=${peek(c.io.out)}")
        
            // Select lower index only
            // [info] [0.008] in_sels=Vector(1, 0), out=10
            poke(c.io.in_sels(0), 1)
            poke(c.io.in_sels(1), 0)
            println(s"in_sels=${peek(c.io.in_sels)}, out=${peek(c.io.out)}")
            } 
        }

        Driver(() => new Module {
            // Example circuit using Mux1H
            val io = IO(new Bundle {
                val in_sels = Input(Vec(2, Bool()))
                val in_bits = Input(Vec(2, UInt(8.W)))
                val out = Output(UInt(8.W))
            })
            io.out := Mux1H(io.in_sels, io.in_bits)
        }) { c => new PeekPokeTester(c) {
            poke(c.io.in_bits(0), 10)
            poke(c.io.in_bits(1), 20)
        
            // Select index 1
            // in_sels=Vector(0, 1), out=20
            poke(c.io.in_sels(0), 0)
            poke(c.io.in_sels(1), 1)
            println(s"in_sels=${peek(c.io.in_sels)}, out=${peek(c.io.out)}")


            
            // Select index 0
            // in_sels=Vector(1, 0), out=10
            poke(c.io.in_sels(0), 1)
            poke(c.io.in_sels(1), 0)
            println(s"in_sels=${peek(c.io.in_sels)}, out=${peek(c.io.out)}")
        
            // Select none (invalid)
            // in_sels=Vector(0, 0), out=0
            poke(c.io.in_sels(0), 0)
            poke(c.io.in_sels(1), 0)
            println(s"in_sels=${peek(c.io.in_sels)}, out=${peek(c.io.out)}")
        
            // Select both (invalid)
            // in_sels=Vector(1, 1), out=30
            poke(c.io.in_sels(0), 1)
            poke(c.io.in_sels(1), 1)
            println(s"in_sels=${peek(c.io.in_sels)}, out=${peek(c.io.out)}")
            } 
        }
    }
}