package week2

import chisel3.iotesters.PeekPokeTester
import chisel3.printf

import scala.util.Random

class ArbiterTester(c: Arbiter) extends PeekPokeTester(c){

  val data = Random.nextInt(1<<16 - 1)
  poke(c.io.fifo_data, data)

  for (i <- 0 until 8) {
    poke(c.io.fifo_valid, (i>>0) % 2)
    poke(c.io.pe0_ready, (i>>1) % 2)
    poke(c.io.pe1_ready, (i>>2) % 2)

    expect(c.io.fifo_ready, i>1)
    expect(c.io.pe0_valid, i==3 || i==7)
    expect(c.io.pe1_valid, i==5)

    print("[arbiter info] i = " + i + " : c.io.fifo_valid: " + (i>>0)%2 + ", c.io.pe0_ready: " + (i>>1)%2 + ", c.io.pe1_ready: " + (i>>2)%2 + "\n")


    if (i == 3 || i == 7) {
      expect(c.io.pe0_data, data)
    } else if(i == 5) {
      expect(c.io.pe1_data, data)
    }
  }
}
