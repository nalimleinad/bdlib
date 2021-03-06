/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.multiblock.tile

import net.bdew.lib.Misc
import net.bdew.lib.block.BlockFace
import net.bdew.lib.multiblock.data.OutputConfig
import net.bdew.lib.multiblock.interact.{CIOutputFaces, MIOutput}
import net.minecraftforge.common.util.ForgeDirection

abstract class TileOutput[T <: OutputConfig] extends TileModule with MIOutput[T] {
  override def getCore = getCoreAs[CIOutputFaces]

  def makeCfgObject(face: ForgeDirection): T

  var rescanFaces = false

  def getCfg(dir: ForgeDirection): Option[T] =
    for {
      core <- getCore
      onum <- core.outputFaces.get(BlockFace(mypos, dir))
      cfggen <- core.outputConfig.get(onum)
      cfg <- Misc.asInstanceOpt(cfggen, outputConfigType)
    } yield cfg

  serverTick.listen(() => {
    if (rescanFaces) {
      rescanFaces = false
      doRescanFaces()
    }
  })

  override def tryConnect() {
    super.tryConnect()
    if (connected.isDefined) rescanFaces = true
  }

  def canConnectoToFace(d: ForgeDirection): Boolean

  def onConnectionsChanged(added: Set[ForgeDirection], removed: Set[ForgeDirection]) {}

  def doRescanFaces() {
    getCore map { core =>
      val connections = (
        ForgeDirection.VALID_DIRECTIONS
          filterNot { dir => core.modules.contains(mypos.neighbour(dir)) }
          filter canConnectoToFace
        ).toSet
      val known = core.outputFaces.filter(_._1.origin == mypos).map(_._1.face).toSet
      val toAdd = connections -- known
      val toRemove = known -- connections
      toRemove.foreach(x => core.removeOutput(mypos, x))
      toAdd.foreach(x => core.newOutput(mypos, x, makeCfgObject(x)))
      if (toAdd.size > 0 || toRemove.size > 0) {
        onConnectionsChanged(toAdd, toRemove)
        getWorldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
      }
    }
  }
}
