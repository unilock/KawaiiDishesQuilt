package com.hakimen.kawaiidishes.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CandyBlock extends Block {

    public CandyBlock(){
        super(Properties.copy(Blocks.CAKE)
                .strength(.25f,0)
                .isSuffocating((p_61036_, p_61037_, p_61038_) -> false));
    }


    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pPlayer.isCrouching()){
            var stack = this.asItem().getDefaultInstance();
            pLevel.addFreshEntity(new ItemEntity(pLevel,pPlayer.getX(),pPlayer.getY(),pPlayer.getZ(),stack));
            pLevel.removeBlock(pPos,false);
            return InteractionResult.SUCCESS;
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        var stack = this.asItem().getDefaultInstance();
        level.addFreshEntity(new ItemEntity(level,pos.getX(),pos.getY(),pos.getZ(),stack));
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

}
