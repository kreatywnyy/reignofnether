package com.solegendary.reignofnether.unit.goals;

import com.solegendary.reignofnether.building.GarrisonableBuilding;
import com.solegendary.reignofnether.unit.interfaces.AttackerUnit;
import com.solegendary.reignofnether.unit.interfaces.RangedAttackerUnit;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.TridentItem;

import java.util.EnumSet;

// modified version of RangedBowAttackGoal which:
// - doesn't strafe
// - stops when the target is dead
// - does not move towards a target if already in attack range and can see the target
// - faces the target
// - does not require the user to wind up bow attacks, instead using RTS-like attack cooldowns

// can set a flag to use for tridents instead of bows

public class UnitBowAttackGoal<T extends net.minecraft.world.entity.Mob> extends Goal {
    private final T mob;
    private final int attackWindupTime = 5; // time to wind up a bow attack
    private int attackCooldownMax;
    private int attackCooldown = 0; // time to wait between bow windups
    private int attackTime = -1;
    private int seeTime; // how long we have seen the target for

    public UnitBowAttackGoal(T mob, int attackCooldown) {
        this.mob = mob;
        this.attackCooldownMax = attackCooldown;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public void tickCooldown() {
        if (this.attackCooldown > 0)
            this.attackCooldown -= 1;
    }

    public boolean canUse() { return this.mob.getTarget() != null && this.isHoldingRangedWeapon(); }

    private boolean isHoldingBow() {
        return this.mob.isHolding(is -> is.getItem() instanceof BowItem);
    }

    private boolean isHoldingTrident() {
        return this.mob.isHolding(is -> is.getItem() instanceof TridentItem);
    }

    protected boolean isHoldingRangedWeapon() {
        return isHoldingBow() || isHoldingTrident();
    }

    public boolean canContinueToUse() {
        Entity target = this.mob.getTarget();

        if (target == null || !target.isAlive() || !this.isHoldingRangedWeapon())
            return false;
        if (!this.canUse() && this.mob.getNavigation().isDone())
            return false;

        return true;
    }

    public void start() {
        super.start();
        this.mob.setAggressive(true);
    }

    public void stop() {
        super.stop();
        this.mob.setAggressive(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.mob.stopUsingItem();
    }

    public void tick() {
        LivingEntity target = this.mob.getTarget();

        if (target != null && target.isAlive()) {
            this.mob.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());

            GarrisonableBuilding garr = GarrisonableBuilding.getGarrison((Unit) this.mob);
            GarrisonableBuilding targetGarr = null;
            if (target instanceof Unit unit)
                targetGarr = GarrisonableBuilding.getGarrison(unit);

            boolean isGarrisoned = garr != null;
            boolean isTargetGarrisoned = targetGarr != null;

            boolean canSeeTarget = this.mob.getSensing().hasLineOfSight(target) || isGarrisoned || isTargetGarrisoned;
            boolean flag = this.seeTime > 0;
            if (canSeeTarget != flag) {
                this.seeTime = 0;
            }
            if (canSeeTarget) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            float attackRange = ((AttackerUnit) this.mob).getAttackRange();

            if (isGarrisoned)
                attackRange += garr.getAttackRangeBonus();
            else if (isTargetGarrisoned)
                attackRange += targetGarr.getExternalAttackRangeBonus();

            double distToTarget = this.mob.distanceTo(target);

            // move towards the target until in range and target is visible
            // don't if the attacker is riding (eg. skeleton jockey) or it influences the vehicle movement
            // move to slightly closer than range so we can still chase and attack a moving target of the same speed
            if (!this.mob.isPassenger()) {
                if ((distToTarget > attackRange - 1 || !canSeeTarget) &&
                    !((Unit) this.mob).getHoldPosition()) {
                    this.mob.getNavigation().moveTo(target, 1.0f);
                } else {
                    this.mob.getNavigation().stop();
                }
            }

            if (this.mob.isUsingItem()) {
                if (distToTarget > attackRange || (!canSeeTarget && this.seeTime < -60)) {
                    this.mob.stopUsingItem();
                }
                else if (distToTarget <= attackRange && canSeeTarget) { // start drawing bowstring
                    int i = this.mob.getTicksUsingItem();
                    if (i >= attackWindupTime && attackCooldown <= 0) {
                        this.mob.stopUsingItem();

                        float velocity = 0;
                        if (isHoldingBow())
                            velocity = BowItem.getPowerForTime(i);
                        else if (isHoldingTrident())
                            velocity = 20;

                        if (mob instanceof RangedAttackerUnit rangedAttackerUnit)
                            rangedAttackerUnit.performUnitRangedAttack(target, velocity);

                        this.attackTime = this.attackWindupTime;
                        this.attackCooldown = this.attackCooldownMax;
                    }
                }
            } else if (distToTarget <= attackRange && --this.attackTime <= 0 && this.seeTime >= -60) {
                this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof BowItem || item instanceof TridentItem));
            }
        }
    }
}
