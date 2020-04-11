package utils

import week2._
import week3._

import chisel3._

import VerbosityImplicit._
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
  * TODO: 1.指定Verilog代码的生成位置 
  *       2.将load-ivy.sc中的getVerilog搬运过来
  */


object getVerilog {
  def main(args: Array[String]): Unit = {

    args(0) toLowerCase match {
      // week 2
      case "sort4"              => chisel3.Driver.emitVerilog(new week2.Sort4)
      case "lastconnect"        => chisel3.Driver.emitVerilog(new LastConnect)
      case "registermodule"     => chisel3.Driver.emitVerilog(new RegisterModule)
      case "findmax"            => chisel3.Driver.emitVerilog(new FindMax)
      case "clockexamples"      => chisel3.Driver.emitVerilog(new ClockExamples)
      case "mymanydynamicelementvecfir" => chisel3.Driver.emitVerilog(new MyManyDynamicElementVecFir(length = 16))
      case "queuemodule"        => chisel3.Driver.emitVerilog(new QueueModule(UInt(9.W), 200))

      // week 3

      case "parameterizedwidthadder"      => chisel3.Driver.emitVerilog(new ParameterizedWidthAdder(1, 4, 6))
      case "delayby1"                     => chisel3.Driver.emitVerilog(new DelayBy1)
      case "halffulladderwithcarry"       => chisel3.Driver.emitVerilog(new HalfFullAdder(hasCarry = true))
      case "halffulladderwithoutcarry"    => chisel3.Driver.emitVerilog(new HalfFullAdder(hasCarry = false))

      case "parameterizedwidthadderwithverbosity" => chisel3.Driver.emitVerilog(new ParameterizedWidthAdder(1, 4, 6)(Verbose))
    }

  }
}