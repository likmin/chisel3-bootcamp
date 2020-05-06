package week2

import chisel3._

/*saturate 饱和*/
class ParameterizedAdder(saturate: Boolean) extends Module {
    val io = IO(new Bundle {
        val in_a = Input(UInt(4.W))
        val in_b = Input(UInt(4.W))
        val out = Output(UInt(4.W))
    })

    // in_a和in_b都是4bit，但是sum为5bit
    val sum = io.in_a +& io.in_b


    if(saturate) {
        io.out := Mux(sum > 15.U, 15.U, sum)
    } else {
        io.out := sum
    }
}