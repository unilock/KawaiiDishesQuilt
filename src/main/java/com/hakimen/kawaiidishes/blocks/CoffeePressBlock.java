package com.hakimen.kawaiidishes.blocks;

import com.hakimen.kawaiidishes.blocks.block_entities.CoffeePressBlockEntity;
import com.hakimen.kawaiidishes.registry.ItemRegister;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CoffeePressBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty PRESSED = BooleanProperty.create("pressed");
    public CoffeePressBlock() {
        super(Properties.copy(Blocks.WHITE_WOOL).strength(2f,2f)
                .sound(SoundType.GLASS));
        registerDefaultState( getStateDefinition().any()
                .setValue(FACING, Direction.NORTH )
                .setValue(PRESSED, false));
    }



    @Override
    @Deprecated
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(4.5f,0,4.5f,11.5,11,11.5);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> properties )
    {
        properties.add( FACING );
        properties.add( PRESSED );
    }

    @NotNull
    @Override
    @Deprecated
    public BlockState mirror( BlockState state, Mirror mirrorIn )
    {
        return state.rotate( mirrorIn.getRotation( state.getValue( FACING ) ) );
    }
    @NotNull
    @Override
    @Deprecated
    public BlockState rotate( BlockState state, Rotation rot )
    {
        return state.setValue( FACING, rot.rotate( state.getValue( FACING ) ) );
    }

    @Override
    public BlockState getStateForPlacement( BlockPlaceContext placement )
    {
        return defaultBlockState().setValue( FACING, placement.getHorizontalDirection().getOpposite() );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CoffeePressBlockEntity(pPos,pState);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        if(level.getBlockEntity(pos) instanceof CoffeePressBlockEntity coffeePressBlockEntity){
            for (int i = 0; i < coffeePressBlockEntity.inventory.getSlots().size(); i++) {
                level.addFreshEntity(new ItemEntity(level,pos.getX(),pos.getY(),pos.getZ(),coffeePressBlockEntity.inventory.getStackInSlot(i)));
            }
        }
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if(blockEntity instanceof CoffeePressBlockEntity coffeePress){
            ItemStack currentItemInHand = pPlayer.getItemInHand(pHand).copy();
            if(currentItemInHand.equals(ItemStack.EMPTY)){
                if(pPlayer.isCrouching()){
                    for (int i = coffeePress.inventory.getSlots().size()-1; i > -1; i--) {
                        if(coffeePress.inventory.getStackInSlot(i) != ItemStack.EMPTY){
                            // TODO: ehhh
                            pPlayer.addItem(coffeePress.inventory.getStackInSlot(i));
                            coffeePress.inventory.setStackInSlot(i,ItemStack.EMPTY);
                            break;
                        }
                    }
                }else if(CoffeePressBlockEntity.hasRecipe(coffeePress)){
                    CoffeePressBlockEntity.craft(coffeePress);
                }
            }else if(currentItemInHand.getItem().equals(ItemRegister.mug.get()) && coffeePress.coffeeGotMade){
                if(coffeePress.coffeeMade.getCount() > 0){
                    pPlayer.addItem(coffeePress.coffeeMade);
                    coffeePress.coffeeGotMade = false;
                    pLevel.setBlockAndUpdate(coffeePress.getBlockPos(),coffeePress.getBlockState().setValue(
                            CoffeePressBlock.PRESSED,coffeePress.coffeeGotMade
                    ));
                }else{
                    coffeePress.coffeeGotMade = false;
                    pLevel.setBlockAndUpdate(coffeePress.getBlockPos(),coffeePress.getBlockState().setValue(
                            CoffeePressBlock.PRESSED,coffeePress.coffeeGotMade
                    ));

                }
                pPlayer.getItemInHand(pHand).shrink(1);

            }else if(coffeePress.inventory.getStackInSlot(0).getCount() < 3){
                var stack = currentItemInHand.copy();
                stack.setCount(1);
                for (int i = 0; i < coffeePress.inventory.getSlots().size(); i++) {
                    if(coffeePress.inventory.getStackInSlot(i) == ItemStack.EMPTY){
                        Transaction transaction = Transaction.openOuter();
                        coffeePress.inventory.insertSlot(i,ItemVariant.of(stack),stack.getCount(),transaction);
                        pPlayer.getItemInHand(pHand).shrink(1);
                        transaction.commit();
                        break;
                    }
                }

            }
        }


        return InteractionResult.SUCCESS;
    }
}
