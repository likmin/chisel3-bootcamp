package week3

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

object QueueTester {
    def apply(){
        Driver(() => new Module {
            // Example circuit using a Queue
            val io = IO(new Bundle {
            val in = Flipped(Decoupled(UInt(8.W)))
            val out = Decoupled(UInt(8.W))
            })
            val queue = Queue(io.in, 2)  // 2-element queue
            io.out <> queue
            }) { c => new PeekPokeTester(c) {
                // Example testsequence showing the use and behavior of Queue
                poke(c.io.out.ready, 0)
                poke(c.io.in.valid, 1)  // Enqueue an element
                poke(c.io.in.bits, 42)
                println(s"Starting:")
                println(s"\tio.in: ready=${peek(c.io.in.ready)}")
                println(s"\tio.out: valid=${peek(c.io.out.valid)}, bits=${peek(c.io.out.bits)}")
                /**
                  * io.in:  ready = 1 这表明queue已经准备好接收数据了
                  * io.out: valid = 0, bits = 0 此时刚要输入，queue中还没有元素，所以c.io.out.valid为0，即bits尚未准备好
                  *
                  */
                step(1)
            


                poke(c.io.in.valid, 1)  // Enqueue another element
                poke(c.io.in.bits, 43)
                // What do you think io.out.valid and io.out.bits will be?
                println(s"After first enqueue:")
                println(s"\tio.in: ready=${peek(c.io.in.ready)}")
                println(s"\tio.out: valid=${peek(c.io.out.valid)}, bits=${peek(c.io.out.bits)}")
                /**
                  * io.in:  ready = 1 queue已经准备好接收数据了
                  * io.out: valid = 1, bits = 42 当前queue队头的数据为42，且有效
                  *
                  */
                step(1)
            


                poke(c.io.in.valid, 1)  // Read a element, attempt to enqueue
                poke(c.io.in.bits, 44)
                poke(c.io.out.ready, 1)
                // What do you think io.in.ready will be, and will this enqueue succeed, and what will be read?
                println(s"On first read:")
                println(s"\tio.in: ready=${peek(c.io.in.ready)}")
                println(s"\tio.out: valid=${peek(c.io.out.valid)}, bits=${peek(c.io.out.bits)}")
                /**
                  * io.in:  ready = 0 queue中队列已满，无法再接收数据
                  * io.out  valid = 1, bits = 42 queue队头数据为42
                  */

                step(1)
            
                poke(c.io.in.valid, 0)  // Read elements out
                poke(c.io.out.ready, 1)
                // What do you think will be read here?
                println(s"On second read:")
                println(s"\tio.in: ready=${peek(c.io.in.ready)}")
                println(s"\tio.out: valid=${peek(c.io.out.valid)}, bits=${peek(c.io.out.bits)}")
                /**
                  * io.in:  ready = 1
                  * io.out: valid = 1, bits = 43
                  */
                step(1)
            
                // Will a third read produce anything? No, the queue is empty now
                println(s"On third read:")
                println(s"\tio.in: ready=${peek(c.io.in.ready)}")
                println(s"\tio.out: valid=${peek(c.io.out.valid)}, bits=${peek(c.io.out.bits)}")
                /**
                  * io.in:  ready = 1
                  * io.out: valid = 0, bits = 42 queue已空，无法再出队列了
                  */
                step(1)
            } 
        }
    }
}