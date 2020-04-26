package week3

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

import scala.math.pow

class GrayCoderTester(c: GrayCoder) extends PeekPokeTester(c) {
    /**
      * String.format类似于C语言中的printf，格式化模式匹配的解释在java.util.Formatter中有描述
      * 
      */
    def toBinary(i: Int, digits: Int = 8) = 
        String.format("%" + digits + "s", i.toBinaryString).replace(' ', '0')

    println("Encoding:")
    for(i <- 0 until pow(2, c.bitwidth).toInt) {
        poke(c.io.in, i)
        poke(c.io.encode, true)
        step(1)
        println(s"In = ${toBinary(i, c.bitwidth)}, Out = ${toBinary(peek(c.io.out).toInt, c.bitwidth)}")
    }

    println("Decoding:")
    for(i <- 0 until pow(2, c.bitwidth).toInt) {
        poke(c.io.in, i)
        poke(c.io.encode, false)
        step(1)
        println(s"In = ${toBinary(i, c.bitwidth)}, Out = ${toBinary(peek(c.io.out).toInt, c.bitwidth)}")
    }

}