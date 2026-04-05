//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package shadows.apotheosis.mixin;

import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import shadows.apotheosis.core.attributeslib.asm.ALCombatRules;
import shadows.apotheosis.core.mobfx.api.MFEffects;

@Mixin({LivingEntity.class})
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @ModifyVariable(
            ordinal = 0,
            argsOnly = true,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z"
            ),
            method = {"getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"}
    )
    public float apoth_sunderingApplyEffect(float value, DamageSource pDamageSource, float pDamageAmount) {
        if (this.hasEffect(MFEffects.SUNDERING.get()) && pDamageSource != DamageSource.OUT_OF_WORLD) {
            int level = this.getEffect(MFEffects.SUNDERING.get()).getAmplifier() + 1;
            value += pDamageAmount * level * 0.05F;
        }

        return value;
    }

    public float apoth_sunderingApplyEffect(float value, float max, DamageSource source, float damage) {
        if (this.hasEffect(MFEffects.SUNDERING.get()) && source != DamageSource.OUT_OF_WORLD) {
            int level = this.getEffect(MFEffects.SUNDERING.get()).getAmplifier() + 1;
            value += damage * level * 0.05F;
        }

        return Math.max(value, max);
    }

    public boolean apoth_sunderingHasEffect(LivingEntity ths, MobEffect effect) {
        return true;
    }

    public int apoth_sunderingGetAmplifier(@Nullable MobEffectInstance inst) {
        return inst == null ? -1 : inst.getAmplifier();
    }

    @Shadow
    public abstract boolean hasEffect(MobEffect var1);

    @Shadow
    public abstract MobEffectInstance getEffect(MobEffect var1);

    public int getTeamColor() {
        int color = super.getTeamColor();
        if (color == 16777215) {
            Component name = this.getCustomName();
            if (name != null && name.getStyle().getColor() != null) {
                color = name.getStyle().getColor().getValue();
            }
        }

        return color;
    }

    @Redirect(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(FFF)F"
            ),
            method = {"getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"},
            require = 1
    )
    public float apoth_applyArmorPen(float amount, float armor, float toughness, DamageSource src, float amt2) {
        return ALCombatRules.getDamageAfterArmor((LivingEntity)(Object)this, src, amount, armor, toughness);
    }

    @Redirect(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterMagicAbsorb(FF)F"
            ),
            method = {"getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"},
            require = 1
    )
    public float apoth_applyProtPen(float amount, float protPoints, DamageSource src, float amt2) {
        return ALCombatRules.getDamageAfterProtection((LivingEntity)(Object)this, src, amount, protPoints);
    }
}
