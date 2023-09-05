package io.github.fabricators_of_create.porting_lib.entity.mixin.common;

import io.github.fabricators_of_create.porting_lib.entity.events.EntityEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(targets = "net/minecraft/world/level/entity/PersistentEntitySectionManager$Callback")
public abstract class PersistentEntitySectionManager$CallbackMixin<T extends EntityAccess> {
	@Shadow
	private long currentSectionKey;

	@Shadow
	@Final
	private T entity;
	@Unique
	private Entity port_lib$realEntity;
	@Unique
	private long port_lib$oldSectionKey;

	@Inject(method = "<init>", at = @At("TAIL"))
	public void port_lib$init(PersistentEntitySectionManager persistentEntitySectionManager, EntityAccess entityAccess, long l, EntitySection entitySection, CallbackInfo ci) {
		this.port_lib$realEntity = entityAccess instanceof Entity e ? e : null;
	}

	@Inject(method = "onMove", at = @At("TAIL"))
	public void port_lib$onEntityEnter(CallbackInfo ci) {
		BlockPos blockPos = this.entity.blockPosition();
		long section = SectionPos.asLong(blockPos);
		if (this.port_lib$realEntity != null)
			EntityEvents.ENTERING_SECTION.invoker().onEntityEnterSection(this.port_lib$realEntity, port_lib$oldSectionKey, section);
	}

	@Inject(method = "onMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/entity/EntitySection;add(Lnet/minecraft/world/level/entity/EntityAccess;)V", shift = At.Shift.AFTER))
	public void port_lib$updateOldKey(CallbackInfo ci) {
		port_lib$oldSectionKey = currentSectionKey;
	}
}
