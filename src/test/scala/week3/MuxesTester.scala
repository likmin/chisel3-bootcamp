package week3

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

object MuxesTester {
    def apply() {

         Driver(() => new Module {
            // Example circuit using MuxCase
            val io = IO(new Bundle {
                val in_sels = Input(Vec(3, Bool()))
                val in_bits = Input(Vec(3, UInt(8.W)))
                val out = Output(UInt(8.W))
            })
            io.out := MuxCase(default = 0.U,mapping = io.in_sels zip io.in_bits)
        }) { c => new PeekPokeTester(c) {
            
                println(Console.BLUE + "Test for MuxCase")

                for(i <- 0 to 2) {
                    poke(c.io.in_bits(i), (i + 1) * 10)
                }

                var muxcase1 = Array(1,1,1)
                for(i <- 0 to 2) poke(c.io.in_sels(i),muxcase1(i))
                expect(c.io.out, 10)

                muxcase1 = Array(0,1,1)
                for(i <- 0 to 2) poke(c.io.in_sels(i),muxcase1(i))
                expect(c.io.out, 20)

                muxcase1 = Array(0,0,1)
                for(i <- 0 to 2) poke(c.io.in_sels(i),muxcase1(i))
                expect(c.io.out, 30)

                muxcase1 = Array(0,0,0)
                for(i <- 0 to 2) poke(c.io.in_sels(i),muxcase1(i))
                expect(c.io.out, 0)


            }
        }
        

        Driver(() => new Module {
            // Example circuit using MuxLoopUp
            val io = IO(new Bundle {
                val in_key  = Input(UInt(4.W)) 
                /** 注意了，in_sels的类型和MuxCase的类型不一样：
                  * MuxCase为Bool类型，这里就不一定了
                  */
                val in_sels = Input(Vec(3, UInt(4.W))) 
                val in_bits = Input(Vec(3, UInt(8.W)))
                val out = Output(UInt(8.W))
            })
            io.out := MuxLookup(key = io.in_key, default = 0.U, mapping = io.in_sels zip io.in_bits)
        }) { c => new PeekPokeTester(c) {

                println(Console.BLUE + "Test for MuxLookUp")

                for(i <- 0 to 2) {
                    poke(c.io.in_bits(i), (i + 1) * 10)
                }

                var muxlookup1 = Array(1, 2, 3)
                for(i <- 0 to 2) poke(c.io.in_sels(i), muxlookup1(i))
                
                poke(c.io.in_key, 2) // 寻找key为2的值
                expect(c.io.out, 20)

                poke(c.io.in_key, 4) // 如果没有匹配的,返回default
                expect(c.io.out, 0)

                var muxlookup2 = Array(1, 1, 3) // 如果有多个匹配的呢?返回第一个匹配的
                poke(c.io.in_key, 1)
                println(s"c.io.out = ${peek(c.io.out)} ") // 返回 10


            




            }
            
        }



        /** 在可以启用多个选择信号的假设下构建Mux树。优先选择第一个选择信号。
          */
        Driver(() => new Module {
            // Example circuit using PriorityMux
            val io = IO(new Bundle {
                val in_sels = Input(Vec(2, Bool()))
                val in_bits = Input(Vec(2, UInt(8.W)))
                val out = Output(UInt(8.W))
            })
            io.out := PriorityMux(io.in_sels, io.in_bits)
        }) { c => new PeekPokeTester(c) {

                println(Console.BLUE + "Test for PriorityMux")
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

                // TODO:选择器都为0选最后一个？
                poke(c.io.in_sels(0), 0)
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