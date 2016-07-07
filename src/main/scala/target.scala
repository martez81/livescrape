/**
  *
  */


trait TargetVal
case object RawTarget extends TargetVal
case object StrTarget extends TargetVal
case object IntTarget extends TargetVal
case object DecimalTarget extends TargetVal
case object DateTarget extends TargetVal
case object TimeTarget extends TargetVal

case class Attr(key: String, value: String)
case class Tag(
                      name: String,
                      child: Option[Tag] = None,
                      attr: Option[Attr] = None,
                      value: Option[TargetVal] = None,
                      childIdx: Option[Int] = None
)

trait Rule

case class RuleEQ(targetA: TargetVal, targetB: TargetVal)

case class Target(name: String, tag: Tag, rule: List[Rule])

case class TargetCollection(name: String, targets: Target*)
