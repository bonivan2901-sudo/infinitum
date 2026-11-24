package com.ivan21.infinitum.menu;

import com.ivan21.infinitum.recipe.ExtremeShapedRecipe;
import com.ivan21.infinitum.registry.ModBlocks;
import com.ivan21.infinitum.registry.ModMenus;
import com.ivan21.infinitum.registry.ModRecipes;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * Меню экстремального верстака.
 * Слоты: 0 — результат, 1..81 — сетка 9x9, 82..108 — инвентарь, 109..117 — хотбар.
 */
public class ExtremeCraftingMenu extends AbstractContainerMenu {
    public static final int GRID = 9;
    public static final int RESULT_SLOT = 0;
    public static final int GRID_START = 1;
    public static final int GRID_END = GRID_START + GRID * GRID; // 82
    public static final int INV_START = GRID_END;                // 82
    public static final int INV_END = INV_START + 36;            // 118

    public static final int GRID_X = 8;
    public static final int GRID_Y = 18;
    public static final int RESULT_X = 206;
    public static final int RESULT_Y = 90;
    public static final int INV_X = 38;
    public static final int INV_Y = 196;

    private final CraftingContainer craftSlots = new TransientCraftingContainer(this, GRID, GRID);
    private final ResultContainer resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private final Player player;

    /** Клиентский конструктор. */
    public ExtremeCraftingMenu(int id, Inventory inv) {
        this(id, inv, ContainerLevelAccess.NULL);
    }

    /** Серверный конструктор. */
    public ExtremeCraftingMenu(int id, Inventory inv, ContainerLevelAccess access) {
        super(ModMenus.EXTREME_CRAFTING.get(), id);
        this.access = access;
        this.player = inv.player;

        addSlot(new ExtremeResultSlot(this, craftSlots, resultSlots, 0, RESULT_X, RESULT_Y));

        for (int row = 0; row < GRID; row++) {
            for (int col = 0; col < GRID; col++) {
                addSlot(new Slot(craftSlots, col + row * GRID, GRID_X + col * 18, GRID_Y + row * 18));
            }
        }
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inv, col + row * 9 + 9, INV_X + col * 18, INV_Y + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inv, col, INV_X + col * 18, INV_Y + 58));
        }
    }

    @Override
    public void slotsChanged(Container container) {
        access.execute((level, pos) -> updateResult(level));
    }

    private void updateResult(Level level) {
        if (level.isClientSide || !(player instanceof ServerPlayer serverPlayer)) return;
        Optional<ExtremeShapedRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipes.EXTREME_TYPE.get(), craftSlots, level);
        ItemStack out = recipe.map(r -> r.assemble(craftSlots, level.registryAccess())).orElse(ItemStack.EMPTY);
        resultSlots.setItem(0, out);
        setRemoteSlot(RESULT_SLOT, out);
        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(containerId, incrementStateId(), RESULT_SLOT, out));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index == RESULT_SLOT) {
                if (!moveItemStackTo(stack, INV_START, INV_END, true)) return ItemStack.EMPTY;
                slot.onQuickCraft(stack, result);
            } else if (index >= INV_START) {
                if (!moveItemStackTo(stack, GRID_START, GRID_END, false)) return ItemStack.EMPTY;
            } else if (!moveItemStackTo(stack, INV_START, INV_END, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();

            if (stack.getCount() == result.getCount()) return ItemStack.EMPTY;
            slot.onTake(player, stack);
        }
        return result;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != resultSlots && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        access.execute((level, pos) -> clearContainer(player, craftSlots));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, ModBlocks.EXTREME_CRAFTING_TABLE.get());
    }
}
