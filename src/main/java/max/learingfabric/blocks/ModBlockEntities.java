package max.learingfabric.blocks;

import max.learingfabric.LearningFabric;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static void initialize() {}
    public static final BlockEntityType<MinerBlockEntity> MINER_BLOCK_ENTITY =
            register("miner", MinerBlockEntity::new, ModBlocks.MINER);
    public static final BlockEntityType<ExtractorBlockEntity> EXTRACTOR_BLOCK_ENTITY =
            register("extractor", ExtractorBlockEntity::new, ModBlocks.EXTRACTOR);
    public static final BlockEntityType<InserterBlockEntity> INSERTER_BLOCK_ENTITY =
            register("inserter", InserterBlockEntity::new, ModBlocks.INSERTER);

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Identifier.of(LearningFabric.MOD_ID, name);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
}
