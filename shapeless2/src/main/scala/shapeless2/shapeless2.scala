package shapeless2

import scala.annotation.tailrec

sealed trait HList extends Product with Serializable

final case class ::[+H, +T <: HList](head: H, tail: T) extends HList {
  override def toString: String = head match {
    case _: ::[_, _] => s"($head) :: $tail"
    case _           => s"$head :: $tail"
  }
}

sealed trait HNil extends HList {
  def ::[H](h: H): H :: HNil = new ::(h, this)

  override def toString = "HNil"
}

case object HNil extends HNil

object HList extends HListScalaCompat {
  def apply(): HNil.type = HNil

  def apply[T](t: T): T :: HNil = t :: HNil

  def apply[P <: Product, L <: HList](p: P)(implicit gen: Generic.Aux[P, L]): L = gen.to(p)
}

trait Generic[T] extends Serializable {
  type Repr

  def to(t: T): Repr

  def from(r: Repr): T
}

object Generic extends GenericScalaCompat {

  type Aux[T, Repr0] = Generic[T] { type Repr = Repr0 }

  def apply[T](implicit gen: Generic[T]): Aux[T, gen.Repr] = gen

  def instance[T, R](f: T => R, g: R => T): Aux[T, R] = new Generic[T] {
    type Repr = R

    def to(t: T): R = f(t)

    def from(r: R): T = g(r)
  }
}

trait LabelledGeneric[T] extends Serializable {
  type Repr

  def to(t: T): Repr

  def from(r: Repr): T
}

object LabelledGeneric extends LabelledGenericScalaCompat {

  type Aux[T, Repr0] = LabelledGeneric[T] { type Repr = Repr0 }

  def apply[T](implicit lgen: LabelledGeneric[T]): Aux[T, lgen.Repr] = lgen

  def unsafeInstance[T, R](gen: Generic[T]): Aux[T, R] = new LabelledGeneric[T] {
    type Repr = R

    def to(t: T): Repr = gen.to(t).asInstanceOf[R]

    def from(r: Repr): T = gen.from(r.asInstanceOf[gen.Repr])
  }
}

trait HListScalaCompat {

  type TupleToHList[T <: scala.Tuple] <: HList = T match {
    case EmptyTuple => HNil
    case h *: t     => h :: TupleToHList[t]
  }

  // TODO: tailrec
  def tupleToHList[T <: scala.Tuple](tuple: T): TupleToHList[T] = tuple match {
    case _: EmptyTuple  => HNil
    case cons: *:[h, t] => ::(cons.head, tupleToHList(cons.tail))
  }

  type HListToTuple[L <: HList] <: scala.Tuple = L match {
    case HNil   => EmptyTuple
    case h :: t => h *: HListToTuple[t]
  }

  // TODO: tailrec
  def hListToTuple[L <: HList](hlist: L): HListToTuple[L] = hlist match {
    case _: HNil        => EmptyTuple
    case cons: ::[h, t] => cons.head *: hListToTuple(cons.tail)
  }
}

trait GenericScalaCompat extends GenericScalaCompatLowPriority {

  given Generic.Aux[Unit, HNil] = new Generic[Unit] {
    override type Repr = HNil

    override def to(t: Unit): Repr = HNil

    override def from(r: Repr): Unit = ()
  }

  given [A <: AnyRef & Singleton](using v: ValueOf[A]): Generic.Aux[A, HNil] = new Generic[A] {
    override type Repr = HNil

    override def to(t: A): Repr = HNil

    override def from(r: Repr): A = v.value
  }
}

trait GenericScalaCompatLowPriority {

  transparent inline given materializeProduct[T <: Product](
      using m: scala.deriving.Mirror.ProductOf[T]
  ): Generic.Aux[T, HList.TupleToHList[m.MirroredElemTypes]] =
    new Generic[T] {
      override type Repr = HList.TupleToHList[m.MirroredElemTypes]

      override def to(t: T): Repr = HList.tupleToHList(scala.Tuple.fromProductTyped(t))

      override def from(r: Repr): T = m.fromProduct(HList.hListToTuple(r))
    }

  transparent inline given materializeSum[T](
      using m: scala.deriving.Mirror.SumOf[T],
      ev: scala.Tuple.Union[Coproduct.CoproductToTuple[Coproduct.TupleToCoproduct[m.MirroredElemTypes]]] <:< T
  ): Generic.Aux[T, Coproduct.TupleToCoproduct[m.MirroredElemTypes]] =
    new Generic[T] {
      override type Repr = Coproduct.TupleToCoproduct[m.MirroredElemTypes]

      override def to(t: T): Repr =
        Coproduct.coproductFromOrdinal(t.asInstanceOf[scala.Tuple.Union[m.MirroredElemTypes]], m.ordinal(t))

      override def from(r: Repr): T = ev(Coproduct.extractCoproduct(r))
    }
}

