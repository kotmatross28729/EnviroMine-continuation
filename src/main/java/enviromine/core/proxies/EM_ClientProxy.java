package enviromine.core.proxies;

import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enviromine.EntityPhysicsBlock;
import enviromine.blocks.tiles.TileEntityDavyLamp;
import enviromine.blocks.tiles.TileEntityElevator;
import enviromine.blocks.tiles.TileEntityEsky;
import enviromine.blocks.tiles.TileEntityFreezer;
import enviromine.client.gui.Gui_EventManager;
import enviromine.client.gui.hud.HUDRegistry;
import enviromine.client.gui.hud.items.HudItemAirQuality;
import enviromine.client.gui.hud.items.HudItemHydration;
import enviromine.client.gui.hud.items.HudItemSanity;
import enviromine.client.gui.hud.items.HudItemTemperature;
import enviromine.client.gui.menu.EM_Gui_Menu;
import enviromine.client.renderer.tileentity.RenderGasHandler;
import enviromine.client.renderer.tileentity.RenderSpecialHandler;
import enviromine.client.renderer.tileentity.TileEntityDavyLampRenderer;
import enviromine.client.renderer.tileentity.TileEntityElevatorRenderer;
import enviromine.client.renderer.tileentity.TileEntityEskyRenderer;
import enviromine.client.renderer.tileentity.TileEntityFreezerRenderer;
import enviromine.core.EM_Settings;
import enviromine.handlers.ObjectHandler;
import enviromine.handlers.keybinds.EnviroKeybinds;

public class EM_ClientProxy extends EM_CommonProxy {

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public boolean isOpenToLAN() {
        if (Minecraft.getMinecraft()
            .isIntegratedServerRunning()) {
            return Minecraft.getMinecraft()
                .getIntegratedServer()
                .getPublic();
        } else {
            return false;
        }
    }

    @Override
    public void registerTickHandlers() {
        super.registerTickHandlers();
    }

    @Override
    public void registerEventHandlers() {
        super.registerEventHandlers();
        // MinecraftForge.EVENT_BUS.register(new EM_GuiEnviroMeters(Minecraft.getMinecraft(),
        // Minecraft.getMinecraft().getResourceManager()));
        MinecraftForge.EVENT_BUS.register(new ObjectHandler());
        MinecraftForge.EVENT_BUS.register(new Gui_EventManager());
        FMLCommonHandler.instance()
            .bus()
            .register(new EnviroKeybinds());
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        EnviroKeybinds.Init();

        initRenderers();
        registerHudItems();

    }

    @SideOnly(Side.CLIENT)
    public static void initRenderers() {
        ObjectHandler.renderGasID = RenderingRegistry.getNextAvailableRenderId();
        ObjectHandler.renderSpecialID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(ObjectHandler.renderGasID, new RenderGasHandler());
        RenderingRegistry.registerBlockHandler(ObjectHandler.renderSpecialID, new RenderSpecialHandler());

        RenderingRegistry.registerEntityRenderingHandler(EntityPhysicsBlock.class, new RenderFallingBlock());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityElevator.class, new TileEntityElevatorRenderer());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDavyLamp.class, new TileEntityDavyLampRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEsky.class, new TileEntityEskyRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFreezer.class, new TileEntityFreezerRenderer());
    }

    @SideOnly(Side.CLIENT)
    public static void registerHudItems() {
        HUDRegistry.registerHudItem(new HudItemTemperature());
        HUDRegistry.registerHudItem(new HudItemHydration());
        HUDRegistry.registerHudItem(new HudItemSanity());
        HUDRegistry.registerHudItem(new HudItemAirQuality());
        HUDRegistry.setInitialLoadComplete(true);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        VoxelMenu();
    }

    public void VoxelMenu() {
        try {

            Class<? extends GuiMainMenu> ingameGuiClass = (Class<? extends GuiMainMenu>) Class
                .forName("com.thevoxelbox.voxelmenu.ingame.GuiIngameMenu");
            Method mRegisterCustomScreen = ingameGuiClass
                .getDeclaredMethod("registerCustomScreen", String.class, Class.class, String.class);

            mRegisterCustomScreen
                .invoke(null, "", EM_Gui_Menu.class, StatCollector.translateToLocal("options.enviromine.menu.title"));

            EM_Settings.voxelMenuExists = true;
        } catch (ClassNotFoundException ex) { // This means VoxelMenu does not
            // exist
            EM_Settings.voxelMenuExists = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
