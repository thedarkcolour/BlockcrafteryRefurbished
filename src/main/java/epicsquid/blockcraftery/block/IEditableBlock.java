package epicsquid.blockcraftery.block;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;

import javax.annotation.Nullable;

public interface IEditableBlock {
	PropertyBool LIGHT = PropertyBool.create("light");

	default IBlockState setEditableProperties(IBlockState state, IBlockState camo) {
		return state;
	}
}
