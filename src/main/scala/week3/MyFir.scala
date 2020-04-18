package week3

import chisel3._
import chisel3.util._
import chisel3.experimental._
import chisel3.internal.firrtl.KnownBinaryPoint

class MyFir(length: Int, bitwidth: Int, window: (Int, Int) => Seq[Int]) extends Module {
    val io = IO(new Bundle {
        val in = Input(UInt(bitwidth.W))
        val out = Output(UInt((bitwidth*2 + length - 1).W))
    })
    
    val coeffs = window(length, bitwidth).map(_.U) 
    val delays = Seq.fill(length)(Wire(UInt(bitwidth.W))).scan(io.in)((prev: UInt, next: UInt) => {
        next := RegNext(prev)
        next
    })

    val mults = delays.zip(coeffs).map{case(delay: UInt, coeff: UInt) => delay * coeff }
    val result = mults.reduce(_ +& _)

    io.out := result
}