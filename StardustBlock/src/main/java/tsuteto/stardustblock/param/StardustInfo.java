package tsuteto.stardustblock.param;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.EnumSet;
import java.util.List;

public class StardustInfo
{
    public enum Color {
        glowstone(0, 0xeedf8a),
        black(1, 0x000000),
        red(2, 0xf82222),
        green(3, 0x3c9351),
        brown(4, 0x965042),
        blue(5, 0x8080ff),
        purple(6, 0xb872db),
        cyan(7, 0x40e0d0),
        silver(8, 0xb0b0b8),
        gray(9, 0x808080),
        pink(10, 0xff808b),
        lime(11, 0x60ff60),
        yellow(12, 0xdecf2a),
        lightBlue(13, 0xa0d8ef),
        magenta(14, 0xe4007f),
        orange(15, 0xeb8844),
        white(16, 0xffffff),
        pastel(17, new int[]{
                0xff8080, //1.0F, 0.5F, 0.5F,
                0x80ff80, //0.5F, 1.0F, 0.5F,
                0x8080ff, //0.5F, 0.5F, 1.0F,
                0xffff33, //1.0F, 1.0F, 0.2F,
                0x33ffff, //0.2F, 1.0F, 1.0F,
                0xff33ff, //1.0F, 0.2F, 1.0F,
                0xffc033, //1.0F, 0.75F, 0.2F,
                0xc0ff33, //0.75F, 1.0F, 0.2F,
                0x33ffc0, //0.2F, 1.0F, 0.75F,
                0x33c0ff, //0.2F, 0.75F, 1.0F,
                0xc033ff, //0.75F, 0.2F, 1.0F,
                0xff33c0, //1.0F, 0.2F, 0.75F,
        })
        ;

        public final int id;
        public final int[] colors;

        Color(int id, int color)
        {
            this.id = id;
            this.colors = new int[]{color};
        }

        Color(int id, int[] colors)
        {
            this.id = id;
            this.colors = colors;
        }

        public static Color getById(int id)
        {
            for (Color color : Color.values())
            {
                if (color.id == id) return color;
            }
            return null;
        }

    }

    public enum Shape
    {
        star(0), circle(1), diamond(2), heart(3), creeper(4), cross(5), flower(6), ring(7);

        public final int id;

        Shape(int id)
        {
            this.id = id;
        }

        public static Shape getById(int id)
        {
            for (Shape shape : Shape.values())
            {
                if (shape.id == id) return shape;
            }
            return null;
        }
    }

    public static final String PROP_ROOT = "SdBlk";
    public static final String PROP_SHAPE = "Shape";
    public static final String PROP_DENSE = "Dense";
    public static final String PROP_SOLID = "Solid";
    public static final String PROP_COLORS = "Colors";

    public static final StardustInfo DEFAULT
            = new StardustInfo().setDense(2).setShape(Shape.star).setSolid(false);

    public static StardustInfo readFromMetadata(int meta)
    {
        StardustInfo info = new StardustInfo();
        info.shape = Shape.getById(meta);
        info.dense = 2;
        return info;
    }

    public static StardustInfo loadFromItem(ItemStack input)
    {
        if (input.getTagCompound() != null)
        {
            return readFromNBT(input.getTagCompound());
        }
        else
        {
            // Default
            return DEFAULT.copy();
        }
    }

    public static StardustInfo readFromNBT(NBTTagCompound nbtTag)
    {
        return new StardustInfo().loadFromNBTTag(nbtTag);
    }

    public Shape shape = null;
    public Integer dense = null;
    public Boolean isSolid = null;
    public EnumSet<Color> colorList;

    public StardustInfo()
    {
        this.colorList = EnumSet.noneOf(Color.class);
    }

    /*
     * Setters
     */
    public StardustInfo setShape(Shape shape)
    {
        this.shape = shape;
        return this;
    }

    public StardustInfo setDense(int dense)
    {
        this.dense = dense;
        return this;
    }

    public StardustInfo setSolid(boolean bool)
    {
        this.isSolid = bool;
        return this;
    }

    public StardustInfo addColor(Color c)
    {
        this.colorList.add(c);
        return this;
    }

    /*
     * Getters
     */
    public boolean isSolid()
    {
        return this.isSolid != null && this.isSolid;
    }

    public int[] makeColorPalette()
    {
        int[] colorPalette;

        if (this.colorList.isEmpty())
        {
            colorPalette = Color.glowstone.colors;
        }
        else
        {
            List<Integer> temp = Lists.newArrayList();
            for (StardustInfo.Color c : this.colorList)
            {
                temp.addAll(Ints.asList(c.colors));
            }
            colorPalette = Ints.toArray(temp);
        }

        return colorPalette;
    }

    public void writeToNBT(NBTTagCompound nbtTag)
    {
        NBTTagCompound sdTag;
        if (nbtTag.hasKey(PROP_ROOT))
        {
            sdTag = new NBTTagCompound();
        }
        else
        {
            sdTag = nbtTag.getCompoundTag(PROP_ROOT);
        }

        if (shape != null) sdTag.setInteger(PROP_SHAPE, shape.id);
        if (dense != null) sdTag.setInteger(PROP_DENSE, dense);
        if (isSolid != null) sdTag.setBoolean(PROP_SOLID, isSolid);
        if (!colorList.isEmpty())
        {
            int[] intList = new int[colorList.size()];
            int i = 0;
            for (Color c : colorList) intList[i++] = c.id;
            sdTag.setIntArray(PROP_COLORS, intList);
        }

        this.writeToNBTAdditional(sdTag);

        nbtTag.setTag(PROP_ROOT, sdTag);
    }

    public void writeToNBTAdditional(NBTTagCompound stardustTag) {}

    public StardustInfo loadFromNBTTag(NBTTagCompound nbtTag)
    {
        if (!nbtTag.hasKey(PROP_ROOT))
        {
            return this;
        }
        else
        {
            NBTTagCompound sdTag = nbtTag.getCompoundTag(PROP_ROOT);
            if (sdTag.hasKey(PROP_SHAPE)) this.shape = Shape.getById(sdTag.getInteger(PROP_SHAPE));
            if (sdTag.hasKey(PROP_DENSE)) this.dense = sdTag.getInteger(PROP_DENSE);
            if (sdTag.hasKey(PROP_SOLID)) this.isSolid = sdTag.getBoolean(PROP_SOLID);
            if (sdTag.hasKey(PROP_COLORS))
            {
                for (int i : sdTag.getIntArray(PROP_COLORS)) this.colorList.add(Color.getById(i));
            }
        }
        return this;
    }

    public StardustInfo copy()
    {
        StardustInfo newOne = new StardustInfo();
        newOne.shape = this.shape;
        newOne.dense = this.dense;
        newOne.isSolid = this.isSolid;
        if (newOne.colorList != null)
        {
            newOne.colorList = EnumSet.copyOf(this.colorList);
        }
        return newOne;
    }
}
