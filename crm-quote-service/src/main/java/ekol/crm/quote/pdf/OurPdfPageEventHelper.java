package ekol.crm.quote.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import ekol.crm.quote.util.Utils;
import ekol.exceptions.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Aşağıdaki kodlar şu adresten alınmıştır, sadece birkaç değişiklik yapılmıştır:
 * https://memorynotfound.com/adding-header-footer-pdf-using-itext-java/
 */
public class OurPdfPageEventHelper extends PdfPageEventHelper {

    public static final byte[] BYTE_ARRAY_OF_BANNER = getByteArrayOfImage("ekol_banner.png");

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OurPdfPageEventHelper.class);

    private String fontDirectory;
    private PdfTemplate t;
    private Image total;
    private String languageCode;

    public OurPdfPageEventHelper(String fontDirectory, String languageCode) {
        this.fontDirectory = fontDirectory;
        this.languageCode = languageCode;
    }

    private static byte[] getByteArrayOfImage(String imageName) {
        try {
            return Utils.readWholeClasspathFileToByteArray("spotQuotePdfResources/img/" + imageName);
        } catch (IOException e) {
            throw new ApplicationException("Could not read image. image name: {0}", imageName);
        }
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        t = writer.getDirectContent().createTemplate(30, 16);
        try {
            total = Image.getInstance(t);
            total.setRole(PdfName.ARTIFACT);
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        writer.getPageSize().setBackgroundColor(new BaseColor(235, 235, 235));
        if(document.getPageNumber()==1){
            addImageBasedOnLanguage(writer, languageCode);
        }else {
            addHeader(writer);
            addFooter(writer);
        }
    }

    private void addImageBasedOnLanguage(PdfWriter writer, String languageCode) {
        String lang = StringUtils.split(languageCode, "_")[0];
        byte[] img = null;
        try {
            img = getByteArrayOfImage("info-"+lang+".png");
        } catch (ApplicationException e) {
            img = getByteArrayOfImage("info-en.png");
            LOGGER.warn("Pdf cover image 'info-{}.png' is failed to read. English cover image is replaced instead.", lang);
        }

        try {
            Image image = Image.getInstance(img);
            image.scaleAbsolute(PageSize.A4);
            image.setAbsolutePosition(0, 0);
            writer.getDirectContentUnder().addImage(image);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    private void addHeader(PdfWriter writer) {
        try {
            Image image = Image.getInstance(BYTE_ARRAY_OF_BANNER);
            image.scalePercent(50);
            image.setAbsolutePosition(Utils.convertMillimeterToPdfUnit(0), Utils.convertMillimeterToPdfUnit(268));
            writer.getDirectContent().addImage(image);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    private void addFooter(PdfWriter writer){
        PdfPTable footer = new PdfPTable(3);
        try {

            Font font = getFooterFont();
            font.setSize(8);

            // set defaults
            footer.setWidths(new int[]{24, 2, 1});
            footer.setTotalWidth(527);
            footer.setLockedWidth(true);
            footer.getDefaultCell().setFixedHeight(40);
            footer.getDefaultCell().setBorder(Rectangle.TOP);
            footer.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

            // add copyright
            footer.addCell(new Phrase("www.ekol.com", font));

            // add current page count
            footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            footer.addCell(new Phrase(String.format("%d /", writer.getPageNumber()), font));

            // add placeholder for total page count
            PdfPCell totalPageCount = new PdfPCell(total);
            totalPageCount.setBorder(Rectangle.TOP);
            totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY);
            footer.addCell(totalPageCount);

            // write page
            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            footer.writeSelectedRows(0, -1, 34, 50, canvas);
            canvas.endMarkedContentSequence();
        } catch(Exception de) {
            throw new ExceptionConverter(de);
        }
    }

    public void onCloseDocument(PdfWriter writer, Document document) {

        int totalLength = String.valueOf(writer.getPageNumber()).length();
        int totalWidth = totalLength * 5;

        Font font = getFooterFont();
        font.setSize(8);

        ColumnText.showTextAligned(t, Element.ALIGN_RIGHT,
                new Phrase(String.valueOf(writer.getPageNumber()), font),
                totalWidth, 6, 0);
    }

    private Font getFooterFont() {
        String fontPath = fontDirectory + "/FreeSans.ttf";
        return FontFactory.getFont(fontPath, "UTF-8", true);
    }
}
