package week3

import chisel3._
import chisel3.util._

class MyRoutingArbiter(numChannels: Int) extends Module {
  val io = IO(new Bundle {
    val in = Vec(numChannels, Flipped(Decoupled(UInt(8.W))))
    val out = Decoupled(UInt(8.W))
  } )

  // Your code here
  io.out.valid := io.in.map(_.valid).reduce(_ || _)
  /**
    * io.in.map(_.valid) 将io.in众多信号中提取出valid信号
    */
  val channel = PriorityMux(
    io.in.map(_.valid).zipWithIndex.map{
      case (valid, index) => (valid, index.U)
    }
  )

  io.out.bits := io.in(channel).bits


  io.in.map(_.ready).zipWithIndex.foreach {
    case (ready, index) => ready := io.out.ready && channel === index.U
  }
  
}