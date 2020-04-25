package week3

import chisel3._
import week2.ShiftRegister

/**
  * 3.5 object oriented programming 
  * Inheritance with Chisel
  * Module
  * Example: Gray Encoder and Decoder
  * The encode or decode operation choice is hardware programmable
  */

class NoGlitchCounterIO(bitwidth: Int) extends Bundle {
    val en  = Input(Bool())
    val out = Output(UInt(bitwidth.W))
}

abstract class NoGlitchCounter(val maxCount: Int) extends Module {
    val bitwidth: Int
    val io = IO(new NoGlitchCounterIO(bitwidth))
}

abstract class AsyncFIFO(depth: Int) extends Module {
    val io = IO(new Bundle {
        // write inputs
        val write_clock  = Input(Clock())
        val write_enable = Input(Bool())
        val write_data   = Input(UInt(32.W))

        // read inputs/outputs
        val read_clock   = Input(Clock())
        val read_enable  = Input(Bool())
        val read_data    = Output(UInt(32.W))

        // FIFO status
        val full = Output(Bool())
        val empty = Output(Bool())
    })

    def makeCounter(maxCount: Int): NoGlitchCounter

    // add extra bit to counter to check for full//empty status
    assert(isPow2(depth))
    val write_counter = withClock(io.write_clock) {
        val count = makeCounter(depth * 2)
        count.io.en := io.write_enable && !io.full
        count.io.out
    }

    val read_counter = withClock(io.read_clock) {
        val count = makeCounter(depth * 2)
        count.io.en := io.read_enable && !io.empty
        count.io.out
    }

    // synchronize
    val sync = withClock(io.read_clock) {
        ShiftRegister(write_counter, 2)
    }
}