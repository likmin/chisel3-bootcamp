package week2

import chisel3._
import chisel3.util._

/**
 * Copyright (c) 2020, likai. All rights reserved.
 * 
 * @author likai
 * @date 2020-03-30
 * @version 0.0.1
 */
class GradLife extends Module {
    val io = IO(new Bundle{
        val state = Input(UInt(2.W))
        val coffee = Input(Bool())
        val idea = Input(Bool())
        val pressure = Input(Bool())
        val nextState = Output(UInt(2.W))
    })

    val idle :: coding :: writing :: grad :: Nil = Enum(4)

    io.nextState := idle

    /**
     * 要注意顺序，有可能coffee,idea,pressure都有效，但是优先级不同
     */
    when(io.state === idle) {
        when(io.coffee) {io.nextState := coding}
        .elsewhen(io.idea) { io.nextState := idle}
        .elsewhen(io.pressure) {io.nextState := writing}
    } .elsewhen(io.state === coding) {
        when     (io.coffee) {io.nextState := coding}
        .elsewhen(io.idea || io.pressure) {io.nextState := writing}
    } .elsewhen(io.state === writing) {
        when     (io.coffee || io.idea) {io.nextState := writing}
        .elsewhen(io.pressure) {io.nextState := grad}
    } .elsewhen(io.state === grad) {
        io.nextState := idle
    }
}