package week2

import chisel3._

class Arbiter extends Module {
  val io = IO(new Bundle {
    // FIFO
    val fifo_valid = Input(Bool())
    val fifo_ready = Output(Bool())
    val fifo_data  = Input(UInt(16.W))

    // PE0
    val pe0_valid  = Output(Bool())
    val pe0_ready  = Input(Bool())
    val pe0_data   = Output(UInt(16.W))

    // PE1
    val pe1_valid  = Output(Bool())
    val pe1_ready  = Input(Bool())
    val pe1_data   = Output(UInt(16.W))
  })

  /**
   * 
   *
   */
  io.fifo_ready := io.pe0_ready || io.pe1_ready
  io.pe0_valid  := io.fifo_valid && io.pe0_ready
  io.pe1_valid  := io.fifo_valid && io.pe1_ready && !io.pe0_ready



  io.pe0_data   := io.fifo_data
  io.pe1_data   := io.fifo_data
}
