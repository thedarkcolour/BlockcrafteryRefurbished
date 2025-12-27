package epicsquid.blockcraftery.model;

import epicsquid.blockcraftery.block.EditableStateProperty;
import epicsquid.blockcraftery.block.IEditableBlock;
import epicsquid.mysticallib.model.CustomModelBase;
import epicsquid.mysticallib.model.DefaultTransformations;
import epicsquid.mysticallib.model.ModelUtil;
import epicsquid.mysticallib.model.block.BakedModelBlock;
import epicsquid.mysticallib.model.parts.Cube;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public abstract class BakedModelEditable extends BakedModelBlock {
	public Map<String, List<BakedQuad>> data = new ConcurrentHashMap<>();
	Cube cube;

	public BakedModelEditable(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, CustomModelBase model) {
		super(state, format, bakedTextureGetter, model);
		this.cube = ModelUtil
			.makeCube(format, 0, 0, 0, 1, 1, 1, ModelUtil.FULL_FACES, new TextureAtlasSprite[]{this.texwest, this.texeast, this.texdown, this.texup, this.texnorth, this.texsouth}, 0);
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		if (state == null) {
			List<BakedQuad> quads = new ArrayList<>();
			addItemModel(quads, side);
			return quads;
		}

		if (!(state instanceof IExtendedBlockState extended) || !(state.getBlock() instanceof IEditableBlock)) {
			return List.of();
		}
		IBlockState texState = extended.getValue(EditableStateProperty.INSTANCE);
		String dataId = texState + "_" + state + "_" + (side == null ? "null" : side.toString()) + MinecraftForgeClient.getRenderLayer();
		return this.data.computeIfAbsent(dataId, (key) -> cacheQuads(state, texState, side, rand));
	}

	private List<BakedQuad> cacheQuads(IBlockState state, IBlockState texState, EnumFacing side, long seed) {
		List<BakedQuad> quads = new ArrayList<>();

		TextureAtlasSprite[] sprites = new TextureAtlasSprite[]{getParticleTexture()};
		int[] tintIndices = new int[]{0};
		boolean usingDefault = true;
		if (texState != null && texState.getBlock() != Blocks.AIR) {
			IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(texState);
			sprites[0] = model.getParticleTexture();
			List<BakedQuad> texQuads = model.getQuads(texState, side, seed);
			if (!texQuads.isEmpty()) {
				sprites = new TextureAtlasSprite[texQuads.size()];
				tintIndices = new int[texQuads.size()];
				for (int i = 0; i < texQuads.size(); i++) {
					if (texQuads.get(i).hasTintIndex()) {
						tintIndices[i] = texQuads.get(i).getTintIndex();
					} else {
						tintIndices[i] = -1;
					}
					sprites[i] = texQuads.get(i).getSprite();
				}
			}
			usingDefault = false;
		}
		if (usingDefault && MinecraftForgeClient.getRenderLayer() == BlockRenderLayer.CUTOUT_MIPPED
			|| !usingDefault && MinecraftForgeClient.getRenderLayer() == texState.getBlock().getRenderLayer()) {
			for (int i = 0; i < sprites.length; i++) {
				addGeometry(quads, side, state, new TextureAtlasSprite[]{sprites[i], sprites[i], sprites[i], sprites[i], sprites[i], sprites[i]}, tintIndices[i]);
			}
		}

		return quads;
	}

	public abstract void addItemModel(List<BakedQuad> quads, EnumFacing side);

	public abstract void addGeometry(List<BakedQuad> quads, EnumFacing side, IBlockState state, TextureAtlasSprite[] texes, int tintIndex);

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

}
