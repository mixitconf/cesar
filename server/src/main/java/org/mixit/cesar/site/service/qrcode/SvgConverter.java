package org.mixit.cesar.site.service.qrcode;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.Lists;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.encoder.QRCode;
import org.mixit.cesar.site.model.qrcode.Point;
import org.mixit.cesar.site.model.qrcode.SvgPath;
import org.springframework.stereotype.Service;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 07/12/15.
 */
@Service
public class SvgConverter {

    /**
     * When we build a QRCode a matrix is generated with all points. We use the method {@link BitMatrix#get(int, int)} which return true if a pixel is painted.
     * The goal is to build a svg image from this matrix. Each line is analysed to know one by one. We don't use a complex search because a square can contain
     * a blank area<br>
     * Example with a blank in a square<br>
     * <pre>
     *     1 1 1
     *     1 0 1
     *     1 1 1
     * </pre>
     */
    public List<SvgPath> parseQRCodeMatrix(QRCode qrcode) {
        Objects.requireNonNull(qrcode);
        List<SvgPath> paths = Lists.newArrayList();

        //We read line by line
        for (int y = 0; y < qrcode.getMatrix().getHeight(); y++) {

            //We read the point from the left to the right
            for (int x = 0; x < qrcode.getMatrix().getWidth(); x++) {

                Point startedPoint = Point.create(x, y);

                if (qrcode.getMatrix().get(x, y)==1) {
                    //If we are on the last cell we create a path
                    if(x + 1 == qrcode.getMatrix().getWidth()){
                        paths.add(SvgPath.create(startedPoint, 1));
                    }
                    //We search the next point to the right
                    for (x = x + 1; x < qrcode.getMatrix().getWidth(); x++) {
                        //If the next point is not used or if we are on the last column
                        if (qrcode.getMatrix().get(x, y)==0) {
                            paths.add(SvgPath.create(startedPoint, x - startedPoint.x()));
                            break;
                        }
                        else if(x + 1 == qrcode.getMatrix().getWidth()){
                            paths.add(SvgPath.create(startedPoint, x - startedPoint.x() + 1));
                        }
                    }
                }
            }
        }

        return paths;
    }


    /**
     * Generate the path use to create  SVG file
     */
    public String generatePathSvg(QRCode qrCode) {
        StringBuilder svgContent = new StringBuilder();
        parseQRCodeMatrix(qrCode).stream().forEach(path -> svgContent.append(path.generate()));
        return svgContent.toString();
    }

    /**
     * Transforms a QRCode to a SVG image. The size of the image depends on the version of the QR code and the version depend on the size of
     * the encoded bytes.
     *
     * @param color (black by default if this arg is null)
     * @see <a href="https://en.wikipedia.org/wiki/QR_code#Storage">QRCode Version</a>
     */
    public String generateSvg(QRCode qrCode, String color, String content) {
        Objects.requireNonNull(qrCode);

        return String.format(
                "<svg viewBox=\"0 0 %d %d\" xmlns=\"http://www.w3.org/2000/svg\">" +
                        "<path stroke=\"%s\" d=\"%s\"/>" +
                        "</svg>",
                qrCode.getMatrix().getWidth(),
                qrCode.getMatrix().getHeight(),
                color==null ? "black" : color,
                content);
    }

}
