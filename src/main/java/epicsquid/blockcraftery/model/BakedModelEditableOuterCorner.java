package epicsquid.blockcraftery.model;

import epicsquid.mysticallib.block.BlockCornerBase;
import epicsquid.mysticallib.model.CustomModelBase;
import epicsquid.mysticallib.model.DefaultTransformations;
import epicsquid.mysticallib.model.ModelUtil;
import epicsquid.mysticallib.model.parts.Segment;
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

public class BakedModelEditableOuterCorner extends BakedModelEditable {
	Segment baked_segm_down_pxpz;

	public BakedModelEditableOuterCorner(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter,
										 CustomModelBase model) {
		super(state, format, bakedTextureGetter, model);

		this.baked_segm_down_pxpz = ModelUtil.makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			new TextureAtlasSprite[]{this.texwest, this.texeast, this.texdown, this.texup, this.texnorth, this.texsouth}, -1);
	}

	@Override
	public void addGeometry(List<BakedQuad> quads, EnumFacing side, IBlockState state, TextureAtlasSprite[] texes, int tintIndex) {
		Segment segm_down_nxnz, segm_down_pxnz, segm_down_pxpz, segm_down_nxpz;
		Segment segm_up_nxnz, segm_up_pxnz, segm_up_pxpz, segm_up_nxpz;
		segm_down_nxnz = ModelUtil.makeSegm(this.format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, texes, tintIndex);
		segm_down_pxnz = ModelUtil.makeSegm(this.format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, texes, tintIndex);
		segm_down_pxpz = ModelUtil.makeSegm(this.format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, texes, tintIndex);
		segm_down_nxpz = ModelUtil.makeSegm(this.format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, texes, tintIndex);
		// Use makeSegmUp on these since it is a special version of the makeSegm method where every face except the downward one has its first and second coord
		// switched with its third and fourth.
		// For some reason faces are rendered black on custom baked models if the coords have a "wrong drawing order" on newer Forge versions.
		segm_up_nxnz = ModelUtil.makeSegmUp(this.format, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, texes, tintIndex);
		segm_up_pxnz = ModelUtil.makeSegmUp(this.format, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, texes, tintIndex);
		segm_up_pxpz = ModelUtil.makeSegmUp(this.format, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, texes, tintIndex);
		segm_up_nxpz = ModelUtil.makeSegmUp(this.format, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, texes, tintIndex);
		boolean up = state.getValue(BlockCornerBase.UP);
		int dir = state.getValue(BlockCornerBase.DIR);
		if (!up) {
			switch (dir) {
				case 0:
					segm_down_nxnz.addToList(quads, side);
					break;
				case 1:
					segm_down_pxnz.addToList(quads, side);
					break;
				case 2:
					segm_down_pxpz.addToList(quads, side);
					break;
				case 3:
					segm_down_nxpz.addToList(quads, side);
					break;
			}
		} else {
			switch (dir) {
				case 0:
					segm_up_nxnz.addToList(quads, side);
					break;
				case 1:
					segm_up_pxnz.addToList(quads, side);
					break;
				case 2:
					segm_up_pxpz.addToList(quads, side);
					break;
				case 3:
					segm_up_nxpz.addToList(quads, side);
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
	public Pair<? extends IBakedModel, javax.vecmath.Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
		Matrix4f matrix = null;
		if (DefaultTransformations.blockTransforms.containsKey(type)) {
			matrix = DefaultTransformations.blockTransforms.get(type).getMatrix();
			return Pair.of(this, matrix);
		}
		return net.minecraftforge.client.ForgeHooksClient.handlePerspective(this, type);
	}

	@Override
	public void addItemModel(List<BakedQuad> quads, EnumFacing side) {
		this.baked_segm_down_pxpz.addToList(quads, side);
	}

}
