/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.data.base

/**
 * Base class for data slots that hold numeric values
 * adds convinience operators to modify the value
 */

abstract class DataSlotNumeric[T](default: T)(implicit n: Numeric[T]) extends DataSlotVal[T] {
  var cval = default
  def +=(that: T) = update(n.plus(cval, that))
  def -=(that: T) = update(n.minus(cval, that))
  def *=(that: T) = update(n.times(cval, that))
}
