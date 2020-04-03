package week2

import chisel3._
import chisel3.experimental.{withClock, withReset, withClockAndReset}

/**
  * 2.4_sequential_logic
  * Appendix: Explicit clock and reset
  * Chisel中有默认的clock和reset，但如果我们设计多时钟。
  * 我们有自己block可以生成clock和reset信号，这时我们可
  * 以使用chisel提供的方法来实现：
  *     1.withClock(){}
  *     2.withReset(){}
  *     3.withClockAndReset(){} // 前两种方法的结合
  * 
  * Note:
  *     1.reset的类型为Bool类型，而且是同步的(synchronous)
  *     2.Clock在Chisel中拥有自己的内心Clock.
  *     3.Bool类型可以通过asClock()转换为Clock类型，但是一般不建议这样做
  *     4.chisel-testers现在还不完全支持多时钟设计
  * 
  */
class ClockExamples extends Module {
    val io = IO(new Bundle {
        val in = Input(UInt(10.W))
        val alternateReset = Input(Bool())
        val alternateClock = Input(Clock())
        val outImplicit    = Output(UInt())
        val outAlternateReset = Output(UInt())
        val outAlternateClock = Output(UInt())
        val outAlternateBoth  = Output(UInt())
    })

    val imp = RegInit(0.U(10.W))
    imp := io.in
    io.outImplicit := imp

    withReset(io.alternateReset) {
        val altRst = RegInit(0.U(10.W))
        altRst := io.in
        io.outAlternateReset := altRst
    }   

    withClock(io.alternateClock) {
        val altClk = RegInit(0.U(10.W))
        altClk := io.in
        io.outAlternateClock := altClk
    }   

    withClockAndReset(io.alternateClock, io.alternateReset) {
        val alt = RegInit(0.U(10.W))
        alt := io.in
        io.outAlternateBoth := alt
    }

}