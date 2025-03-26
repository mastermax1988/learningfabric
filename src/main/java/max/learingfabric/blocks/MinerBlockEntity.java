package max.learingfabric.blocks;

import max.learingfabric.LearningFabric;
import net.fabricmc.fabric.api.block.v1.FabricBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

import static net.minecraft.block.Block.dropStack;
import static net.minecraft.block.Block.getDroppedStacks;

public class MinerBlockEntity extends BlockEntity {

    private boolean done = false;
    private int x, y, z;


    public MinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MINER_BLOCK_ENTITY, pos, state);
        x = -5;
        y = -2;
        z = -5;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putInt("xk", x);
        nbt.putInt("yk", y);
        nbt.putInt("zk", z);
        nbt.putBoolean("done", done);
        super.writeNbt(nbt, registries);
        LearningFabric.LOGGER.info("MinerBlockEntity written into nbt " + y);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        x = nbt.getInt("xk");
        y = nbt.getInt("yk");
        z = nbt.getInt("zk");
        done = nbt.getBoolean("done");
        LearningFabric.LOGGER.info("MinerBlockEntity read nbt " + y);
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, MinerBlockEntity entity) {
        if (entity.done) return;

        entity.z++;
        if (entity.z > 5) {
            entity.z = -5;
            entity.x++;
            if (entity.x > 5) {
                entity.x = -5;
                entity.y--;
                if (blockPos.getY() + entity.y <= -63) {
                    entity.done = true;
                    return;
                }
            }
        }
        BlockPos toMine = blockPos.add(entity.x, entity.y, entity.z);
        BlockState b = world.getBlockState(toMine);
        BlockState above = world.getBlockState(blockPos.up());
        ItemStack drop;
        try {
            drop = getDroppedStacks(b, Objects.requireNonNull(world.getServer()).getWorld(world.getRegistryKey()), toMine, entity).getFirst();
        } catch (Exception e) {
            return;
        }
        world.breakBlock(toMine, false);
        if(above.getBlock() instanceof ChestBlock chest) {
            Inventory inventory = ChestBlock.getInventory(chest, above, world, blockPos.up(), true);
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack stack = inventory.getStack(i);
                if(stack.isEmpty()) {
                    inventory.setStack(i, drop);
                    inventory.markDirty();
                    return;
                } if(stack.getItem().equals(drop.getItem()) && stack.getCount() < stack.getMaxCount()) {
                    stack.increment(1);
                    inventory.markDirty();
                    return;
                }
            }

        } else if(above.getBlock() instanceof FurnaceBlock furnace) {
            LearningFabric.LOGGER.info("MinerBlockEntity above " + above.getBlock().getClass().getSimpleName());
        }
        Block.dropStacks(b, world, blockPos.up());
    }
}
