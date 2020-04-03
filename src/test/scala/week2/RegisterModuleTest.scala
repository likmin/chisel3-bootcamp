package week2

import chisel3.iotesters.PeekPokeTester
 
class RegisterModuleTest(c: RegisterModule) extends PeekPokeTester(c) {
    // TODO: happy coding! (created by vscode extension new-file-by-type)
  val maxnumber = (1 << 12)

  for(i <- 0 until maxnumber) {
    poke(c.io.in, i)
    step(1)
    expect(c.io.out, (i + 1) % maxnumber)
  }
}