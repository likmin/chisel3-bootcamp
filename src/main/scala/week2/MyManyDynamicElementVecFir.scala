package week2

import chisel3._

/**
  * 2.5_exercise
  * FIR(finite impulse response) Filter Generator
  * 8-bit Specification
  * 这里n是可定义的，bi在io.consts中
  * 𝑦[𝑛] = 𝑏0 * 𝑥[𝑛] + 𝑏1 * 𝑥[𝑛−1] + 𝑏2 * 𝑥[𝑛−2] + ...
  * 其中
  * 1.y[n]为第n的输出信号，x[n]为输入信号
  * 2.𝑏𝑖为滤波器的系数（coefficients）或脉冲响应（impulse response）
  * 3.n - 1, n - 2,...表示第n次延迟了1，2，3...个周期
  *    
  */

class MyManyDynamicElementVecFir(length: Int) extends Module {
  val io = IO(new Bundle {
    val in      = Input(UInt(8.W))  
    val valid   = Input(Bool())
    val out     = Output(UInt(8.W))
    val consts  = Input(Vec(length, UInt(8.W)))
  })

  /**
    * 初始化Taps，结果为
    * List(io.in, 0.U, ... )
    */
  val taps = Seq(io.in) ++ Seq.fill(io.consts.length - 1)(RegInit(0.U(8.W)))
  /**
    * taps.tail 为taps去除第一个元素组成的数组
    * taps.zip(taps.tail) 结果为 Seq((taps(0), taps(1)), (taps(1), taps(2)), ... , (taps(n - 1), taps(n)))
    * foreach{case (a, b) => when(io.valid) {b := a}} 表示，如果io.valid === true.B, taps(i) := tap(i + 1)
    */
  /**
    * @question b := a 是可变的？
    */
  taps.zip(taps.tail).foreach{case (a, b) => when(io.valid) {b := a}}

  /**
    * taps.zip(io.consts).map {case (a, b) => a * b} 将taps(i) * io.consts(i)
    * reduce(_ + _) 将List中的值依次相加
    */
  io.out := taps.zip(io.consts).map {case (a, b) => a * b}.reduce(_ + _)
}