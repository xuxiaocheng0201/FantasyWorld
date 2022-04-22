package HeadLibs.Helper;

@SuppressWarnings("MagicNumber")
public class HColorHelper {
    /**
     * Makes an integer color from the given red, green, and blue float values
     */
    public static int rgb(float r, float g, float b) {
        return rgb(HMathHelper.floor(r * 255.0F), HMathHelper.floor(g * 255.0F), HMathHelper.floor(b * 255.0F));
    }

    /**
     * Makes a single int color with the given red, green, and blue values.
     */
    public static int rgb(int r, int g, int b) {
        int rgb = (r << 8) + g;
        rgb = (rgb << 8) + b;
        return rgb;
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    public static int multiplyColor(int color1, int color2) {
        int i = (color1 & 16711680) >> 16;
        int j = (color2 & 16711680) >> 16;
        int k = (color1 & 65280) >> 8;
        int l = (color2 & 65280) >> 8;
        int i1 = (color1 & 255);
        int j1 = (color2 & 255);
        int k1 = (int)(i * j / 255.0F);
        int l1 = (int)(k * l / 255.0F);
        int i2 = (int)(i1 * j1 / 255.0F);
        return color1 & -16777216 | k1 << 16 | l1 << 8 | i2;
    }
}
