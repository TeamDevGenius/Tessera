package com.tessera.gdx;

import com.tessera.content.vanilla.Blocks;
import com.tessera.content.vanilla.Entities;
import com.tessera.content.vanilla.Items;
import com.tessera.content.vanilla.blocks.RenderType;
import com.tessera.content.vanilla.blocks.type.*;
import com.tessera.content.vanilla.terrain.FlatTerrain;
import com.tessera.content.vanilla.terrain.defaultTerrain.DefaultTerrain;
import com.tessera.engine.client.Client;
import com.tessera.engine.server.Registrys;
import com.tessera.engine.server.block.Block;
import com.tessera.engine.server.entity.EntitySupplier;
import com.tessera.engine.server.item.Item;
import com.tessera.engine.server.loot.AllLootTables;
import com.tessera.engine.server.recipes.AllRecipes;
import com.tessera.engine.server.world.World;
import com.tessera.engine.server.world.data.WorldData;
import com.tessera.engine.utils.progress.ProgressData;
import com.tessera.engine.utils.resource.ResourceLister;
import com.tessera.engine.utils.resource.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Initialises core game content for the libGDX / Android path.
 * <p>
 * Splits work into two phases so the caller can schedule GL-context work on
 * the render thread:
 * <ol>
 *   <li>{@link #initLogic} – background-thread-safe (no GL calls)</li>
 *   <li>{@link #initGLResources} – must be called on the GL/render thread</li>
 * </ol>
 * Nuklear UI, ClientWindow, and LWJGL-only classes are intentionally excluded.
 */
public class GdxGameInitializer {

    // Intermediate results passed from initLogic → initGLResources
    private static List<Block> pendingBlockList;
    private static List<EntitySupplier> pendingEntityList;
    private static List<Item> pendingItemList;
    private static String pendingWorldName;
    private static int pendingSeed;

    /** The world instance used by the GDX/Android render path. */
    public static World gdxWorld;

    /**
     * Phase 1 – runs on a background thread.
     * Initialises resource paths and prepares block / entity / item lists.
     */
    public static void initLogic(String worldName, int seed, String terrainName,
                                  ProgressData progress) throws Exception {

        if (progress != null) progress.setTask("Initializing resources...");
        ResourceUtils.initialize(false, null);
        ResourceLister.init();

        pendingWorldName = (worldName != null && !worldName.isEmpty()) ? worldName : "world";
        pendingSeed = seed;

        if (progress != null) progress.setTask("Registering block types...");
        registerBlockTypes();

        if (progress != null) progress.setTask("Loading blocks...");
        pendingBlockList = Blocks.starup_getBlocks();

        if (progress != null) progress.setTask("Loading entities...");
        // Pass null for ClientWindow – entity suppliers store the reference but only
        // use it at render time, which is handled separately in the GDX render path.
        pendingEntityList = Entities.startup_getEntities(null);

        if (progress != null) progress.setTask("Loading items...");
        pendingItemList = Items.startup_getItems();
    }

    /**
     * Phase 2 – must be called on the GL render thread (e.g. via
     * {@code Gdx.app.postRunnable(...)}).
     * Creates GPU textures and completes registry / loot / recipe setup.
     */
    public static void initGLResources(ProgressData progress) throws Exception {
        if (progress != null) progress.setTask("Uploading textures...");
        // Registrys.initializeGdx creates BlockArrayTexture (GL work) then wires up
        // block/entity/item registries.
        Registrys.initializeGdx(pendingBlockList, pendingEntityList, pendingItemList);

        if (progress != null) progress.setTask("Loading loot tables...");
        AllLootTables.blockLootTables.register("/data/tessera/loot/block");
        AllLootTables.animalFeedLootTables.register("/data/tessera/loot/animalFeed");

        if (progress != null) progress.setTask("Loading recipes...");
        AllRecipes.craftingRecipes.register("/data/tessera/recipes/crafting");
        AllRecipes.smeltingRecipes.register("/data/tessera/recipes/smelting");

        if (progress != null) progress.setTask("Adding terrains...");
        // Terrains are registered on the game object that owns the world.
        // Access them via the static TesseraGame reference if available,
        // or register directly into a local list used by the GDX game loop.
        // For now we populate the standard vanilla terrains here so that a
        // GdxServer/World can resolve terrain by name.
        GdxTerrainRegistry.terrains.add(new DefaultTerrain());
        GdxTerrainRegistry.terrains.add(new FlatTerrain());

        // Create and initialise the GDX world using the existing Client.world instance
        gdxWorld = Client.world;
        gdxWorld.data = new WorldData();
        gdxWorld.terrain = GdxTerrainRegistry.terrains.isEmpty() ? null : GdxTerrainRegistry.terrains.get(0);
        if (gdxWorld.terrain != null) {
            com.tessera.engine.utils.option.OptionsList emptyOptions =
                    new com.tessera.engine.utils.option.OptionsList();
            gdxWorld.terrain.initForWorld(pendingSeed, emptyOptions, 0);
            gdxWorld.data.makeNew(pendingWorldName, 256, gdxWorld.terrain, pendingSeed);
        }
        gdxWorld.initGdx(Registrys.blocks.textures);

        // Clean up intermediate lists to allow GC
        pendingBlockList = null;
        pendingEntityList = null;
        pendingItemList = null;
        pendingWorldName = null;
        pendingSeed = 0;
    }

    // -------------------------------------------------------------------------

    private static void registerBlockTypes() throws Exception {
        Registrys.blocks.addBlockType("sprite",      RenderType.SPRITE,            new SpriteRenderer());
        Registrys.blocks.addBlockType("floor",       RenderType.FLOOR,             new FloorItemRenderer());
        Registrys.blocks.addBlockType("orientable",  RenderType.ORIENTABLE_BLOCK,  new OrientableBlockRenderer());
        Registrys.blocks.addBlockType("slab",        RenderType.SLAB,              new SlabRenderer());
        Registrys.blocks.addBlockType("stairs",      RenderType.STAIRS,            new StairsRenderer());
        Registrys.blocks.addBlockType("fence",       RenderType.FENCE,             new FenceRenderer("/assets/tessera/models/block/fence"));
        Registrys.blocks.addBlockType("wall",        RenderType.WALL_ITEM,         new WallItemRenderer());
        Registrys.blocks.addBlockType("lamp",        RenderType.LAMP,              new LampRenderer());
        Registrys.blocks.addBlockType("pane",        RenderType.PANE,              new PaneRenderer());
        Registrys.blocks.addBlockType("track",       RenderType.RAISED_TRACK,      new RaisedTrackRenderer());
        Registrys.blocks.addBlockType("torch",       RenderType.TORCH,             new TorchRenderer());
        Registrys.blocks.addBlockType("pillar",      RenderType.PILLAR,            new PillarRenderer());
        Registrys.blocks.addBlockType("trapdoor",    RenderType.TRAPDOOR,          new TrapdoorRenderer());
        Registrys.blocks.addBlockType("fence gate",  RenderType.FENCE_GATE,        new FenceGateRenderer());
        Registrys.blocks.addBlockType("door half",   RenderType.DOOR_HALF,         new DoorHalfRenderer());
    }
}
