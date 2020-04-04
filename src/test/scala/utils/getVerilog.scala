package utils

import week2._

/**
  * 用于生成Verilog代码，调用方法：
  * 1.打开sbt
  * 2.输入
  *   {{{
  *   test:runMain utils.getVerilog 文件名
  *   }}}
  *
  *   @example:要生成Sort4.scala的Verilog代码，输入
  *   {{{
  *   test:runMain utils.getVerilog sort4 //大小写无所谓
  *   }}}
  *  
  * 可以自行添加想要生成Verilog代码的文件名
  *
  * TODO: 指定Verilog代码的生成位置 
  */


object getVerilog {
  def main(args: Array[String]): Unit = {

    args(0) toLowerCase match {
      case "sort4"              => chisel3.Driver.emitVerilog(new Sort4)
      case "lastconnect"        => chisel3.Driver.emitVerilog(new LastConnect)
      case "registermodule"     => chisel3.Driver.emitVerilog(new RegisterModule)
      case "findmax"            => chisel3.Driver.emitVerilog(new FindMax)
      case "clockexamples"      => chisel3.Driver.emitVerilog(new ClockExamples)
      case "mymanydynamicelementvecfir" => chisel3.Driver.emitVerilog(new MyManyDynamicElementVecFir(length = 16))
    }

  }
}