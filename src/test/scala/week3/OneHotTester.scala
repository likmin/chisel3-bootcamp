package week3

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

object OneHotTester {
    def apply() {
        Driver(() => new Module {
    // Example circuit using UIntToOH
            val io = IO(new Bundle {
                val in = Input(UInt(4.W))
                val out = Output(UInt(16.W))
            })
            io.out := UIntToOH(io.in)
        }) { c => new PeekPokeTester(c) {
            println("Test for UIntToOH")

            /**
              * [info] [0.004] in=0, out=0b1
              * [info] [0.004] in=1, out=0b10
              * [info] [0.004] in=8, out=0b100000000        
              * [info] [0.004] in=15, out=0b1000000000000000
              */

            poke(c.io.in, 0)
            println(s"in=${peek(c.io.in)}, out=0b${peek(c.io.out).toInt.toBinaryString} ")


            poke(c.io.in, 1)
            println(s"in=${peek(c.io.in)}, out=0b${peek(c.io.out).toInt.toBinaryString}")


        
            poke(c.io.in, 8)
            println(s"in=${peek(c.io.in)}, out=0b${peek(c.io.out).toInt.toBinaryString}")


        
            poke(c.io.in, 15)
            println(s"in=${peek(c.io.in)}, out=0b${peek(c.io.out).toInt.toBinaryString}")

            } 
        }

        println()

        Driver(() => new Module {
            // Example circuit using OHToUInt
            val io = IO(new Bundle {
                val in = Input(UInt(16.W))
                val out = Output(UInt(4.W))
            })
            io.out := OHToUInt(io.in)
        }) { c => new PeekPokeTester(c) {

            println("Test for OHToUInt")

            /**
              * [info] [0.004] in=0b1, out=0 
              * [info] [0.004] in=0b10000000, out=7
              * [info] [0.004] in=0b1000000000000001, out=15
              * [info] [0.008] in=0b0, out=0
              * [info] [0.008] in=0b1010000100000, out=15 // multiple high
              */

            poke(c.io.in, Integer.parseInt("0000 0000 0000 0001".replace(" ", ""), 2))
            println(s"in=0b${peek(c.io.in).toInt.toBinaryString}, out=${peek(c.io.out)} ")

            poke(c.io.in, Integer.parseInt("0000 0000 1000 0000".replace(" ", ""), 2))
            println(s"in=0b${peek(c.io.in).toInt.toBinaryString}, out=${peek(c.io.out)}")
        
            poke(c.io.in, Integer.parseInt("1000 0000 0000 0001".replace(" ", ""), 2))
            println(s"in=0b${peek(c.io.in).toInt.toBinaryString}, out=${peek(c.io.out)}")
        
            // Some invalid inputs:
            // None high
            poke(c.io.in, Integer.parseInt("0000 0000 0000 0000".replace(" ", ""), 2))
            println(s"in=0b${peek(c.io.in).toInt.toBinaryString}, out=${peek(c.io.out)}")
        
            // Multiple high
            poke(c.io.in, Integer.parseInt("0001 0100 0010 0000".replace(" ", ""), 2))
            println(s"in=0b${peek(c.io.in).toInt.toBinaryString}, out=${peek(c.io.out)}")
            
            } 
        }
    }
}