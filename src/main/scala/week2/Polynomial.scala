package week2

import chisel3._

class Polynomial extends Module {
    val io = IO(new Bundle {
        val select = Input(UInt(2.W))
        val x      = Input(SInt(32.W))
        val fOfX   = Output(SInt(32.W))
    })

    val result = Wire(SInt(32.W))
    val square = Wire(SInt(32.W))

    square := io.x * io.x

    when(io.select === 0.U) {
        result := square - 2.S * io.x + 1.S
    } .elsewhen(io.select === 1.U) {
        result := 2.S * square + 6.S * io.x + 3.S
    } .otherwise {
        result := 4.S * square - 10.S * io.x - 5.S
    }

    io.fOfX := result
}