trait LabelledGenericScalaCompat {

  type MakeFieldsProduct[Types <: scala.Tuple, Labels <: scala.Tuple] <: HList = (Types, Labels) match {
    case (EmptyTuple, EmptyTuple)        => HNil
    case (tpe *: types, label *: labels) => labelled.FieldType[label, tpe] :: MakeFieldsProduct[types, labels]
  }

  type MakeFieldsCoproduct[Types <: scala.Tuple, Labels <: scala.Tuple] <: Coproduct = (Types, Labels) match {
    case (EmptyTuple, EmptyTuple)        => CNil
    case (tpe *: types, label *: labels) => labelled.FieldType[label, tpe] :+: MakeFieldsCoproduct[types, labels]
  }

  given LabelledGeneric.Aux[Unit, HNil] = new LabelledGeneric[Unit] {
    override type Repr = HNil

    override def to(t: Unit): Repr = HNil

    override def from(r: Repr): Unit = ()
  }

  transparent inline given materializeProduct[T <: Product](
      using m: scala.deriving.Mirror.ProductOf[T]
  ): LabelledGeneric.Aux[T, MakeFieldsProduct[m.MirroredElemTypes, m.MirroredElemLabels]] =
    LabelledGeneric.unsafeInstance(Generic.materializeProduct)

  transparent inline given materializeSum[T](
      using m: scala.deriving.Mirror.SumOf[T],
      ev: scala.Tuple.Union[Coproduct.CoproductToTuple[Coproduct.TupleToCoproduct[m.MirroredElemTypes]]] <:< T
  ): LabelledGeneric.Aux[T, MakeFieldsCoproduct[m.MirroredElemTypes, m.MirroredElemLabels]] =
    LabelledGeneric.unsafeInstance(Generic.materializeSum)
}

sealed trait Coproduct extends Product with Serializable

sealed trait :+:[+H, +T <: Coproduct] extends Coproduct {
  def eliminate[A](l: H => A, r: T => A): A
}

final case class Inl[+H, +T <: Coproduct](head: H) extends :+:[H, T] {
  override def eliminate[A](l: H => A, r: T => A) = l(head)
}

final case class Inr[+H, +T <: Coproduct](tail: T) extends :+:[H, T] {
  override def eliminate[A](l: H => A, r: T => A) = r(tail)
}

sealed trait CNil extends Coproduct {
  def impossible: Nothing
}

object Coproduct extends CoproductScalaCompat {
  def unsafeMkCoproduct(length: Int, value: Any) =
    (0 until length).foldLeft[Coproduct](Inl(value))((accum, _) => Inr(accum))

  @tailrec
  def unsafeGet(c: Coproduct): Any = (c: @unchecked) match {
    case Inl(h) => h
    case Inr(c) => unsafeGet(c)
  }
}

trait CoproductScalaCompat {

  type CoproductToTuple[C <: Coproduct] <: scala.Tuple = C match {
    case CNil    => EmptyTuple
    case h :+: t => h *: CoproductToTuple[t]
  }
  type TupleToCoproduct[T <: scala.Tuple] <: Coproduct = T match {
    case EmptyTuple => CNil
    case h *: t     => h :+: TupleToCoproduct[t]
  }

  type HListToCoproduct[L <: HList] <: Coproduct = L match {
    case HNil   => CNil
    case h :: t => h :+: t
  }

  def extractCoproduct[C <: Coproduct](coproduct: C): scala.Tuple.Union[CoproductToTuple[C]] = coproduct match {
    case Inl(head) => head.asInstanceOf[scala.Tuple.Union[CoproductToTuple[C]]]
    case Inr(tail) => extractCoproduct(tail.asInstanceOf[C])
  }

  def coproductFromOrdinal[T <: scala.Tuple](a: scala.Tuple.Union[T], ordinal: Int): TupleToCoproduct[T] =
    if ordinal == 0 then Inl(a).asInstanceOf[TupleToCoproduct[T]]
    else Inr(coproductFromOrdinal(a, ordinal - 1)).asInstanceOf[TupleToCoproduct[T]]
}

object labelled {

  opaque type FieldType[K, +V] <: V = V

  type ->>[K, +V] = FieldType[K, V]

  def field[K]: FieldBuilder[K] = new FieldBuilder(true)

  class FieldBuilder[K](private val dummy: Boolean) extends AnyVal {
    def apply[V](v: V): FieldType[K, V] = v.asInstanceOf[FieldType[K, V]]
  }
}
