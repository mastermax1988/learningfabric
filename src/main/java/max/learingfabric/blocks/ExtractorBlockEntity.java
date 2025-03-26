package max.learingfabric.blocks;

import com.mojang.serialization.MapCodec;
import max.learingfabric.LearningFabric;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public class ExtractorBlockEntity extends BlockEntity {

    private InserterBlock inserterBlock;
    public ExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EXTRACTOR_BLOCK_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, ExtractorBlockEntity extractorBlockEntity) {
        LearningFabric.LOGGER.info("Extractor Block Entity Tick");
        if (world.isClient())
            return;
        if (extractorBlockEntity.inserterBlock != null && (world.getBlockEntity(blockPos.up()) instanceof ChestBlockEntity inventory)) {
            ItemStack itemStack = inventory.getStack(0);
            inventory.setStack(0, ItemStack.EMPTY);
            inventory.markDirty();
            if (itemStack != null && !itemStack.isEmpty()) {
                extractorBlockEntity.inserterBlock.insert(itemStack);
                LearningFabric.LOGGER.info("Extractor Block Entity Tick: " + itemStack.toString());
            }
        }
    }


    public void setInserterBlock(InserterBlock inserterBlock) {
        this.inserterBlock = inserterBlock;
    }
}
