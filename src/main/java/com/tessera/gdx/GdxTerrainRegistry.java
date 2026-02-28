package com.tessera.gdx;

import com.tessera.engine.server.world.Terrain;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the list of available terrain generators for the libGDX game path.
 * Populated by {@link GdxGameInitializer#initGLResources}.
 */
public class GdxTerrainRegistry {
    public static final List<Terrain> terrains = new ArrayList<>();

    public static Terrain getTerrainByName(String name) {
        for (Terrain t : terrains) {
            if (t.name.equals(name)) return t;
        }
        return null;
    }
}
