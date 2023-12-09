package com.hakimen.kawaiidishes.blocks.block_entities;

import com.hakimen.kawaiidishes.blocks.CoffeePressBlock;
import com.hakimen.kawaiidishes.recipes.CoffeePressRecipe;
import com.hakimen.kawaiidishes.registry.BlockEntityRegister;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class CoffeePressBlockEntity extends BlockEntity {

    public final ItemStackHandler inventory = createHandler();

    public boolean coffeeGotMade;
    public ItemStack coffeeMade = ItemStack.EMPTY;
    public CoffeePressBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(BlockEntityRegister.coffeePress.get(), pWorldPosition, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.merge(this.inventory.serializeNBT());
        pTag.put("coffee",coffeeMade.serializeNBT());
        pTag.putBoolean("isDone",coffeeGotMade);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        this.inventory.deserializeNBT(pTag);
        coffeeMade.deserializeNBT(pTag.getCompound("coffee"));
        coffeeGotMade = pTag.getBoolean("isDone");
        super.load(pTag);
    }

    public static boolean hasRecipe(CoffeePressBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.inventory.getSlots().size());
        for (int i = 0; i < entity.inventory.getSlots().size(); i++) {
            if(entity.inventory.getStackInSlot(i) != ItemStack.EMPTY){
                inventory.setItem(i, entity.inventory.getStackInSlot(i));
            }
        }
        Optional<CoffeePressRecipe> match = level.getRecipeManager()
                .getRecipeFor(CoffeePressRecipe.Type.INSTANCE, inventory, level);
        return match.isPresent();
    }

    public static void craft(CoffeePressBlockEntity entity){
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.inventory.getSlots().size());
        for (int i = 0; i < entity.inventory.getSlots().size(); i++) {
            inventory.setItem(i, entity.inventory.getStackInSlot(i));
        }
        Optional<CoffeePressRecipe> match = level.getRecipeManager()
                .getRecipeFor(CoffeePressRecipe.Type.INSTANCE, inventory, level);
        if(match.isPresent()) {
            for (int i = 0; i < entity.inventory.getSlots().size(); i++) {
                var stack = entity.inventory.getStackInSlot(i).getItem().getCraftingRemainingItem();
                if(stack == null){
                    entity.inventory.setStackInSlot(i,ItemStack.EMPTY);
                }else{
                    entity.level.addFreshEntity(new ItemEntity(entity.level,
                            entity.getBlockPos().getX(),
                            entity.getBlockPos().getY(),
                            entity.getBlockPos().getZ(),
                            stack.getDefaultInstance()));
                    entity.inventory.setStackInSlot(i,ItemStack.EMPTY);
                }

            }
            entity.coffeeMade = match.get().getResultItem(null);
            entity.coffeeGotMade = true;
            level.setBlockAndUpdate(entity.getBlockPos(),entity.getBlockState().setValue(
                    CoffeePressBlock.PRESSED,true
            ));
        }
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(3) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, ItemVariant resource, int count) {
                return true;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            public long insertSlot(int slot, ItemVariant resource, long maxAmount, TransactionContext transaction) {
                if(!isItemValid(slot, resource, (int) maxAmount)) {
                    return 0;
                }

                return super.insertSlot(slot, resource, maxAmount, transaction);
            }
        };
    }

}
