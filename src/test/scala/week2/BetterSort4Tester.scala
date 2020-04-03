package week2

import chisel3.iotesters.PeekPokeTester

class BetterSort4Tester(c: Sort4) extends PeekPokeTester(c) {

  List(1, 2, 3, 4).permutations.foreach { case i0 :: i1 :: i2 :: i3 :: Nil => 
    println(s"Sorting $i0 $i1 $i2 $i3")
    poke(c.io.in0, i0)
    poke(c.io.in1, i1)
    poke(c.io.in2, i2)
    poke(c.io.in3, i3)
    expect(c.io.out0, 1)
    expect(c.io.out1, 2)
    expect(c.io.out2, 3)
    expect(c.io.out3, 4)
  }
  

}