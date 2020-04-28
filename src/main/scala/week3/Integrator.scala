package week3

import chisel3._
import chisel3.util._
import chisel3.experimental._
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

class Integrator[T <: Data] (genIn: T, genReg: T) extends Module {
    val io = IO(new Bundle {
        val in = Input(genIn.cloneType)
        val out = Output(genReg.cloneType)
    })

    val reg = RegInit(genReg, Ring[T].zero) // init to zero

    reg := reg + io.in

    io.out := reg
}