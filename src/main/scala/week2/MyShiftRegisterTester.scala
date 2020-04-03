package week2

import chisel3.iotesters.PeekPokeTester

class MyShiftRegisterTester(c: MyShiftRegister) extends PeekPokeTester(c) {

  val inSeq = Seq(0, 1, 1, 1, 0, 1, 1, 0, 0, 1)
  var state = c.init
  var i = 0
  poke(c.io.en, 1)

  while (i < 10 * c.n) {
      
      val toPoke = inSeq(i % inSeq.length)
      poke(c.io.in, toPoke)

      // 更新期待的state
      state = ((state * 2) + toPoke) & BigInt("1"*c.n, 2)

      /**
        * 这个BigInt("1"*c.n, 2)得到的是位宽为n的最大值，比如说4的时候得15
        * 目的是为了截取n为宽的state。
        * 
        * Scala找的的BigInt定义和这里不太一样：
        * Scala中的BigInt定义在了scala.collection.immutable.Range中，
        * 主要用于定义等比序列的。
        * 
        * Chisel中也没有相关的定义，那BigInt在哪呢？
        *
        * TODO: 找到BigInt定义的地方，
        */
      println("[myshiftregistertester info] = " + BigInt("1"*c.n, 2))
  
      step(1)
      expect(c.io.out, state)

      i += 1
  }
 
}