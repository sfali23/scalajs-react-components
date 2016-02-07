package chandu0101.scalajs.react.components
package materialui

import chandu0101.macros.tojs.JSMacro
import japgolly.scalajs.react._
import scala.scalajs.js
import scala.scalajs.js.`|`

/**
 * This file is generated - submit issues instead of PR against it
 */
    
case class MuiCardText(
  key:           js.UndefOr[String]        = js.undefined,
  ref:           js.UndefOr[String]        = js.undefined,
  /* Whether a click on this card component expands the card. Can be set on any child of the Card component.*/
  actAsExpander: js.UndefOr[Boolean]       = js.undefined,
  color:         js.UndefOr[MuiColor]      = js.undefined,
  /* Whether this card component is expandable. Can be set on any child of the Card component.*/
  expandable:    js.UndefOr[Boolean]       = js.undefined,
  /* Override the inline-styles of the root element.*/
  style:         js.UndefOr[CssProperties] = js.undefined){

  def apply(children: ReactNode*) = {
    val props = JSMacro[MuiCardText](this)
    val f = React.asInstanceOf[js.Dynamic].createFactory(Mui.CardText)
    if (children.isEmpty)
      f(props).asInstanceOf[ReactComponentU_]
    else if (children.size == 1)
      f(props, children.head).asInstanceOf[ReactComponentU_]
    else
      f(props, children.toJsArray).asInstanceOf[ReactComponentU_]
  }
}
