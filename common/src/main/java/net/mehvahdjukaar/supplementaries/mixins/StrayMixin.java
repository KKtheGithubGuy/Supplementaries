package net.mehvahdjukaar.supplementaries.mixins;

import net.mehvahdjukaar.supplementaries.api.IQuiverEntity;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.common.network.SyncSkellyQuiverPacket;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Stray.class)
public abstract class StrayMixin extends AbstractSkeleton implements IQuiverEntity {
    //frick it going full mixin here. I could have used caps and spawn events...

    //server
    @Unique
    private ItemStack supplementaries$quiver = ItemStack.EMPTY;
    @Unique
    private float supplementaries$quiverDropChance = 0.6f;

    protected StrayMixin(EntityType<? extends AbstractSkeleton> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int looting, boolean hitByPlayer) {
        super.dropCustomDeathLoot(damageSource, looting, hitByPlayer);
        if (this.supplementaries$quiver != null && hitByPlayer) {
            ItemStack itemStack = this.supplementaries$quiver;
            if (Math.max(this.random.nextFloat() - looting * 0.02F, 0.0F) < supplementaries$quiverDropChance) {
                this.spawnAtLocation(itemStack);
                this.supplementaries$quiver = ItemStack.EMPTY;
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (!this.supplementaries$quiver.isEmpty()) {
            compound.put("Quiver", supplementaries$quiver.save(new CompoundTag()));
            compound.putFloat("QuiverDropChance", supplementaries$quiverDropChance);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Quiver")) {
            this.supplementaries$setQuiver(ItemStack.of(compound.getCompound("Quiver")));
            this.supplementaries$quiverDropChance = compound.getFloat("QuiverDropChance");
        }
    }

    @Override
    public ItemStack supplementaries$getQuiver() {
        return supplementaries$quiver;
    }

    @Override
    public void supplementaries$setQuiver(ItemStack quiver) {
        this.supplementaries$quiver = quiver;
        if(!level().isClientSide){
            //only needed when entity is alraedy spawned
            NetworkHelper.sentToAllClientPlayersTrackingEntity(this,
                    new SyncSkellyQuiverPacket(this));
        }
    }

    @Override
    public boolean wantsToPickUp(ItemStack stack) {
        if (this.supplementaries$quiver == null && stack.getItem() == ModRegistry.QUIVER_ITEM.get()) return true;
        return super.wantsToPickUp(stack);
    }

    @Override
    public ItemStack equipItemIfPossible(ItemStack stack) {
        if(stack.getItem() == ModRegistry.QUIVER_ITEM.get()){
            if(this.supplementaries$quiver != null){
                this.spawnAtLocation(supplementaries$quiver);
            }
            this.supplementaries$setQuiver(stack);
            this.supplementaries$quiverDropChance = 1;
            return stack;
        }
        return super.equipItemIfPossible(stack);
    }
}