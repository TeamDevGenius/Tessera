package com.tessera.gdx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tessera.engine.client.player.raycasting.Ray;
import com.tessera.engine.client.player.raycasting.RayCasting;
import com.tessera.engine.server.GameMode;
import com.tessera.engine.server.Registrys;
import com.tessera.engine.server.block.Block;
import com.tessera.engine.server.item.Item;
import com.tessera.engine.server.item.ItemStack;
import com.tessera.engine.server.item.StorageSpace;
import com.tessera.engine.server.players.Player;
import com.tessera.engine.server.world.World;
import com.tessera.engine.server.world.data.WorldData;
import com.tessera.engine.utils.json.gson.ItemStackTypeAdapter;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Lightweight libGDX / Android player.
 * <p>
 * Extends the shared {@link Player} base without requiring any LWJGL or
 * desktop-specific classes (ClientWindow, Camera, etc.).  The position is
 * synchronised from the libGDX {@link com.badlogic.gdx.graphics.PerspectiveCamera}
 * on every frame via {@link #syncPositionFromCamera}.
 */
public class GdxPlayer extends Player {

    // ─── vital stats ──────────────────────────────────────────────────────────
    public static final float MAX_HEALTH  = 20f;
    public static final float MAX_FOOD    = 20f;
    public static final float MAX_OXYGEN  = 20f;

    private static final float IDLE_FOOD_DEPLETION    = 0.00001f;
    private static final float MOVING_FOOD_DEPLETION  = 0.0003f;
    private static final float HEALTH_REGEN_SPEED     = 0.0006f;

    private float health  = MAX_HEALTH;
    private float food    = MAX_FOOD;
    private float oxygen  = MAX_OXYGEN;

    private final Vector3f spawnPosition = new Vector3f(0, 70, 0);

    // ─── inventory (hotbar = first 9 slots) ───────────────────────────────────
    public static final int INVENTORY_SIZE = 33;
    public static final int HOTBAR_SIZE    =  9;
    public final StorageSpace inventory;
    private int hotbarSlot = 0;

    // ─── raycasting ───────────────────────────────────────────────────────────
    private static final int REACH = 6;
    private final Ray ray = new Ray();

    private static final Gson pdGson = new GsonBuilder()
            .registerTypeAdapter(ItemStack.class, new ItemStackTypeAdapter())
            .create();

    static final String PLAYER_DATA_FILE = "player.json";

    // ─── constructor ──────────────────────────────────────────────────────────

    public GdxPlayer() {
        super();
        inventory = new StorageSpace(INVENTORY_SIZE);
    }

    // ─── vital-stat accessors ─────────────────────────────────────────────────

    public float getHealth()  { return health; }
    public float getFood()    { return food;   }
    public float getOxygen()  { return oxygen; }

    public void setHealth(float v) { health = Math.max(0, Math.min(MAX_HEALTH, v)); }
    public void setFood(float v)   { food   = Math.max(0, Math.min(MAX_FOOD,   v)); }
    public void setOxygen(float v) { oxygen = Math.max(0, Math.min(MAX_OXYGEN, v)); }

    // ─── hotbar ───────────────────────────────────────────────────────────────

    public int getHotbarSlot()              { return hotbarSlot; }
    public void setHotbarSlot(int slot)     { hotbarSlot = ((slot % HOTBAR_SIZE) + HOTBAR_SIZE) % HOTBAR_SIZE; }
    public ItemStack getHotbarItem()        { return inventory.get(hotbarSlot); }

    /** Scroll through hotbar slots (+1 or -1). */
    public void scrollHotbar(int delta)     { setHotbarSlot(hotbarSlot + delta); }

    // ─── per-frame tick ───────────────────────────────────────────────────────

    /**
     * Update vital stats once per render frame.
     * @param delta frame-time in seconds
     * @param isMoving whether the player moved this frame
     * @param isUnderwater whether the camera is inside a liquid block
     */
    public void tickStats(float delta, boolean isMoving, boolean isUnderwater) {
        float multiplier = delta * 60f; // normalise to 60 fps

        if (isUnderwater) {
            oxygen = Math.max(0, oxygen - 0.05f * multiplier);
        } else {
            oxygen = Math.min(MAX_OXYGEN, oxygen + 0.1f * multiplier);
        }

        if (food > 0) {
            food -= (isMoving ? MOVING_FOOD_DEPLETION : IDLE_FOOD_DEPLETION) * multiplier;
        }

        if (food > 3 && health < MAX_HEALTH) {
            health = Math.min(MAX_HEALTH, health + HEALTH_REGEN_SPEED * multiplier);
        } else if (oxygen <= 0 || food <= 0) {
            health = Math.max(0, health - 0.2f * multiplier * 0.016f);
        }
    }

    // ─── position sync ────────────────────────────────────────────────────────

    /**
     * Copies the libGDX camera position into this player's world-position so
     * that server-side range checks and world-save work correctly.
     */
    public void syncPositionFromCamera(com.badlogic.gdx.graphics.PerspectiveCamera camera) {
        worldPosition.set(camera.position.x, camera.position.y, camera.position.z);
        pan  = 0f; // heading is managed by TouchControls, not needed here
        tilt = 0f;
    }

    // ─── block interaction ────────────────────────────────────────────────────

    /**
     * Perform a ray-cast from the given origin+direction and break the first
     * solid block hit.  Does nothing if no solid block is within reach.
     *
     * @param origin    ray start (camera eye)
     * @param direction normalised ray direction
     * @param world     active world
     * @return true if a block was broken
     */
    public boolean breakBlock(Vector3f origin, Vector3f direction, World world) {
        RayCasting.traceSimpleRay(ray, origin, direction, REACH, world);
        if (!ray.hitTarget) return false;

        Vector3i pos = ray.getHitPositionAsInt();
        Block block = world.getBlock(pos.x, pos.y, pos.z);
        if (block == null || !block.solid) return false;

        world.setBlock(Registrys.getBlock("air").id, pos.x, pos.y, pos.z);
        return true;
    }

    /**
     * Perform a ray-cast and place the selected hotbar block against the first
     * solid surface hit.  Uses the hit normal to find the adjacent air cell.
     *
     * @param origin    ray start (camera eye)
     * @param direction normalised ray direction
     * @param world     active world
     * @return true if a block was placed
     */
    public boolean placeBlock(Vector3f origin, Vector3f direction, World world) {
        RayCasting.traceSimpleRay(ray, origin, direction, REACH, world);
        if (!ray.hitTarget) return false;

        ItemStack held = getHotbarItem();
        if (held == null) return false;

        Item item = held.item;
        if (item == null || item.block == null) return false;

        Vector3i placePos = ray.getHitPosPlusNormal();
        Block existing = world.getBlock(placePos.x, placePos.y, placePos.z);
        if (existing != null && existing.solid) return false; // occupied

        world.setBlock(item.block.id, placePos.x, placePos.y, placePos.z);

        // Consume one item from the stack
        held.stackSize--;
        if (held.stackSize <= 0) {
            inventory.set(hotbarSlot, null);
        }
        return true;
    }

    // ─── persistence ─────────────────────────────────────────────────────────

    /**
     * Save this player's position, stats and inventory to the given world
     * directory.  Called by {@link World#save()} via reflection-safe cast to
     * keep {@link World} compatible with both the desktop and GDX paths.
     */
    public void saveToWorld(WorldData worldData) {
        if (worldData == null || worldData.getDirectory() == null) return;
        File playerFile = new File(worldData.getDirectory(), PLAYER_DATA_FILE);
        JsonObject obj = new JsonObject();
        obj.add("inventory", pdGson.toJsonTree(inventory.getList()));
        obj.addProperty("x", worldPosition.x);
        obj.addProperty("y", worldPosition.y);
        obj.addProperty("z", worldPosition.z);
        obj.addProperty("spawnX", spawnPosition.x);
        obj.addProperty("spawnY", spawnPosition.y);
        obj.addProperty("spawnZ", spawnPosition.z);
        obj.addProperty("health", health);
        obj.addProperty("hunger", food);
        obj.addProperty("oxygen", oxygen);
        try {
            Files.write(playerFile.toPath(), pdGson.toJson(obj).getBytes());
        } catch (IOException e) {
            com.badlogic.gdx.Gdx.app.error("GdxPlayer", "Failed to save player data", e);
        }
    }

    /**
     * Load this player's position, stats and inventory from the given world
     * directory.
     */
    public void loadFromWorld(WorldData worldData) {
        if (worldData == null || worldData.getDirectory() == null) return;
        File playerFile = new File(worldData.getDirectory(), PLAYER_DATA_FILE);
        if (!playerFile.exists()) return;
        try {
            String json = new String(Files.readAllBytes(playerFile.toPath()));
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            if (obj.has("inventory")) {
                AtomicInteger i = new AtomicInteger(0);
                obj.get("inventory").getAsJsonArray().forEach(el -> {
                    ItemStack stack = pdGson.fromJson(el, ItemStack.class);
                    inventory.set(i.getAndIncrement(), stack);
                });
            }
            if (obj.has("x"))      worldPosition.x = obj.get("x").getAsFloat();
            if (obj.has("y"))      worldPosition.y = obj.get("y").getAsFloat();
            if (obj.has("z"))      worldPosition.z = obj.get("z").getAsFloat();
            if (obj.has("spawnX")) spawnPosition.x = obj.get("spawnX").getAsFloat();
            if (obj.has("spawnY")) spawnPosition.y = obj.get("spawnY").getAsFloat();
            if (obj.has("spawnZ")) spawnPosition.z = obj.get("spawnZ").getAsFloat();
            if (obj.has("health")) health = obj.get("health").getAsFloat();
            if (obj.has("hunger")) food   = obj.get("hunger").getAsFloat();
            if (obj.has("oxygen")) oxygen = obj.get("oxygen").getAsFloat();
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("GdxPlayer", "Failed to load player data", e);
        }
    }

    /** Return spawn position (the last set spawn point). */
    public Vector3f getSpawnPosition() { return spawnPosition; }

    /** Set the spawn point. */
    public void setSpawnPosition(float x, float y, float z) {
        spawnPosition.set(x, y, z);
    }
}
