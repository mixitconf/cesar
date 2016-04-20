package org.mixit.cesar.site.service.qrcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.QRCode;
import org.assertj.core.api.StrictAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.mixit.cesar.site.model.qrcode.Point;
import org.mixit.cesar.site.model.qrcode.SvgPath;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 20/04/16.
 */
public class SvgConverterTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private SvgConverter svgConverter;

    /**
     * Test {@link SvgConverter#parseQRCodeMatrix(com.google.zxing.qrcode.encoder.QRCode)}
     * Case
     * <pre>
     *     null
     * </pre>
     */
    @Test(expected = NullPointerException.class)
    public void should_throw_npe_when_parse_null_matrix() {
        svgConverter.parseQRCodeMatrix(null);
    }

    /**
     * Test {@link SvgConverter#parseQRCodeMatrix(com.google.zxing.qrcode.encoder.QRCode)}
     * Case plain line
     * <pre>
     *     0 0 0 0
     *     0 0 0 0
     * </pre>
     */
    @Test
    public void should_return_no_polygon_when_parse_matrix_with_empty_line() {
        QRCode qrCode = new QRCode();
        qrCode.setMatrix(new ByteMatrix(4, 2));
        assertThat(svgConverter.parseQRCodeMatrix(qrCode)).isEmpty();
    }

    /**
     * Test {@link SvgConverter#parseQRCodeMatrix(com.google.zxing.qrcode.encoder.QRCode)}
     * Case plain line
     * <pre>
     *     1 1 1 1
     *     1 1 1 1
     * </pre>
     */
    @Test
    public void should_return_two_polygons_when_parse_matrix_with_full_line() {
        ByteMatrix byteMatrix = new ByteMatrix(4, 2);
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 2; y++) {
                byteMatrix.set(x, y, true);
            }
        }
        QRCode qrCode = new QRCode();
        qrCode.setMatrix(byteMatrix);

        List<SvgPath> rectangles = svgConverter.parseQRCodeMatrix(qrCode);

        assertThat(rectangles).hasSize(2);
        StrictAssertions.assertThat(rectangles.get(0)).isEqualToComparingFieldByField(SvgPath.create(Point.create(0, 0), 4));
        StrictAssertions.assertThat(rectangles.get(1)).isEqualToComparingFieldByField(SvgPath.create(Point.create(0, 1), 4));
    }

    /**
     * Test {@link SvgConverter#parseQRCodeMatrix(com.google.zxing.qrcode.encoder.QRCode)}
     * Case matrix with a blank in a square
     * <pre>
     *     1 1 1 1
     *     1 0 0 1
     *     1 1 1 1
     *     0 1 1 0
     * </pre>
     */
    @Test
    public void should_return_several_polygons_when_parse_complex_matrix() {
        ByteMatrix byteMatrix = new ByteMatrix(4, 4);
        for (int x = 0; x < 4; x++) {
            byteMatrix.set(x, 0, true);
            byteMatrix.set(x, 2, true);
        }
        byteMatrix.set(0, 1, true);
        byteMatrix.set(3, 1, true);
        byteMatrix.set(1, 3, true);
        byteMatrix.set(2, 3, true);
        QRCode qrCode = new QRCode();
        qrCode.setMatrix(byteMatrix);

        List<SvgPath> rectangles = svgConverter.parseQRCodeMatrix(qrCode);

        assertThat(rectangles).hasSize(5);
        StrictAssertions.assertThat(rectangles.get(0)).isEqualToComparingFieldByField(SvgPath.create(Point.create(0, 0), 4));
        StrictAssertions.assertThat(rectangles.get(1)).isEqualToComparingFieldByField(SvgPath.create(Point.create(0, 1), 1));
        StrictAssertions.assertThat(rectangles.get(2)).isEqualToComparingFieldByField(SvgPath.create(Point.create(3, 1), 1));
        StrictAssertions.assertThat(rectangles.get(3)).isEqualToComparingFieldByField(SvgPath.create(Point.create(0, 2), 4));
        StrictAssertions.assertThat(rectangles.get(4)).isEqualToComparingFieldByField(SvgPath.create(Point.create(1, 3), 2));
    }

    /**
     * Test {@link SvgConverter#generateSvg(QRCode, String, String)}
     */
    @Test(expected = NullPointerException.class)
    public void should_not_generate_svg_when_qrcode_is_nul() {
        svgConverter.generateSvg(null, "black", "content");
    }

    /**
     * Test {@link SvgConverter#generateSvg(QRCode, String, String)}
     */
    @Test
    public void should_generate_svg() {
        QRCode qrCode = new QRCode();
        qrCode.setMatrix(new ByteMatrix(4, 2));
        StrictAssertions.assertThat(svgConverter.generateSvg(qrCode, "black", svgConverter.generatePathSvg(qrCode))).isNotEmpty();
    }
}