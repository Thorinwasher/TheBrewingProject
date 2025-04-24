package dev.jsinco.brewery.bukkit.breweries;

import dev.jsinco.brewery.brew.Brew;
import dev.jsinco.brewery.brew.BrewingStep;
import dev.jsinco.brewery.breweries.Distillery;
import dev.jsinco.brewery.bukkit.TheBrewingProject;
import dev.jsinco.brewery.bukkit.brew.BrewAdapter;
import dev.jsinco.brewery.bukkit.brew.BukkitDistilleryBrewDataType;
import dev.jsinco.brewery.bukkit.structure.PlacedBreweryStructure;
import dev.jsinco.brewery.bukkit.util.BukkitAdapter;
import dev.jsinco.brewery.bukkit.util.WrapperFactoryImpl;
import dev.jsinco.brewery.configuration.locale.TranslationsConfig;
import dev.jsinco.brewery.database.PersistenceException;
import dev.jsinco.brewery.structure.StructureMeta;
import dev.jsinco.brewery.util.Pair;
import dev.jsinco.brewery.util.Wrapper;
import dev.jsinco.brewery.vector.BreweryLocation;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BukkitDistillery implements Distillery<BukkitDistillery, ItemStack, Inventory> {

    @Getter
    private final PlacedBreweryStructure<BukkitDistillery> structure;
    @Getter
    private long startTime;
    @Getter
    private final DistilleryInventory mixture;
    @Getter
    private final DistilleryInventory distillate;
    private boolean dirty = true;
    private final Set<BreweryLocation> mixtureContainerLocations = new HashSet<>();
    private final Set<BreweryLocation> distillateContainerLocations = new HashSet<>();


    public BukkitDistillery(@NotNull PlacedBreweryStructure<BukkitDistillery> structure) {
        this(structure, TheBrewingProject.getInstance().getTime());
    }

    public BukkitDistillery(@NotNull PlacedBreweryStructure<BukkitDistillery> structure, long startTime) {
        this.structure = structure;
        this.startTime = startTime;
        this.mixture = new DistilleryInventory("Distillery Mixture", structure.getStructure().getMeta(StructureMeta.INVENTORY_SIZE), this);
        this.distillate = new DistilleryInventory("Distillery Distillate", structure.getStructure().getMeta(StructureMeta.INVENTORY_SIZE), this);
    }

    public boolean open(@NotNull BreweryLocation location, @NotNull UUID playerUuid) {
        checkDirty();
        Player player = Bukkit.getPlayer(playerUuid);
        if (mixtureContainerLocations.contains(location)) {
            if (!player.hasPermission("brewery.distillery.access")) {
                player.sendMessage(MiniMessage.miniMessage().deserialize(TranslationsConfig.DISTILLERY_ACCESS_DENIED));
                return true;
            }
            mixture.updateInventoryFromBrews();
            TheBrewingProject.getInstance().getBreweryRegistry().registerOpened(this);
            player.openInventory(mixture.getInventory());
            return true;
        }
        if (distillateContainerLocations.contains(location)) {
            if (!player.hasPermission("brewery.distillery.access")) {
                player.sendMessage(MiniMessage.miniMessage().deserialize(TranslationsConfig.DISTILLERY_ACCESS_DENIED));
                return true;
            }
            distillate.updateInventoryFromBrews();
            TheBrewingProject.getInstance().getBreweryRegistry().registerOpened(this);
            player.openInventory(distillate.getInventory());
        }
        return false;
    }

    @Override
    public boolean inventoryAllows(@NotNull Wrapper<UUID, ?> playerWrapper, @NotNull ItemStack item) {
        return WrapperFactoryImpl.playerWrapperType()
                .value(playerWrapper)
                .filter(player -> player.hasPermission("brewery.distillery.access"))
                .filter(ignored -> BrewAdapter.fromItem(item).isPresent())
                .isPresent();
    }

    @Override
    public Set<Inventory> getInventories() {
        return Set.of(mixture.getInventory(), distillate.getInventory());
    }

    /**
     * Made to avoid chunk access on startup
     */
    private void checkDirty() {
        if (!dirty) {
            return;
        }
        dirty = false;
        Set<BreweryLocation> potLocations = new HashSet<>();
        Material taggedMaterial = Material.valueOf(structure.getStructure().getMeta(StructureMeta.TAGGED_MATERIAL).toUpperCase(Locale.ROOT));
        for (BreweryLocation location : structure.positions()) {
            BukkitAdapter.toBlock(location)
                    .map(Block::getType)
                    .filter(taggedMaterial::equals)
                    .ifPresent(ignored -> potLocations.add(location));
        }
        for (BreweryLocation breweryLocation : potLocations) {
            if (potLocations.contains(breweryLocation.add(0, 1, 0)) || potLocations.contains(breweryLocation.add(0, -1, 0))) {
                mixtureContainerLocations.add(breweryLocation);
            } else {
                distillateContainerLocations.add(breweryLocation);
            }
        }
    }

    public void tick() {
        checkDirty();
        boolean hasChanged;
        if (mixture.getInventory().getViewers().isEmpty()) {
            mixture.getInventory().clear(); // Depopulate inventory, save on memory
            hasChanged = false;
        } else {
            hasChanged = mixture.updateBrewsFromInventory();
        }
        if (distillate.getInventory().getViewers().isEmpty()) {
            distillate.getInventory().clear(); // Depopulate inventory, save on memory
        } else {
            distillate.updateBrewsFromInventory();
        }
        if (hasChanged) {
            resetStartTime();
        }
        if (distillate.getInventory().getViewers().isEmpty() && mixture.getInventory().getViewers().isEmpty()) {
            TheBrewingProject.getInstance().getBreweryRegistry().unregisterOpened(this);
        }
        if (TheBrewingProject.getInstance().getTime() - startTime < getStructure().getStructure().getMeta(StructureMeta.PROCESS_TIME) || mixture.isEmpty()) {
            return;
        }
        transferItems(mixture, distillate);
        if (!distillate.getInventory().getViewers().isEmpty()) {
            distillate.updateInventoryFromBrews();
        }
        if (!mixture.getInventory().getViewers().isEmpty()) {
            mixture.updateInventoryFromBrews();
        }
        resetStartTime();
    }

    private void resetStartTime() {
        startTime = TheBrewingProject.getInstance().getTime();
    }

    private void transferItems(DistilleryInventory inventory1, DistilleryInventory inventory2) {
        Brew[] brews = inventory1.getBrews();
        int i = 0;
        for (int j = 0; j < inventory2.getBrews().length; j++) {
            if (inventory2.getBrews()[j] != null) {
                continue; // Avoid overriding brews
            }
            while (i < brews.length) {
                if (brews[i] != null) {
                    break;
                }
                i++;
            }
            if (i >= brews.length) {
                return;
            }
            Brew mixtureBrew = inventory1.getBrews()[i];
            inventory1.store(null, i);
            inventory2.store(mixtureBrew.withLastStep(
                            BrewingStep.Distill.class,
                            BrewingStep.Distill::incrementAmount,
                            () -> new BrewingStep.Distill(1))
                    , i);
        }
    }

    @Override
    public void destroy(BreweryLocation breweryLocation) {
        Optional<Location> location = BukkitAdapter.toLocation(breweryLocation)
                .map(location1 -> location1.add(0.5, 0, 0.5));
        for (DistilleryInventory distilleryInventory : List.of(distillate, mixture)) {
            List.copyOf(distilleryInventory.getInventory().getViewers()).forEach(HumanEntity::closeInventory);
            distilleryInventory.getInventory().clear();
            for (Brew brew : distilleryInventory.getBrews()) {
                if (brew == null) {
                    continue;
                }
                location.ifPresent(location1 -> location1.getWorld().dropItem(location1, BrewAdapter.toItem(brew, new Brew.State.Other())));
            }
        }
    }

    public static class DistilleryInventory implements InventoryHolder {

        private final Inventory inventory;
        @Getter
        private final Brew[] brews;
        private final BukkitDistillery owner;

        public DistilleryInventory(String title, int size, BukkitDistillery owner) {
            this.inventory = Bukkit.createInventory(this, size, title);
            this.brews = new Brew[size];
            this.owner = owner;
        }

        @NotNull
        @Override
        public Inventory getInventory() {
            return inventory;
        }

        /**
         * Set an item in the inventory without changing the database
         *
         * @param brew
         * @param position
         */
        public void set(@Nullable Brew brew, int position) {
            brews[position] = brew;
        }

        /**
         * Set an item in the inventory and store the changes in the database
         *
         * @param brew
         * @param position
         */
        public void store(@Nullable Brew brew, int position) {
            if (Objects.equals(brews[position], brew)) {
                return;
            }
            try {
                Brew previous = brews[position];
                set(brew, position);
                BreweryLocation unique = owner.getStructure().getUnique();
                BukkitDistilleryBrewDataType.DistilleryContext context = new BukkitDistilleryBrewDataType.DistilleryContext(unique.x(), unique.y(), unique.z(), unique.world(), position, this == owner.getDistillate());
                Pair<Brew, BukkitDistilleryBrewDataType.DistilleryContext> data = new Pair<>(brew, context);
                if (previous == null) {
                    TheBrewingProject.getInstance().getDatabase().insertValue(BukkitDistilleryBrewDataType.INSTANCE, data);
                    return;
                }
                if (brew == null) {
                    TheBrewingProject.getInstance().getDatabase().remove(BukkitDistilleryBrewDataType.INSTANCE, data);
                    return;
                }
                TheBrewingProject.getInstance().getDatabase().updateValue(BukkitDistilleryBrewDataType.INSTANCE, data);
            } catch (PersistenceException e) {
                e.printStackTrace();
            }

        }

        public void updateInventoryFromBrews() {
            for (int i = 0; i < brews.length; i++) {
                Brew brew = brews[i];
                if (brew == null) {
                    inventory.setItem(i, null);
                    continue;
                }
                inventory.setItem(i, BrewAdapter.toItem(brew, new Brew.State.Brewing()));
            }
        }

        public boolean updateBrewsFromInventory() {
            boolean hasUpdated = false;
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack itemStack = inventory.getItem(i);
                Brew brew;
                if (itemStack == null) {
                    brew = null;
                } else {
                    brew = BrewAdapter.fromItem(itemStack).orElse(null);
                }
                if (!Objects.equals(brew, brews[i])) {
                    hasUpdated = true;
                }
                store(brew, i);
            }
            return hasUpdated;
        }

        public boolean isEmpty() {
            for (Brew brew : brews) {
                if (brew != null) {
                    return false;
                }
            }
            return true;
        }
    }
}
