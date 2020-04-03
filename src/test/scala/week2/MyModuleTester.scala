package week2

import chisel3.iotesters.PeekPokeTester

class MyModuleTester(c: MyModule) extends PeekPokeTester(c) {
  chisel3.Driver.emitVerilog(new MyModule)
}
