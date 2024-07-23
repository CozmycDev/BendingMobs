package net.doodcraft.cozmyc.bendingmobs.object;

import org.bukkit.entity.LivingEntity;

public class BendingEntity {

    private LivingEntity entity;
    private LivingEntity target;

    public BendingEntity(LivingEntity entity, LivingEntity target) {
        this.entity = entity;
        this.target = target;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public void setEntity(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }
}
