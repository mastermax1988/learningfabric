package max.learingfabric.blocks;

import com.mojang.serialization.MapCodec;
import max.learingfabric.LearningFabric;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public class ExtractorBlock extends BlockWithEntity {

    private ExtractorBlockEntity entity;

    protected ExtractorBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(ExtractorBlock::new);
    }


    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return entity = new ExtractorBlockEntity(pos,state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntities.EXTRACTOR_BLOCK_ENTITY, ExtractorBlockEntity::tick);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        LearningFabric.linkingBlocks.put(player, pos);
        player.sendMessage(Text.literal("saved " + pos.toString()), true);
        return super.onUse(state,world,pos,player,hit);
    }

    public void setInserter(InserterBlock inserter) {
        entity.setInserterBlock(inserter);
    }
}
