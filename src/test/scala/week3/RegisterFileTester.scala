package week3

import chisel3.iotesters.PeekPokeTester

import scala.util.Random

class RegisterFileTester(c: RegisterFile) extends PeekPokeTester(c) {
    def abs(x: Int): Int = if(x > 0) x else -x

    val initdata = Seq.fill(32){abs(Random.nextInt(1<<31 - 1))}
    
    poke(c.io.wen, 1)

    // 初始化寄存器
    for(i <- 1 until 32) {
        poke(c.io.waddr, i)
        poke(c.io.wdata, initdata(i))
        step(1)
    }

    poke(c.io.wen, 0)

    for(i <- 0 until c.readPorts) {
        for(j <- 1 until 32) {
            poke(c.io.raddr(i), j)
            expect(c.io.rdata(i), initdata(j))
            step(1)
        }
    }
}