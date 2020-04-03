package week2

import chisel3.iotesters.PeekPokeTester

class GradLifeTester(c: GradLife) extends PeekPokeTester(c) {

    // state map
    def states = Map("idle" -> 0, "coding" -> 1, "writing" -> 2, "grad" -> 3)

    // life is full of question marks
    def gradLife (state: Int, coffee: Boolean, idea: Boolean, pressure: Boolean): Int = {
        var nextState = states("idle")
        if (state == states("idle")) {
            if      (coffee) { nextState = states("coding") }
            else if (idea) { nextState = states("idle") }
            else if (pressure) { nextState = states("writing") }
        } else if (state == states("coding")) {
            if      (coffee) { nextState = states("coding") } 
            else if (idea || pressure) { nextState = states("writing") }
        } else if (state == states("writing")) {
            if      (coffee || idea) { nextState = states("writing") }
            else if (pressure) { nextState = states("grad") }
        }
        
        nextState
    }

    for(state <- 0 to 3) {
        for(coffee <- List(true, false)) {
            for(idea <- List(true, false)) {
                for(pressure <- List(true, false)) {
                    poke(c.io.state, state)
                    poke(c.io.coffee, coffee)
                    poke(c.io.idea, idea)
                    poke(c.io.pressure, pressure)
                    expect(c.io.nextState, gradLife(state, coffee, idea, pressure))
                }
            }
        }
    }

}