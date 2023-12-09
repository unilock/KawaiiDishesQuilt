package com.hakimen.kawaiidishes.blocks.block_entities;

import com.hakimen.kawaiidishes.containers.CoffeeMachineContainer;
import com.hakimen.kawaiidishes.recipes.CoffeeMachineRecipe;
import com.hakimen.kawaiidishes.registry.BlockEntityRegister;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CoffeeMachineBlockEntity extends BlockEntity implements MenuProvider,BlockEntityTicker<CoffeeMachineBlockEntity> {

    public final ItemStackHandler inventory = createHandler();
    public int progress = 0;
    public int recipeTicks = 0;

    protected final ContainerData data;

    private boolean isCrafting = false;
    public CoffeeMachineBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(BlockEntityRegister.coffeeMachine.get(), pWorldPosition, pBlockState);
        this.data = new ContainerData()     {
            public int get(int index) {
                switch (index) {
                    case 0: return CoffeeMachineBlockEntity.this.progress;
                    case 1: return CoffeeMachineBlockEntity.this.recipeTicks;
                    default: return 0;
                }
            }

            public void set(int index, int value) {
                switch(index) {
                    case 0: CoffeeMachineBlockEntity.this.progress = value; break;
                    case 1: CoffeeMachineBlockEntity.this.recipeTicks = value; break;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.merge(this.inventory.serializeNBT());
        pTag.putInt("progress",progress);
        pTag.putInt("recipeTicks",recipeTicks);
        pTag.putBoolean("isCrafting",isCrafting);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        progress = pTag.getInt("progress");
        recipeTicks = pTag.getInt("recipeTicks");
        isCrafting = pTag.getBoolean("isCrafting");
        this.inventory.deserializeNBT(pTag);
    }
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this,BlockEntity::saveWithFullMetadata);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithFullMetadata();
    }
    public static boolean hasRecipe(CoffeeMachineBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.inventory.getSlots().size());
        for (int i = 0; i < entity.inventory.getSlots().size(); i++) {
            inventory.setItem(i, entity.inventory.getStackInSlot(i));
        }

        Optional<CoffeeMachineRecipe> match = level.getRecipeManager()
                .getRecipeFor(CoffeeMachineRecipe.Type.INSTANCE, inventory, level);

        return match.isPresent();
    }


    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState, CoffeeMachineBlockEntity pBlockEntity) {
        if(hasRecipe(pBlockEntity)){
            Level level = pBlockEntity.level;
            SimpleContainer inventory = new SimpleContainer(pBlockEntity.inventory.getSlots().size());
            for (int i = 0; i < pBlockEntity.inventory.getSlots().size(); i++) {
                inventory.setItem(i, pBlockEntity.inventory.getStackInSlot(i));
            }
            Optional<CoffeeMachineRecipe> match = level.getRecipeManager()
                    .getRecipeFor(CoffeeMachineRecipe.Type.INSTANCE, inventory, level);
            if(match.isPresent()) {
                CoffeeMachineRecipe recipe = match.get();
                if(!isCrafting) {
                    isCrafting = true;
                    recipeTicks = recipe.getTicks();
                    setChanged();
                }else {
                    this.progress++;
                    setChanged();
                    if(progress >= recipeTicks) {
                        isCrafting = false;
                        progress = 0;
                        for (int i = 0; i < pBlockEntity.inventory.getSlots().size()-1; i++) {
                            if(i == 0 || i == 1){
                                var stack = pBlockEntity.inventory.getStackInSlot(i).getItem().getCraftingRemainingItem();
                                if (stack == null) {
                                    stack = ItemStack.EMPTY.getItem();
                                }
                                pBlockEntity.inventory.setStackInSlot(i, stack.getDefaultInstance());
                            }else{
                                pBlockEntity.inventory.setStackInSlot(i, ItemStack.EMPTY);
                            }

                        }
                        pBlockEntity.inventory.setStackInSlot(5,recipe.getResultItem(null));
                        setChanged();
                    }
                }
            }
        }else{
            if(progress > 0){
                progress--;
            }
        }
        setChanged();
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(6) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(), Block.UPDATE_ALL);
            }

            @Override
            public boolean isItemValid(int slot, ItemVariant resource, int count) {
                switch (slot){
                    case 0 ->{
                        return resource.isOf(Items.WATER_BUCKET);
                    }
                    case 1 ->{
                        return resource.isOf(Items.MILK_BUCKET.asItem())||resource.isOf(Items.BUCKET.asItem());
                    }
                }
                return true;
            }

            @Override
            public int getSlotLimit(int slot) {
                return slot == 5 ? 1 : 64;
            }

            @NotNull
            public long insertSlot(int slot, ItemVariant resource, long maxAmount, TransactionContext transaction) {
                if(!isItemValid(slot, resource, (int) maxAmount)) {
                    return 0;
                }

                return super.insertSlot(slot, resource, maxAmount, transaction);
            }
        };
    }


    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.kawaiidishes.coffee_machine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new CoffeeMachineContainer(pContainerId, pInventory, this, this.data);
    }
}
