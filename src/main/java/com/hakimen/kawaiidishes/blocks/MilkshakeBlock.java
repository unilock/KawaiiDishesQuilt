package com.hakimen.kawaiidishes.blocks;

import com.hakimen.kawaiidishes.blocks.block_entities.PlaceableFoodBlockEntity;
import com.hakimen.kawaiidishes.registry.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MilkshakeBlock extends Block implements EntityBlock {

    public MilkshakeBlock(){
        super(Properties.copy(Blocks.WHITE_WOOL)
                .sound(SoundType.STONE)
                .strength(1,1)
                .isSuffocating((p_61036_, p_61037_, p_61038_) -> false));
    }


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(5.0D, 0D, 5.0D, 11.0D, 13.0D, 11.0D);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pPlayer.isCrouching()){
            var stack = this.asItem().getDefaultInstance();
            stack.getOrCreateTag();
            if(pLevel.getBlockEntity(pPos) instanceof PlaceableFoodBlockEntity entity){
                if(!entity.mainEffect.equals(new CompoundTag())){
                    stack.getOrCreateTag().put("mainEffect",entity.mainEffect);
                }
                if(!entity.secondaryEffect.equals(new CompoundTag())){
                    stack.getOrCreateTag().put("secondaryEffect",entity.secondaryEffect);
                }
            }
            pLevel.removeBlock(pPos,false);
            pPlayer.addItem(stack);
            return InteractionResult.SUCCESS;
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        pLevel.getBlockEntity(pPos, BlockEntityRegister.placeableFood.get()).ifPresent((a) -> {
            a.load(pStack.getOrCreateTag());
        });
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        var stack = this.asItem().getDefaultInstance();
        stack.getOrCreateTag();
        if(level.getBlockEntity(pos) instanceof PlaceableFoodBlockEntity entity){
            if(!entity.mainEffect.equals(new CompoundTag())){
                stack.getOrCreateTag().put("mainEffect",entity.mainEffect);
            }
            if(!entity.secondaryEffect.equals(new CompoundTag())){
                stack.getOrCreateTag().put("secondaryEffect",entity.secondaryEffect);
            }
        }
        level.addFreshEntity(new ItemEntity(level,pos.getX(),pos.getY(),pos.getZ(),stack));
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PlaceableFoodBlockEntity(pPos,pState);
    }
}
