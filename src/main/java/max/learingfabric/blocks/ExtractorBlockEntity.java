package max.learingfabric.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ExtractorBlockEntity extends BlockEntity {

    private InserterBlock inserterBlock;
    private BlockPos inserterPos;

    public ExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EXTRACTOR_BLOCK_ENTITY, pos, state);

    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putInt("inserterx", inserterPos.getX());
        nbt.putInt("insertery", inserterPos.getY());
        nbt.putInt("inserterz", inserterPos.getZ());
        super.writeNbt(nbt, registries);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        inserterPos = new BlockPos(nbt.getInt("inserterx"), nbt.getInt("insertery"), nbt.getInt("inserterz"));
        setInserterBlock(inserterPos);
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, ExtractorBlockEntity extractorBlockEntity) {
        if (world.isClient())
            return;
              if (extractorBlockEntity.inserterBlock != null && (world.getBlockEntity(blockPos.up()) instanceof ChestBlockEntity inventory)) {
            ItemStack itemStack;
            int index = 0;
            do {
                itemStack = inventory.getStack(index);
                index++;
            } while (itemStack.isEmpty() && index < inventory.size());
            index--;
            if (itemStack.isEmpty()) {
                return;
            }
            inventory.setStack(index, ItemStack.EMPTY);
            inventory.markDirty();
            extractorBlockEntity.inserterBlock.insert(itemStack);
        }
    }


    public void setInserterBlock(BlockPos pos) {
        inserterPos = pos;
        this.inserterBlock = (InserterBlock) world.getBlockState(pos).getBlock();
    }
}
