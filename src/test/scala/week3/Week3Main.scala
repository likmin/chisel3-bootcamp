package week3

import chisel3._
import chisel3.iotesters.{TesterOptionsManager, Driver, PeekPokeTester}
import utils.TutorialRunner

object Week3Main {
    val tests = Map(
        "sort4ascending" -> {
            (manager: TesterOptionsManager) => iotesters.Driver.execute(() => new Sort4(true), manager) {
                (c) => new Sort4AscendingTester(c)
            }
        },
        "sort4descending" -> {
            (manager: TesterOptionsManager) => iotesters.Driver.execute(() => new Sort4(false), manager) {
                (c) => new Sort4DescendingTester(c)
            }
        },
        "halfadder" -> {
            (manager: TesterOptionsManager) => iotesters.Driver.execute(() => new HalfFullAdder(false), manager) {
                (c) => new HalfAdderTester(c)
            }
        },
        "fulladder" -> {
            (manager: TesterOptionsManager) => iotesters.Driver.execute(() => new HalfFullAdder(true), manager) {
                (c) => new FullAdderTester(c)
            }
        }
    )


    def main(args: Array[String]): Unit = {
        // TODO: args中的字母全部变小写 args.foreach{_ => _.toLowerCase}。 为什么不对呢
        TutorialRunner("Week3Main", tests, args)
    }
}