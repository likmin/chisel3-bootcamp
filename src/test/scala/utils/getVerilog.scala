package utils

import week2._
import week3._
import chisel3._
import chisel3.experimental._
import dsptools.numbers._
import VerbosityImplicit._
import week4.DelayBy2
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

  // used to test BinaryMealy start！
  // example from https://en.wikipedia.org/wiki/Mealy_machine
  def stateTransition(state: Int, in: Boolean): Int = if(in) 1 else 0

  def output(state: Int, in: Boolean): Int = {
     if (state == 2) {
          return 0
     }
     if ((state == 1 && !in) || (state == 0 && in)) {
         return 1
     } else {
         return 0
     }
  }

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
      case "my4elementfir"      => chisel3.Driver.emitVerilog(new week2.My4ElementFir(1, 2, 3, 4))
      case "polynomial"         => chisel3.Driver.emitVerilog(new Polynomial)
      case "myshiftregister"    => chisel3.Driver.emitVerilog(new MyShiftRegister(n = 4))
      case "mymodule"           => chisel3.Driver.emitVerilog(new MyModule)
      // week 3

      case "parameterizedwidthadder"      => chisel3.Driver.emitVerilog(new ParameterizedWidthAdder(1, 4, 6))
      case "delayby1"                     => chisel3.Driver.emitVerilog(new DelayBy1)
      case "halffulladderwithcarry"       => chisel3.Driver.emitVerilog(new HalfFullAdder(hasCarry = true))
      case "halffulladderwithoutcarry"    => chisel3.Driver.emitVerilog(new HalfFullAdder(hasCarry = false))

      case "parameterizedwidthadderwithverbosity" => chisel3.Driver.emitVerilog(new ParameterizedWidthAdder(1, 4, 6)(Verbose))
      case "binarymealy"                  => chisel3.Driver.emitVerilog(new BinaryMealy(BinaryMealyParams(nStates = 3, s0 = 2, stateTransition, output)))
      case "badtypemodule"                => chisel3.Driver.emitVerilog(new BadTypeModule)
      case "macforuint"                   => chisel3.Driver.emitVerilog(new Mac(UInt(4.W), UInt(6.W)))
      case "macforsint"                   => chisel3.Driver.emitVerilog(new Mac(SInt(4.W), SInt(6.W)))
      case "macforfixedpoint"             => chisel3.Driver.emitVerilog(new Mac(FixedPoint(4.W, 3.BP), FixedPoint(6.W, 4.BP)))

      case "delayby2"                     => chisel3.Driver.emitVerilog(new DelayBy2(4))
    }

  }
}