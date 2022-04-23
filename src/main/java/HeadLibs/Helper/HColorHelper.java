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

    @SuppressWarnings("NumericCastThatLosesPrecision")
    public static int hsvToRGB(float hue, float saturation, float value) {
        int i = (int)(hue * 6.0F) % 6;
        float f = hue * 6.0F - i;
        float f1 = value * (1.0F - saturation);
        float f2 = value * (1.0F - f * saturation);
        float f3 = value * (1.0F - (1.0F - f) * saturation);
        float f4;
        float f5;
        float f6;
        switch (i) {
            case 0 -> {
                f4 = value;
                f5 = f3;
                f6 = f1;
            }
            case 1 -> {
                f4 = f2;
                f5 = value;
                f6 = f1;
            }
            case 2 -> {
                f4 = f1;
                f5 = value;
                f6 = f3;
            }
            case 3 -> {
                f4 = f1;
                f5 = f2;
                f6 = value;
            }
            case 4 -> {
                f4 = f3;
                f5 = f1;
                f6 = value;
            }
            case 5 -> {
                f4 = value;
                f5 = f1;
                f6 = f2;
            }
            default -> throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }
        int j = HMathHelper.clamp((int)(f4 * 255.0F), 0, 255);
        int k = HMathHelper.clamp((int)(f5 * 255.0F), 0, 255);
        int l = HMathHelper.clamp((int)(f6 * 255.0F), 0, 255);
        return j << 16 | k << 8 | l;
    }
}
