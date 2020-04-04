package week2

import chisel3._
import chisel3.util.ShiftRegister

/**
  * 2.5_exercise
  * FIR(finite impulse response) Filter 有限单位脉冲响应滤波器 
  * 8-bit Specification
  * 𝑦[𝑛] = 𝑏0 * 𝑥[𝑛] + 𝑏1 * 𝑥[𝑛−1] + 𝑏2 * 𝑥[𝑛−2] + ...
  * 其中
  * 1.y[n]为第n的输出信号，x[n]为输入信号
  * 2.𝑏𝑖为滤波器的系数（coefficients）或脉冲响应（impulse response）
  * 3.n - 1, n - 2,...表示第n次延迟了1，2，3...个周期
  *    
  */

class My4ElementFir(b0: Int, b1: Int, b2: Int, b3: Int) extends Module {
    val io = IO(new Bundle {
        val in = Input(UInt(8.W))
        val out = Output(UInt(8.W))
    })

    // 这个想法从一开始就是错误的，因为MyShiftRegister移动的对象是就不是一个数组
    // 
    // val x = Module(new MyShiftRegister(init = 0, n = 8))
    // x.io.en := true.B
    // x.io.in := io.in
    // io.out := x.io.out(0) * b0.U(8.W) + 
    //           x.io.out(1) * b1.U(8.W) + 
    //           x.io.out(2) * b2.U(8.W) + 
    //           x.io.out(3) * b3.U(8.W) 

    /**
      * 方法1：使用RegNext，这个方法太笨了
      */
    // val x_n1 = RegNext(io.in, 0.U)
    // val x_n2 = RegNext(x_n1, 0.U)
    // val x_n3 = RegNext(x_n2, 0.U)

    // io.out := io.in * b0.U(8.W) + x_n1 * b1.U(8.W) + 
    //           x_n2 * b2.U(8.W) + x_n3 * b3.U(8.W)

    /**
      * 方法2：使用ShiftRegister，可以指定延迟周期n，也不太好
      */
    val x_n1 = ShiftRegister(in = io.in, n = 1, resetData = 0.U, en = true.B)
    val x_n2 = ShiftRegister(in = x_n1, n = 1, resetData = 0.U, en = true.B)
    val x_n3 = ShiftRegister(in = x_n2, n = 1, resetData = 0.U, en = true.B)
    io.out := io.in * b0.U(8.W) + x_n1 * b1.U(8.W) + 
              x_n2 * b2.U(8.W) + x_n3 * b3.U(8.W)

    // TODO: 如果这里不是4个系数，而是n个，首先定义一个数组的移位寄存器，然后调用。
    // @see MyManyDynamicElementVecFir



}