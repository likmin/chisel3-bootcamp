package week3

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

object ReverseTester {
    def apply() {
        Driver(() => new Module {
                // Example circuit using Reverse
                val io = IO(new Bundle {
                val in = Input(UInt(8.W))
                val out = Output(UInt(8.W))
                })
                io.out := Reverse(io.in)
            }) { c => new PeekPokeTester(c) {
                // Integer.parseInt is used create an Integer from a binary specification
                poke(c.io.in, Integer.parseInt("01010101", 2))
                println(s"in=0b${peek(c.io.in).toInt.toBinaryString}, out=0b${peek(c.io.out).toInt.toBinaryString}")
            
                poke(c.io.in, Integer.parseInt("00001111", 2))
                println(s"in=0b${peek(c.io.in).toInt.toBinaryString}, out=0b${peek(c.io.out).toInt.toBinaryString}")
            
                poke(c.io.in, Integer.parseInt("11110000", 2))
                println(s"in=0b${peek(c.io.in).toInt.toBinaryString}, out=0b${peek(c.io.out).toInt.toBinaryString}")
            
                poke(c.io.in, Integer.parseInt("11001010", 2))
                println(s"in=0b${peek(c.io.in).toInt.toBinaryString}, out=0b${peek(c.io.out).toInt.toBinaryString}")
            } 
        }
    }
}