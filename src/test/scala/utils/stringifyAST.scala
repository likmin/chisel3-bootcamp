package utils

import firrtl._

object stringifyAST {
    def apply(firrtlAST: firrtl.ir.Circuit): String = {
        var ntabs = 0
        val buf = new StringBuilder
        val string = firrtlAST.toString
        string.zipWithIndex.foreach { case (c, idx) =>
            c match {
            case ' ' =>
            case '(' =>
                ntabs += 1
                buf ++= "(\n" + "| " * ntabs
            case ')' =>
                ntabs -= 1
                buf ++= "\n" + "| " * ntabs + ")"
            case ','=> buf ++= ",\n" + "| " * ntabs
            case  c if idx > 0 && string(idx-1)==')' =>
                buf ++= "\n" + "| " * ntabs + c
            case c => buf += c
            }
        }
        buf.toString
    }
}