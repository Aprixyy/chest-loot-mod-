package com.example.chestloot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ChestLootMod implements ModInitializer {
    private static final Gson GSON = new Gson();
    private static final Map<Identifier, String[]> biomeLootTableMap = new HashMap<>();

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
        System.out.println("Chest Loot Mod has been initialized!");
    }

    private void onServerStarted(MinecraftServer server) {
        try (FileReader reader = new FileReader(new File("C:\\Users\\Bianca\\Desktop\\chestloot_config.json"), StandardCharsets.UTF_8)) {
            JsonObject config = GSON.fromJson(reader, JsonObject.class);
            JsonObject biomes = config.getAsJsonObject("biomes");
            for (String biome : biomes.keySet()) {
                Identifier biomeId = new Identifier(biome);
                String[] lootTables = GSON.fromJson(biomes.getAsJsonArray(biome), String[].class);
                biomeLootTableMap.put(biomeId, lootTables);
            }
            System.out.println("Configuration loaded: " + biomeLootTableMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] getLootTablesForBiome(Identifier biome) {
        return biomeLootTableMap.getOrDefault(biome, new String[0]);
    }
}
