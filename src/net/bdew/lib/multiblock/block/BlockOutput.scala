/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.multiblock.block

import net.bdew.lib.block.{BlockFace, BlockRef}
import net.bdew.lib.multiblock.tile.TileOutput
import net.bdew.lib.render.connected.{BlockAdditionalRender, ConnectedHelper, FaceOverlay}
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.util.ForgeDirection

trait BlockOutput[T <: TileOutput[_]] extends BlockModule[T] with BlockAdditionalRender {
  def getAdjancedFaces(face: ForgeDirection, bp: BlockRef) = {
    val sides = ConnectedHelper.faceAdjanced(face)
    Seq(
      (BlockFace(bp, sides.top), resources.arrowTop),
      (BlockFace(bp, sides.right), resources.arrowRight),
      (BlockFace(bp, sides.bottom), resources.arrowBottom),
      (BlockFace(bp, sides.left), resources.arrowLeft)
    )
  }

  def getFaceOverlays(world: IBlockAccess, x: Int, y: Int, z: Int, face: ForgeDirection): List[FaceOverlay] = {
    var result = List.empty[FaceOverlay]
    Option(getTE(world, x, y, z)) flatMap (_.getCore) map { core =>
      val bf = BlockFace(x, y, z, face)
      if (core.outputFaces.contains(bf))
        result :+= FaceOverlay(resources.output, resources.outputColors(core.outputFaces(bf)))
      for ((bf, icon) <- getAdjancedFaces(face, bf.origin)) {
        if (core.outputFaces.contains(bf))
          result :+= FaceOverlay(icon, resources.outputColors(core.outputFaces(bf)))
      }
    }
    return result
  }

}
