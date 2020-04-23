package week3

import chisel3._
import chisel3.util._
import chisel3.experimental.FixedPoint

class Neuron(inputs: Int, act: FixedPoint => FixedPoint) extends Module {
    val io = IO(new Bundle {
        /**
          * FixedPoint representing a fixed point number that has a bit width and a binary point The width and binary point
          * may be inferred
          * @param width        bit width of the fixed point number
          * @param binaryPoint  the position of the binary point with respect to the right most bit of the width currently this
          *                     should be positive but it is hoped to soon support negative points and thus use this field as a
          *                     simple exponent
          */
        val in      = Input(Vec(inputs, FixedPoint(width = 16.W, binaryPoint = 8.BP)))
        val weights = Input(Vec(inputs, FixedPoint(16.W, 8.BP)))
        val out     = Output(FixedPoint(16.W, 8.BP))
    })

    val net_input = (io.in zip io.weights).
                    map{case (a: FixedPoint, b: FixedPoint) => a * b}.
                    reduce(_ + _)

    io.out := act(net_input)

}