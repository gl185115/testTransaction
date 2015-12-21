package ncr.res.mobilepos.simpleprinterdriver;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class BmpColorPallet {
    private List<BmpRgbQuad> colorPallet = new ArrayList<BmpRgbQuad>();

    /**
     * Constructor for Windows Bitmap(Bitmap INFO Header).
     * 
     * @param colorPalletBytes
     */
    public BmpColorPallet(byte[] colorPalletBytes) {
        for (int i = 0; i < colorPalletBytes.length - 1; i += 4) {
            colorPallet.add(new BmpRgbQuad(new byte[] { colorPalletBytes[i],
                    colorPalletBytes[i + 1], colorPalletBytes[i + 2],
                    colorPalletBytes[i + 3]}));
        }
    }

    public Color getMonoBmpIndex1() {
        BmpRgbQuad rgbQuad = colorPallet.get(0);
        Color color = new Color((int)(rgbQuad.getRgbRed() & 0xFF),
                                (int)(rgbQuad.getRgbGreen() & 0xFF),
                                (int)(rgbQuad.getRgbBlue() & 0xFF));
        return color;
    }

    public Color getMonoBmpIndex2() {
        BmpRgbQuad rgbQuad = colorPallet.get(1);
        Color color = new Color((int)(rgbQuad.getRgbRed() & 0xFF),
                                (int)(rgbQuad.getRgbGreen() & 0xFF),
                                (int)(rgbQuad.getRgbBlue() & 0xFF));
        return color;
    }

    private class BmpRgbQuad {
        private byte rgbBlue;
        private byte rgbGreen;
        private byte rgbRed;
        private byte rgbReserved;

        public BmpRgbQuad(byte[] rgbQuad) {
            this.setRgbBlue(rgbQuad[0]);
            this.setRgbGreen(rgbQuad[1]);
            this.setRgbRed(rgbQuad[2]);
            this.setRgbReserved(rgbQuad[3]);
        }

        public byte getRgbBlue() {
            return rgbBlue;
        }

        private void setRgbBlue(byte rgbBlue) {
            this.rgbBlue = rgbBlue;
        }

        public byte getRgbGreen() {
            return rgbGreen;
        }

        private void setRgbGreen(byte rgbGreen) {
            this.rgbGreen = rgbGreen;
        }

        public byte getRgbRed() {
            return rgbRed;
        }

        private void setRgbRed(byte rgbRed) {
            this.rgbRed = rgbRed;
        }

        public byte getRgbReserved() {
            return rgbReserved;
        }

        private void setRgbReserved(byte rgbReserved) {
            this.rgbReserved = rgbReserved;
        }
    }
}
