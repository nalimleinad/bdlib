/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.data.base

import net.minecraft.nbt.NBTTagCompound

/**
 * Base trait for all data slots
 */
trait DataSlot {
  /**
   * Tile Entity that owns this slot
   * Accessed in constructor, so should be in parameters or a lazy val, otherwise will crash
   */
  val parent: TileDataSlots

  /**
   * Unique name
   * Accessed in constructor, so should be in parameters or a lazy val, otherwise will crash
   */
  val name: String

  parent.dataSlots += (name -> this)

  // Where should it sync
  var updateKind = Set(UpdateKind.GUI)

  def save(t: NBTTagCompound, kind: UpdateKind.Value)
  def load(t: NBTTagCompound, kind: UpdateKind.Value)

  def setUpdate(vals: UpdateKind.Value*): this.type = {
    updateKind = vals.toSet
    return this
  }
}