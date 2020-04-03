package week2

import chisel3.iotesters.PeekPokeTester

class PassthroughGeneratorTester(p: PassthroughGenerator) extends PeekPokeTester(p){
  val numMax = 1 << p.io.out.getWidth // 获取当前测试部件的最大值
  println(s"[PassthroughGeneratorTester info] numMax = $numMax")
  for (i <- 0 until numMax) {
    poke(p.io.in, i)
    step(1)
    expect(p.io.out, i)
  }
  print(chisel3.Driver.emitVerilog(new PassthroughGenerator(10)))
}
