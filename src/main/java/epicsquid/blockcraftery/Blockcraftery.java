package epicsquid.blockcraftery;

import epicsquid.blockcraftery.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class Blockcraftery {
    @SidedProxy(clientSide = "epicsquid.blockcraftery.proxy.ClientProxy", serverSide = "epicsquid.blockcraftery.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static Blockcraftery INSTANCE;
    public static ModContainer CONTAINER;

    public static CreativeTabs tab = new CreativeTabs("blockcraftery") {
        @Override
        public String getTabLabel() {
            return Reference.MOD_ID;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(RegistryManager.editable_block, 1);
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CONTAINER = Loader.instance().activeModContainer();
        MinecraftForge.EVENT_BUS.register(new RegistryManager());
        MinecraftForge.EVENT_BUS.register(new ConfigManager());
        ConfigManager.init(event.getSuggestedConfigurationFile());
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
