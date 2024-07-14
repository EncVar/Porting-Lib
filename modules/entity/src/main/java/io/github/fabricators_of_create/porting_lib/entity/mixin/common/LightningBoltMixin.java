package io.github.fabricators_of_create.porting_lib.entity.mixin.common;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;

import io.github.fabricators_of_create.porting_lib.core.util.MixinHelper;
import io.github.fabricators_of_create.porting_lib.entity.EntityHooks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightningBolt.class)
public class LightningBoltMixin {
	@WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;thunderHit(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LightningBolt;)V"))
	private boolean shouldStrikeEntity(Entity entity, ServerLevel level, LightningBolt lightningBolt) {
		return !EntityHooks.onEntityStruckByLightning(entity, MixinHelper.cast(this));
	}
}
