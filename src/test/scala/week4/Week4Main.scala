package week4

import chisel3._
import chisel3.iotesters.TesterOptionsManager
import utils.TutorialRunner

object Week4Main {
     def main(args: Array[String]): Unit = {
         if(args.length == 1 && args(0).toLowerCase == "firstfirrtlast") {
            FirstFirrtlAST()
        } 
     }
}