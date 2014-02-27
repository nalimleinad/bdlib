/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib

package object gui {
  type Rect = BaseRect[Float]
  type Point = BasePoint[Float]

  import language.implicitConversions

  implicit def awt2point(p: java.awt.Point) = BasePoint(p.x, p.y)
  implicit def point2awt(p: BasePoint[Int]): java.awt.Point = new java.awt.Point(p.x, p.y)
  implicit def tuple2point[T: Numeric](p: (T, T)) = BasePoint(p._1, p._2)

  // Converts different kinds of Point/Rect as long as there's an implicit for the underlying type
  implicit def pointconvert[T, R](a: BasePoint[R])(implicit f: R => T, n: Numeric[T]) = BasePoint[T](f(a.x), f(a.y))
  implicit def rectconvert[T, R](a: BaseRect[R])(implicit f: R => T, n: Numeric[T]) = BaseRect[T](f(a.x), f(a.y), f(a.w), f(a.h))

  def Rect(x: Float, y: Float, w: Float, h: Float) = BaseRect(x, y, w, h)
  def Point(x: Float, y: Float) = BasePoint(x, y)
}
