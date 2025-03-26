package max.learingfabric.blocks;

import com.mojang.serialization.MapCodec;
import max.learingfabric.LearningFabric;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class InserterBlock extends BlockWithEntity {
    private BlockPos chestPos;
    private World world;
    protected InserterBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(InserterBlock::new);
    }


    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new InserterBlockEntity(pos,state);
    }


    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        chestPos = pos.up();
        this.world = world;

        BlockPos extractor = LearningFabric.linkingBlocks.get(player);
        if(extractor == null || !(world.getBlockState(extractor).getBlock() instanceof ExtractorBlock)){
            player.sendMessage(Text.literal("You need to link an extractor block first"),true);
        } else{
           ExtractorBlock extractorBlock = (ExtractorBlock) world.getBlockState(extractor).getBlock();
           extractorBlock.setInserter(this);
           player.sendMessage(Text.literal("You have linked an extractor block"),true);
        }
        return super.onUse(state,world,pos,player,hit);
    }

    public void insert(ItemStack stack){
        LearningFabric.LOGGER.info("Inserting "+stack.toString()+" into inserter");
        var inventory = ChestBlock.getInventory((ChestBlock) world.getBlockState(chestPos).getBlock(), this.world.getBlockState(chestPos), world, chestPos, true);
        inventory.setStack(0,stack);
        inventory.markDirty();
    }
}
