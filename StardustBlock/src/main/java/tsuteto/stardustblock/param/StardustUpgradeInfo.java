package tsuteto.stardustblock.param;

import net.minecraft.nbt.NBTTagCompound;

public class StardustUpgradeInfo extends StardustInfo
{
    public boolean isToClearColor = false;

    public StardustInfo setToClearColor()
    {
        this.isToClearColor = true;
        return this;
    }

    public boolean upgradeNBT(NBTTagCompound nbt)
    {
        StardustInfo target = readFromNBT(nbt);

        if (shape != null) target.shape = this.shape;
        if (dense != null)
        {
            target.dense = target.dense + this.dense;
            if (target.dense < 0 || target.dense > 6) return false;
        }
        if (isSolid != null) target.isSolid = this.isSolid;
        if (colorList.size() > 0)
        {
            target.colorList.addAll(this.colorList);
            if (target.colorList.size() > 5) return false;
        }

        target.writeToNBT(nbt);

        // Remove color
        NBTTagCompound sdTag = nbt.getCompoundTag(PROP_ROOT);
        if (isToClearColor && sdTag.hasKey(PROP_COLORS))
        {
            sdTag.removeTag(PROP_COLORS);
        }


        return true;
    }
}
