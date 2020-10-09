package ekol.crm.quote.pdf;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;

/**
 * Aşağıdaki kodlar şu adresten alınmıştır, sadece birkaç değişiklik yapılmıştır:
 * https://developers.itextpdf.com/examples/xml-worker-itext5/html-images#1512-parsehtml4.java
 */
public class OurImageProvider extends AbstractImageProvider {

    /**
     * Aşağıdaki kodun dikkat edilmesi gereken yeri, Base64-encoded image'lerden com.itextpdf.text.Image nesneleri oluşturduğumuz yerdir.
     * Yani HTML'den PDF'e çevrim yaparken, verilen HTML içinde aşağıdaki gibi Base64-encoded image'ler varsa bunların nasıl çevrileceğini belirtiyoruz.
     * {@code <img src="data:image/png;base64,..." alt="..." width="..." height="..."></img>}
     */
    @Override
    public Image retrieve(String src) {
        int pos = src.indexOf("base64,");
        try {
            if (src.startsWith("data") && pos > 0) {
                byte[] img = Base64.decodeBase64(src.substring(pos + 7));
                return Image.getInstance(img);
            } else {
                Image image = Image.getInstance(src);
                return image;
            }
        } catch (BadElementException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public String getImageRootPath() {
        return null;
    }
}
