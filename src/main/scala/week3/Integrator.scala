package week3

import chisel3._
import dsptools.numbers._

/**
  * 你这样做了以后，泛型就没有意义了
  */
// class Integrator[T <: Data] (n1: Int, n2: Int) extends Module {
//     val io = IO(new Bundle {
//         val in = Input(UInt(n2.W))
//         val out = Output(UInt(n1.W))
//     })

//     val temp = io.out + io.in 

//     io.out := RegNext(temp)
// }
// 问题记录：
class Integrator[T <: Data : Ring] (genIn: T, genReg: T) extends Module {
    val io = IO(new Bundle {
        val in = Input(genIn.cloneType)
        val out = Output(genReg.cloneType)
    })

    /**
      * 当时泛型这里只写了[T <: Data]，而不是[T <: Data : Ring] ，所以出现了错误：
      * could not find implicit value for parameter A ：dsptools.numbers.Ring[T]
      *
      */
    val reg = RegInit(genReg, Ring[T].zero) // init to zero

    reg := reg + io.in

    io.out := reg
}