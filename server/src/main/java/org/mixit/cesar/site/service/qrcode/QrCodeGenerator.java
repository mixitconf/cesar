package org.mixit.cesar.site.service.qrcode;

import java.util.Hashtable;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import org.mixit.cesar.site.model.qrcode.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Generate a SVG
 *
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 07/12/15.
 */
@Service
public class QrCodeGenerator {

    private static final Logger logger = LoggerFactory.getLogger(QrCodeGenerator.class);

    @Autowired
    private SvgConverter svgConverter;

    /**
     * Generates a QRCode from an URL. We generate a QRCode which an error correction capability available at 30%
     *
     * @see <a href="https://en.wikipedia.org/wiki/QR_code#Error_correction">QRCode Error levels</a>
     */
    public String generateQRCode(String url, ErrorCorrectionLevel errorCorrectionLevel) {
        Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel == null ? ErrorCorrectionLevel.H : errorCorrectionLevel);
        hintMap.put(EncodeHintType.MARGIN, 0);

        try {
            //We need to know informations on QRCode, so we generate a first QRCode like QRCodeWriter will do that
            QRCode qrCode = Encoder.encode(url, errorCorrectionLevel == null ? ErrorCorrectionLevel.H : errorCorrectionLevel, hintMap);
            StringBuilder svgContent = new StringBuilder();
            svgConverter.parseQRCodeMatrix(qrCode).stream().forEach(path -> svgContent.append(path.generate()));
            return svgContent.toString();

        }
        catch (WriterException e) {
            logger.error("Error on QrCode generation for url=" + url, e);
            throw new QrCodeGeneratorException();
        }
    }

}
