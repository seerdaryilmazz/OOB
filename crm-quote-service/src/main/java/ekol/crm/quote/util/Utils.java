package ekol.crm.quote.util;

import java.io.*;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.*;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.core.io.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.tool.xml.*;
import com.itextpdf.tool.xml.css.*;
import com.itextpdf.tool.xml.html.*;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.*;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.*;

import ekol.crm.quote.domain.model.quote.SpotQuote;
import ekol.exceptions.ApplicationException;

public class Utils {
	
	private Utils() {
	    throw new IllegalStateException("Utility class");
	  }

    public static <T> T getForObject(boolean ignoreNotFoundException, RestTemplate restTemplate, String url, Class<T> responseType, Object... uriVariables) {

        T result = null;
        
        try {
            result = restTemplate.getForObject(url, responseType, uriVariables);
        } catch(HttpClientErrorException e) {
            if (!ignoreNotFoundException || HttpStatus.NOT_FOUND != e.getStatusCode()) {
                throw e;
            }
        } 
        return result;
    }

    /**
     * Aşağıdaki kodlar şu adresten alınmıştır, sadece birkaç değişiklik yapılmıştır:
     * https://developers.itextpdf.com/examples/xml-worker-itext5/html-images#1512-parsehtml4.java
     */
    public static void convertHtmlToPdf(
            String htmlString, String pdfFilePath, ImageProvider imageProvider, PdfPageEvent pageEvent,
            Rectangle pageSize, float marginLeft, float marginRight, float marginTop, float marginBottom,
            String fontDirectory) throws IOException, DocumentException {

        Document document = new Document(pageSize, marginLeft, marginRight, marginTop, marginBottom);
        try (OutputStream fileStream = new FileOutputStream(pdfFilePath)){
        	PdfWriter writer = PdfWriter.getInstance(document, fileStream);
            writer.setPageEvent(pageEvent);

            document.open();

            InputStream inputStream = new ClassPathResource("pdfStyling.css").getInputStream();
            CSSResolver cssResolver = new StyleAttrCSSResolver();
            CssFile cssFile = XMLWorkerHelper.getCSS(inputStream);
            cssResolver.addCss(cssFile);

            XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider();
            
            /** Ftl dosyası içindeki inline ve Spot quote pdf settings içindeki olası fontları tanımlar. **/
            fontProvider.register(fontDirectory + "/Calibri.ttf", "Calibri");
            fontProvider.register(fontDirectory + "/FuturaMedium.ttf", "Futura Medium");
            fontProvider.register(fontDirectory + "/Arial.ttf", "Arial");
            fontProvider.register(fontDirectory + "/FreeSans.ttf", "Free Sans");
            fontProvider.register(fontDirectory + "/Georgia.ttf", "Georgia");
            fontProvider.register(fontDirectory + "/Impact.ttf", "Impact");
            fontProvider.register(fontDirectory + "/Tahoma.ttf", "Tahoma");
            fontProvider.register(fontDirectory + "/Verdana.ttf", "Verdana");
            fontProvider.register(fontDirectory + "/TimesNewRoman.ttf", "Times New Roman");
            fontProvider.setUseUnicode(true);

            CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
            htmlContext.setImageProvider(imageProvider);

            PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
            HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
            CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

            XMLWorker worker = new XMLWorker(css, true);
            XMLParser parser = new XMLParser(worker);
            String xhtmlString = convertHtmlToXhtml(htmlString);

            try(InputStream stream = new ByteArrayInputStream(xhtmlString.getBytes(StandardCharsets.UTF_8))){
            	parser.parse(stream);
            }
            
            if (document.isOpen()) {
                document.close();
            }
		}
    }

    public static String convertHtmlToXhtml(String htmlString) {
        final org.jsoup.nodes.Document document = Jsoup.parse(htmlString);
        document.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
        return document.html();
    }

    public static float convertMillimeterToPdfUnit(float mm) {
        return ((mm / 10) / 2.54f) * 72;
    }

    /**
     * Performans sorununa yol açmayacak küçük dosyalar için kullanıyoruz.
     */
    public static byte[] readWholeClasspathFileToByteArray(String filePath) throws IOException {
        byte[] bytes;
        Resource resource = new ClassPathResource(filePath);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            bytes = IOUtils.toByteArray(inputStream);
            inputStream.close();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return bytes;
    }

    public static String generatePdfFileName(SpotQuote quote) {
        return "spotQuote" + quote.getNumber() + ".pdf";
    }

    public static void downloadFile(String filePath, boolean isFileInClassPath, HttpServletResponse response) {

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {

            String fileName = FilenameUtils.getName(filePath);
            String contentType = URLConnection.guessContentTypeFromName(fileName);

            if (StringUtils.isNotEmpty(contentType)) {
                response.setContentType(contentType);
            } else {
                response.setContentType("application/octet-stream");
            }

            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));

            Resource resource;

            if (isFileInClassPath) {
                resource = new ClassPathResource(filePath);
            } else {
                resource = new FileSystemResource(filePath);
            }

            inputStream = new BufferedInputStream(resource.getInputStream());
            outputStream = response.getOutputStream();

            byte[] buffer = new byte[8192];
            int numberOfBytesRead;

            while ((numberOfBytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, numberOfBytesRead);
            }

            outputStream.close();
            inputStream.close();

        } catch (Exception e) {
            throw new ApplicationException("Could not download file", e);
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
        }
    }

    // TODO: LocalDateDeserializer'ın içinde public static bir deserialize metodu olmalı ve
    // manual deserialize işlemlerinde bu metod kullanılmalı.
    public static LocalDate deserializeLocalDateStr(String str) {
        if (StringUtils.isNotBlank(str)) {
            return LocalDate.parse(str, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else {
            return null;
        }
    }

    @SafeVarargs
    public static <E> Set<E> hashSet(E... args) {
    	return new HashSet<>(Arrays.asList(args));
    }
}
