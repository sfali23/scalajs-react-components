package com.dbrsn.generator.requiresjs

import ammonite.ops.Path
import com.dbrsn.generator.{add, Import, VarName}
import jdk.nashorn.internal.ir._

import scala.collection.JavaConverters._
import scala.collection.mutable

final case class VisitorImports(n: FunctionNode, currentPath: Path)
    extends VisitorHelperNameStack[FunctionNode, Seq[Import]](n) {
  private val ret: mutable.Map[VarName, Import] =
    mutable.Map.empty[VarName, Import]

  override def enterCallNode(n: CallNode): Boolean =
    matcher((n.getFunction, n.getArgs.asScala.toList)) {
      case (i: IdentNode, List(o: LiteralNode[_])) if i.getName == "require" =>
        val target =
          if (o.getString.startsWith(".")) {
            Left[Path, String](add(currentPath, o.getString))
          } else {
            Right[Path, String](o.getString)
          }

        val name = nameStack.headOption.getOrElse(VarName(o.getString))
        ret(name) = Import(name, target)

      case (i: IdentNode, List(arg: IdentNode)) if i.getName.contains("interopRequireDefault") =>
        ret(VarName(arg.getName)) = ret(VarName(arg.getName)).copy(varName = nameStack.head)
    }

  override protected def fetchValue(): Seq[Import] =
    ret.values.toSeq
}
