package epicsquid.blockcraftery.model;

import epicsquid.mysticallib.model.CustomModelBase;
import epicsquid.mysticallib.model.DefaultTransformations;
import epicsquid.mysticallib.model.ModelUtil;
import epicsquid.mysticallib.model.parts.Cube;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class BakedModelEditableTrapDoor extends BakedModelEditable {

	private final Cube cube_down;

	public BakedModelEditableTrapDoor(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter,
									  CustomModelBase model) {
		super(state, format, bakedTextureGetter, model);
		TextureAtlasSprite[] texes = new TextureAtlasSprite[]{this.texwest, this.texeast, this.texdown, this.texup, this.texnorth, this.texsouth};
		this.cube_down = ModelUtil.makeCube(format, 0, 0, 0, 1, 0.1875, 1, null, texes, 0).setNoCull(EnumFacing.DOWN);
	}

	@Override
	public void addGeometry(List<BakedQuad> quads, EnumFacing side, IBlockState state, TextureAtlasSprite[] texes, int tintIndex) {
		Cube cube_down, cube_up, cube_east, cube_west, cube_south, cube_north;
		cube_down = ModelUtil.makeCube(this.format, 0, 0, 0, 1, 0.1875, 1, null, texes, 0).setNoCull(EnumFacing.DOWN);
		cube_up = ModelUtil.makeCube(this.format, 0, 0.8125, 0, 1, 0.1875, 1, null, texes, 0).setNoCull(EnumFacing.UP);
		cube_west = ModelUtil.makeCube(this.format, 0.8125, 0, 0, 0.1875, 1, 1, null, texes, 0).setNoCull(EnumFacing.WEST);
		cube_east = ModelUtil.makeCube(this.format, 0, 0, 0, 0.1875, 1, 1, null, texes, 0).setNoCull(EnumFacing.EAST);
		cube_north = ModelUtil.makeCube(this.format, 0, 0, 0.8125, 1, 1, 0.1875, null, texes, 0).setNoCull(EnumFacing.NORTH);
		cube_south = ModelUtil.makeCube(this.format, 0, 0, 0, 1, 1, 0.1875, null, texes, 0).setNoCull(EnumFacing.SOUTH);
		BlockTrapDoor.DoorHalf half = state.getValue(BlockTrapDoor.HALF);
		boolean open = state.getValue(BlockTrapDoor.OPEN);
		if (!open) {
			if (half == BlockTrapDoor.DoorHalf.BOTTOM) {
				cube_down.addToList(quads, side);
			} else {
				cube_up.addToList(quads, side);
			}
		} else {
			switch (state.getValue(BlockTrapDoor.FACING)) {
				case EAST:
					cube_east.addToList(quads, side);
					break;
				case SOUTH:
					cube_south.addToList(quads, side);
					break;
				case WEST:
					cube_west.addToList(quads, side);
					break;
				case NORTH:
					cube_north.addToList(quads, side);
					break;
			}
		}
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return this.particle;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return new ItemOverrideList(List.of());
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
		Matrix4f matrix = null;
		if (DefaultTransformations.blockTransforms.containsKey(type)) {
			matrix = DefaultTransformations.blockTransforms.get(type).getMatrix();
			return Pair.of(this, matrix);
		}
		return net.minecraftforge.client.ForgeHooksClient.handlePerspective(this, type);
	}

	@Override
	public void addItemModel(List<BakedQuad> quads, EnumFacing side) {
		this.cube_down.addToList(quads, side);
	}

}
