package week3

import chisel3._
import chisel3.util._
import scala.collection._

/**
  * 3.2_collections
  * 
  */
class My4ElementFir(b0: Int, b1: Int, b2: Int, b3: Int) extends Module {
    val io = IO(new Bundle {
        val in = Input(UInt(8.W))
        val out = Output(UInt(8.W))
    })

    val x_n1 = RegNext(io.in, 0.U)
    val x_n2 = RegNext(x_n1, 0.U)
    val x_n3 = RegNext(x_n2, 0.U)

    io.out := io.in * b0.U(8.W) + x_n1 * b1.U(8.W) + 
        x_n2 * b2.U(8.W) + x_n3 * b3.U(8.W)

}