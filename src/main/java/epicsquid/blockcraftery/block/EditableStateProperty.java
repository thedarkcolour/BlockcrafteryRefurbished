package epicsquid.blockcraftery.block;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nonnull;

public enum EditableStateProperty implements IUnlistedProperty<IBlockState> {
	INSTANCE;

	@Override
	public String getName() {
		return "stateprop";
	}

	@Override
	public boolean isValid(@Nonnull IBlockState value) {
		return true;
	}

	@Override
	public Class<IBlockState> getType() {
		return IBlockState.class;
	}

	@Override
	public String valueToString(@Nonnull IBlockState value) {
		return value.toString();
	}
}
