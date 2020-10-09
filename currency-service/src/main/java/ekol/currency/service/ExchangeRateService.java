package ekol.currency.service;

import ekol.currency.config.JavaMailSenderConfig;
import ekol.currency.domain.ExchangeRate;
import ekol.currency.domain.ExchangeRatePublisher;
import ekol.currency.domain.PreferredValue;
import ekol.currency.domain.dto.ExchangeRateForUI;
import ekol.currency.domain.dto.MissingDay;
import ekol.currency.domain.dto.PagedDataHolder;
import ekol.currency.repository.ExchangeRateRepository;
import ekol.currency.util.Constants;
import ekol.currency.util.Util;
import ekol.currency.webserviceclient.exchangerate.hungary.MNBArfolyamServiceSoapImplStub;
import ekol.exceptions.ApplicationException;
import ekol.exceptions.BadRequestException;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  ekol.currency.webserviceclient.exchangerate.hungary paketindeki web service client kodlarını şöyle oluşturduk:
 * - axis2-xxx.zip'i (örneğin axis2-1.7.6.zip'i) bilgisayaramıza indirip bir dizine açtık.
 * - Komut satırını açıp axis2-xxx/bin dizinine gittik.
 * - Bu komutu çalıştırdık: ./wsdl2java.sh -uri http://www.mnb.hu/arfolyamok.asmx?wsdl -o <output-directory> -p <package-name> -d adb
 * - output-directory: Java dosyaları bu dizinde oluşturulacak
 * - package-name: Java dosyalarında bu paket ismi yazacak
 * - Oluşan Java dosyalarından ismi ...Stub.java olan, web service metodlarını çağırmak için kullanacağımız dosyadır.
 * Not: Web service client kodlarını ilk önce apache-cxf-3.2.0 ile oluşturduk ancak oluşan kodlar ile parametre alan metodları çağıramıyorduk.
 */
@Service
public class ExchangeRateService {

    @Value("${exchangeRateWebService.hungary.url}")
    private String exchangeRateWebService_hungary_url;

    @Value("${exchangeRateWebService.poland.url}")
    private String exchangeRateWebService_poland_url;

    @Value("${exchangeRateWebService.turkey.url}")
    private String exchangeRateWebService_turkey_url;

    @Value("${exchangeRateWebService.europe.url}")
    private String exchangeRateWebService_europe_url;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private JavaMailSender defaultMailSender;

    @Autowired
    private JavaMailSenderConfig javaMailSenderConfig;

    @Autowired
    private Environment environment;

    public List<ExchangeRate> parseExchangeRatesXmlForHungary(String xml) {

        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try {

            Document xmlDocument = Util.getDocumentFromXml(xml);
            XPath xPath = XPathFactory.newInstance().newXPath();

            Node nodeThatContainsPublishDate = (Node) xPath.compile("/MNBCurrentExchangeRates/Day|/MNBExchangeRates/Day").evaluate(xmlDocument, XPathConstants.NODE);

            // Kendi verdiğimiz tarihe göre sorgulama yaptığımızda dönen xml bu: <MNBExchangeRates />
            // Bu durumda nodeThatContainsPublishDate null oluyor.
            if (nodeThatContainsPublishDate != null) {

                String publishDateAsStr = nodeThatContainsPublishDate.getAttributes().getNamedItem("date").getNodeValue();
                LocalDate publishDate = LocalDate.parse(publishDateAsStr, DateTimeFormatter.ISO_LOCAL_DATE);

                NodeList nodeList = (NodeList) xPath.compile("/MNBCurrentExchangeRates/Day/Rate|/MNBExchangeRates/Day/Rate").evaluate(xmlDocument, XPathConstants.NODESET);

                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);

                    String unit = node.getAttributes().getNamedItem("unit").getNodeValue();
                    String currencyCode = node.getAttributes().getNamedItem("curr").getNodeValue();
                    String rate = node.getTextContent();

                    ExchangeRate exchangeRate = new ExchangeRate();
                    exchangeRate.setPublisher(ExchangeRatePublisher.CENTRAL_BANK_OF_HUNGARY);
                    exchangeRate.setPublishDate(publishDate);
                    exchangeRate.setUnit(Integer.parseInt(unit));
                    exchangeRate.setFromCurrency(currencyCode);
                    exchangeRate.setToCurrency("HUF");
                    exchangeRate.setValue(new BigDecimal(rate.replace(',', '.'))); // Ondalık ayracını hesaba katıyoruz...
                    exchangeRates.add(exchangeRate);
                }
            }

        } catch (Exception e) {
            throw new ApplicationException("Exception while exchange rate parse", e);
        }

        return exchangeRates;
    }

    public List<ExchangeRate> getLastPublishedExchangeRatesForHungary() {

        String xml;

        try {

            String urlWithoutWsdl = StringUtils.removeEnd(exchangeRateWebService_hungary_url, "?wsdl");
            MNBArfolyamServiceSoapImplStub stub = new MNBArfolyamServiceSoapImplStub(urlWithoutWsdl);

            org.apache.axis2.client.Options options = stub._getServiceClient().getOptions();
            options.setProperty(HTTPConstants.CONNECTION_TIMEOUT, new Integer(Constants.DEFAULT_CONNECT_TIMEOUT));
            options.setProperty(HTTPConstants.SO_TIMEOUT, new Integer(Constants.DEFAULT_READ_TIMEOUT));

            MNBArfolyamServiceSoapImplStub.GetCurrentExchangeRates getCurrentExchangeRates = new MNBArfolyamServiceSoapImplStub.GetCurrentExchangeRates();
            MNBArfolyamServiceSoapImplStub.GetCurrentExchangeRatesResponse getCurrentExchangeRatesResponse = stub.getCurrentExchangeRates(getCurrentExchangeRates);
            xml = getCurrentExchangeRatesResponse.getGetCurrentExchangeRatesResponse().getGetCurrentExchangeRatesResult();

        } catch (Exception e) {
            throw new ApplicationException("Exception while exchange rate query", e);
        }

        return parseExchangeRatesXmlForHungary(xml);
    }

    public List<ExchangeRate> getExchangeRatesForHungary(LocalDate publishDate) {

        String xml;

        try {

            String urlWithoutWsdl = StringUtils.removeEnd(exchangeRateWebService_hungary_url, "?wsdl");
            MNBArfolyamServiceSoapImplStub stub = new MNBArfolyamServiceSoapImplStub(urlWithoutWsdl);

            String publishDateAsFormattedStr = DateTimeFormatter.ISO_LOCAL_DATE.format(publishDate);

            MNBArfolyamServiceSoapImplStub.GetExchangeRatesRequestBody getExchangeRatesRequestBody = new MNBArfolyamServiceSoapImplStub.GetExchangeRatesRequestBody();
            getExchangeRatesRequestBody.setStartDate(publishDateAsFormattedStr);
            getExchangeRatesRequestBody.setEndDate(publishDateAsFormattedStr);
            // "Tümü" gibi bir seçenek bulamadığımızdan, listeyi web service'teki getCurrentExchangeRates metodundan gelen para birimleri ile kendimiz oluşturduk.
            getExchangeRatesRequestBody.setCurrencyNames(StringUtils.join(Constants.ALL_CURRENCIES_FOR_CENTRAL_BANK_OF_HUNGARY, ","));

            MNBArfolyamServiceSoapImplStub.GetExchangeRates getExchangeRates = new MNBArfolyamServiceSoapImplStub.GetExchangeRates();
            getExchangeRates.setGetExchangeRates(getExchangeRatesRequestBody);

            MNBArfolyamServiceSoapImplStub.GetExchangeRatesResponse getExchangeRatesResponse = stub.getExchangeRates(getExchangeRates);
            xml = getExchangeRatesResponse.getGetExchangeRatesResponse().getGetExchangeRatesResult();

        } catch (Exception e) {
            throw new ApplicationException("Exception while exchange rate query", e);
        }

        return parseExchangeRatesXmlForHungary(xml);
    }

    public List<ExchangeRate> parseExchangeRatesXmlForPoland(String xml) {

        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try {

            Document xmlDocument = Util.getDocumentFromXml(xml);
            XPath xPath = XPathFactory.newInstance().newXPath();

            Node nodeThatContainsPublishDate = (Node) xPath.compile("/tabela_kursow/data_publikacji").evaluate(xmlDocument, XPathConstants.NODE);
            String publishDateAsStr = nodeThatContainsPublishDate.getTextContent();
            LocalDate publishDate = LocalDate.parse(publishDateAsStr, DateTimeFormatter.ISO_LOCAL_DATE);

            NodeList nodeList = (NodeList) xPath.compile("/tabela_kursow/pozycja").evaluate(xmlDocument, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node node = nodeList.item(i);

                String unit = null;
                String currencyCode = null;
                String rate = null;

                for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                    Node childNode = node.getChildNodes().item(j);
                    if (childNode.getNodeName().equals("przelicznik")) {
                        unit = childNode.getTextContent();
                    } else if (childNode.getNodeName().equals("kod_waluty")) {
                        currencyCode = childNode.getTextContent();
                    } else if (childNode.getNodeName().equals("kurs_sredni")) {
                        rate = childNode.getTextContent();
                    }
                }

                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setPublisher(ExchangeRatePublisher.CENTRAL_BANK_OF_POLAND);
                exchangeRate.setPublishDate(publishDate);
                exchangeRate.setUnit(Integer.parseInt(unit));
                exchangeRate.setFromCurrency(currencyCode);
                exchangeRate.setToCurrency("PLN");
                exchangeRate.setValue(new BigDecimal(rate.replace(',', '.'))); // Ondalık ayracını hesaba katıyoruz...
                exchangeRates.add(exchangeRate);
            }

        } catch (Exception e) {
            throw new ApplicationException("Exception while exchange rate parse", e);
        }

        return exchangeRates;
    }

    /**
     * Bu sayfada değerlerin nasıl alınacağı açıklanıyor:
     * http://www.nbp.pl/homen.aspx?f=/kursy/instrukcja_pobierania_kursow_walut_en.html
     */
    public List<ExchangeRate> getLastPublishedExchangeRatesForPoland() {

        String xml;

        try {
            xml = Util.readContentOfFileAtUrl(exchangeRateWebService_poland_url + "/lasta.xml");
        } catch (Exception e) {
            throw new ApplicationException("Exception while exchange rate query", e);
        }

        return parseExchangeRatesXmlForPoland(xml);
    }

    public List<ExchangeRate> getExchangeRatesForPoland(LocalDate publishDate) {

        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try {

            /**
             * Aşağıda removeNonAsciiChars() kullanmamızın nedeni txt uzantılı dosyalardan gelen ilk satırda '\uFEFF' karakterinin olmasıdır.
             * The character &#65279 is the Unicode Character 'ZERO WIDTH NO-BREAK SPACE' (U+FEFF).
             */

            List<String> lines = Util.readLinesOfFileAtUrl(exchangeRateWebService_poland_url + "/dir.txt");
            String lastTwoDigitsOfYear = Integer.toString(publishDate.getYear()).substring(2);

            // İlk satırdaki yıl bilgisini inceleyip istediğimiz yıl olup olmadığına bakıyoruz.
            if (!Util.removeNonAsciiChars(lines.get(0)).substring(5, 7).equals(lastTwoDigitsOfYear)) {
                try {
                    lines = Util.readLinesOfFileAtUrl(exchangeRateWebService_poland_url + "/dir" + publishDate.getYear() + ".txt");
                } catch (FileNotFoundException fnfe) {
                    // Verilen yıla ait dosya yok demektir.
                    lines = Collections.emptyList();
                }
            }

            if (lines.size() > 0) {

                String publishDateAsYYMMDD = DateTimeFormatter.ofPattern("yyMMdd").format(publishDate);
                String xmlFileName = null;

                for (String line : lines) {
                    if (Util.removeNonAsciiChars(line).startsWith("a") && line.endsWith(publishDateAsYYMMDD)) {
                        xmlFileName = line;
                        break;
                    }
                }

                if (xmlFileName != null) {
                    String xml = Util.readContentOfFileAtUrl(exchangeRateWebService_poland_url + "/" + xmlFileName + ".xml");
                    exchangeRates = parseExchangeRatesXmlForPoland(xml);
                }
            }

        } catch (Exception e) {
            throw new ApplicationException("Exception while exchange rate query", e);
        }

        return exchangeRates;
    }

    public List<ExchangeRate> parseExchangeRatesXmlForTurkey(String xml) {

        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try {

            Document xmlDocument = Util.getDocumentFromXml(xml);
            XPath xPath = XPathFactory.newInstance().newXPath();

            Node nodeThatContainsPublishDate = (Node) xPath.compile("/Tarih_Date").evaluate(xmlDocument, XPathConstants.NODE);
            String publishDateAsStr = nodeThatContainsPublishDate.getAttributes().getNamedItem("Date").getNodeValue();
            LocalDate publishDate = LocalDate.parse(publishDateAsStr, DateTimeFormatter.ofPattern("MM/dd/yyyy"));

            NodeList nodeList = (NodeList) xPath.compile("/Tarih_Date/Currency").evaluate(xmlDocument, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node node = nodeList.item(i);

                String currencyCode = node.getAttributes().getNamedItem("CurrencyCode").getNodeValue();
                String unit = null;
                String forexBuying = null;
                String forexSelling = null;
                String banknoteBuying = null;
                String banknoteSelling = null;

                for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                    Node childNode = node.getChildNodes().item(j);
                    if (childNode.getNodeName().equals("Unit")) {
                        unit = childNode.getTextContent();
                    } else if (childNode.getNodeName().equals("ForexBuying")) {
                        forexBuying = childNode.getTextContent();
                    } else if (childNode.getNodeName().equals("ForexSelling")) {
                        forexSelling = childNode.getTextContent();
                    } else if (childNode.getNodeName().equals("BanknoteBuying")) {
                        banknoteBuying = childNode.getTextContent();
                    } else if (childNode.getNodeName().equals("BanknoteSelling")) {
                        banknoteSelling = childNode.getTextContent();
                    }
                }

                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setPublisher(ExchangeRatePublisher.CENTRAL_BANK_OF_TURKEY);
                exchangeRate.setPublishDate(publishDate);
                exchangeRate.setUnit(Integer.valueOf(unit));
                exchangeRate.setFromCurrency(currencyCode);
                exchangeRate.setToCurrency("TRY");
                exchangeRate.setValue(new BigDecimal(forexBuying)); // forexBuying değerini varsayılan değer olarak kabul ediyoruz.
                exchangeRate.setForexBuyingValue(new BigDecimal(forexBuying));
                if (StringUtils.isNotBlank(forexSelling)) {
                    exchangeRate.setForexSellingValue(new BigDecimal(forexSelling));
                }
                if (StringUtils.isNotBlank(banknoteBuying)) {
                    exchangeRate.setBanknoteBuyingValue(new BigDecimal(banknoteBuying));
                }
                if (StringUtils.isNotBlank(banknoteSelling)) {
                    exchangeRate.setBanknoteSellingValue(new BigDecimal(banknoteSelling));
                }
                exchangeRates.add(exchangeRate);
            }

        } catch (Exception e) {
            throw new ApplicationException("Exception while exchange rate parse", e);
        }

        return exchangeRates;
    }

    public List<ExchangeRate> getLastPublishedExchangeRatesForTurkey() {

        String xml;

        try {
            xml = Util.readContentOfFileAtUrl(exchangeRateWebService_turkey_url + "/today.xml");
        } catch (Exception e) {
            throw new ApplicationException("Exception while exchange rate query", e);
        }

        return parseExchangeRatesXmlForTurkey(xml);
    }

    public List<ExchangeRate> getExchangeRatesForTurkey(LocalDate publishDate) {

        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try {

            String publishDate_YYYY_MM = DateTimeFormatter.ofPattern("yyyyMM").format(publishDate);
            String publishDate_DD_MM_YYYY = DateTimeFormatter.ofPattern("ddMMyyyy").format(publishDate);
            String xml;

            try {
                xml = Util.readContentOfFileAtUrl(exchangeRateWebService_turkey_url + "/" + publishDate_YYYY_MM + "/" + publishDate_DD_MM_YYYY + ".xml");
            } catch (FileNotFoundException fnfe) {
                // Verilen tarihe ait dosya yok demektir.
                xml = null;
            }

            if (xml != null) {
                exchangeRates = parseExchangeRatesXmlForTurkey(xml);
            }

        } catch (Exception e) {
            throw new ApplicationException("Exception while exchange rate query", e);
        }

        return exchangeRates;
    }

    public List<ExchangeRate> getLastPublishedExchangeRatesForEurope() {

        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try {

            String xml = Util.readContentOfFileAtUrl(exchangeRateWebService_europe_url + "/eurofxref-daily.xml");
            Document xmlDocument = Util.getDocumentFromXml(xml);
            XPath xPath = XPathFactory.newInstance().newXPath();

            Node nodeThatContainsPublishDate = (Node) xPath.compile("/Envelope/Cube/Cube").evaluate(xmlDocument, XPathConstants.NODE);
            String publishDateAsStr = nodeThatContainsPublishDate.getAttributes().getNamedItem("time").getNodeValue();
            LocalDate publishDate = LocalDate.parse(publishDateAsStr, DateTimeFormatter.ISO_LOCAL_DATE);

            NodeList nodeList = (NodeList) xPath.compile("/Envelope/Cube/Cube/Cube").evaluate(xmlDocument, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node node = nodeList.item(i);
                String currencyCode = node.getAttributes().getNamedItem("currency").getNodeValue();
                String rate = node.getAttributes().getNamedItem("rate").getNodeValue();

                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setPublisher(ExchangeRatePublisher.CENTRAL_BANK_OF_EUROPE);
                exchangeRate.setPublishDate(publishDate);
                exchangeRate.setUnit(Integer.valueOf(1));
                exchangeRate.setFromCurrency("EUR");
                exchangeRate.setToCurrency(currencyCode);
                exchangeRate.setValue(new BigDecimal(rate));
                exchangeRates.add(exchangeRate);
            }

        } catch (Exception e) {
            throw new ApplicationException("Exception while exchange rate query", e);
        }

        return exchangeRates;
    }

    public List<ExchangeRate> getExchangeRatesForEurope(LocalDate publishDate) {

        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try {

            String xml = Util.readContentOfFileAtUrl(exchangeRateWebService_europe_url + "/eurofxref-hist-90d.xml");
            Document xmlDocument = Util.getDocumentFromXml(xml);
            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList nodeList = (NodeList) xPath.compile("/Envelope/Cube/Cube").evaluate(xmlDocument, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node nodeThatContainsPublishDate = nodeList.item(i);
                String publishDateAsStr = nodeThatContainsPublishDate.getAttributes().getNamedItem("time").getNodeValue();

                if (DateTimeFormatter.ISO_LOCAL_DATE.format(publishDate).equals(publishDateAsStr)) {

                    for (int j = 0; j < nodeThatContainsPublishDate.getChildNodes().getLength(); j++) {

                        Node node = nodeThatContainsPublishDate.getChildNodes().item(j);
                        String currencyCode = node.getAttributes().getNamedItem("currency").getNodeValue();
                        String rate = node.getAttributes().getNamedItem("rate").getNodeValue();

                        ExchangeRate exchangeRate = new ExchangeRate();
                        exchangeRate.setPublisher(ExchangeRatePublisher.CENTRAL_BANK_OF_EUROPE);
                        exchangeRate.setPublishDate(publishDate);
                        exchangeRate.setUnit(Integer.valueOf(1));
                        exchangeRate.setFromCurrency("EUR");
                        exchangeRate.setToCurrency(currencyCode);
                        exchangeRate.setValue(new BigDecimal(rate));
                        exchangeRates.add(exchangeRate);
                    }

                    break;
                }
            }

        } catch (Exception e) {
            throw new ApplicationException("Exception while exchange rate query", e);
        }

        return exchangeRates;
    }

    public List<ExchangeRate> getExchangeRates(ExchangeRatePublisher publisher, LocalDate publishDate) {
        if (publisher.equals(ExchangeRatePublisher.CENTRAL_BANK_OF_HUNGARY)) {
            return getExchangeRatesForHungary(publishDate);
        } else if (publisher.equals(ExchangeRatePublisher.CENTRAL_BANK_OF_POLAND)) {
            return getExchangeRatesForPoland(publishDate);
        } else if (publisher.equals(ExchangeRatePublisher.CENTRAL_BANK_OF_TURKEY)) {
            return getExchangeRatesForTurkey(publishDate);
        } else if (publisher.equals(ExchangeRatePublisher.CENTRAL_BANK_OF_EUROPE)) {
            return getExchangeRatesForEurope(publishDate);
        } else {
            throw new ApplicationException("No implementation for {0}", publisher);
        }
    }

    public List<ExchangeRate> getLastPublishedExchangeRates(ExchangeRatePublisher publisher) {
        if (publisher.equals(ExchangeRatePublisher.CENTRAL_BANK_OF_HUNGARY)) {
            return getLastPublishedExchangeRatesForHungary();
        } else if (publisher.equals(ExchangeRatePublisher.CENTRAL_BANK_OF_POLAND)) {
            return getLastPublishedExchangeRatesForPoland();
        } else if (publisher.equals(ExchangeRatePublisher.CENTRAL_BANK_OF_TURKEY)) {
            return getLastPublishedExchangeRatesForTurkey();
        } else if (publisher.equals(ExchangeRatePublisher.CENTRAL_BANK_OF_EUROPE)) {
            return getLastPublishedExchangeRatesForEurope();
        } else {
            throw new ApplicationException("No implementation for {0}", publisher);
        }
    }
    
    public boolean doExchangeRatesExist(ExchangeRatePublisher publisher, LocalDate publishDate) {
        long count = exchangeRateRepository.countByPublisherAndPublishDate(publisher, publishDate);
        return (count > 0);
    }

    @Transactional
    public void getLastPublishedExchangeRatesAndSave(ExchangeRatePublisher publisher, ZoneId zoneId) {
        LocalDate today = LocalDate.now(zoneId);
        if (!doExchangeRatesExist(publisher, today)) {
            List<ExchangeRate> exchangeRates = getLastPublishedExchangeRates(publisher);
            if (CollectionUtils.isNotEmpty(exchangeRates)) {
                /*
                Yukarıda verilerin kayıtlı olup olmadığını kontrol etmişken aşağıda neden tekrar kontrol ediyoruz? 
                Çünkü en son yayınlanan verileri aldığımızda, bu veriler bir veya daha önceki bir güne ait olabilir. 
                Örneğin bugünün verileri henüz yayınlanmadıysa, en son yayınlanan verilerini aldığımızda gelen veriler bir önceki güne ait olacak.
                 */
                if (!doExchangeRatesExist(publisher, exchangeRates.get(0).getPublishDate())) {
                    for (ExchangeRate exchangeRate : exchangeRates) {
                        exchangeRateRepository.save(exchangeRate);
                    }
                }
            }
        }
    }
    
    @Scheduled(cron = "0 0/30 * * * *")
    public void getLastPublishedExchangeRatesForHungaryAndSave() {
        getLastPublishedExchangeRatesAndSave(ExchangeRatePublisher.CENTRAL_BANK_OF_HUNGARY, Constants.DEFAULT_ZONE_ID_FOR_HUNGARY);
    }

    @Scheduled(cron = "0 0/30 * * * *")
    public void getLastPublishedExchangeRatesForPolandAndSave() {
        getLastPublishedExchangeRatesAndSave(ExchangeRatePublisher.CENTRAL_BANK_OF_POLAND, Constants.DEFAULT_ZONE_ID_FOR_POLAND);
    }

    @Scheduled(cron = "0 0/30 * * * *")
    public void getLastPublishedExchangeRatesForTurkeyAndSave() {
        getLastPublishedExchangeRatesAndSave(ExchangeRatePublisher.CENTRAL_BANK_OF_TURKEY, Constants.DEFAULT_ZONE_ID_FOR_TURKEY);
    }

    @Scheduled(cron = "0 0/30 * * * *")
    public void getLastPublishedExchangeRatesForEuropeAndSave() {
        getLastPublishedExchangeRatesAndSave(ExchangeRatePublisher.CENTRAL_BANK_OF_EUROPE, Constants.DEFAULT_ZONE_ID_FOR_EUROPE);
    }

    @Transactional
    public void getExchangeRatesAndSave(ExchangeRatePublisher publisher, LocalDate publishDate) {

        if (doExchangeRatesExist(publisher, publishDate)) {
            throw new BadRequestException("Exchange rates already exist.");
        }

        List<ExchangeRate> exchangeRates = getExchangeRates(publisher, publishDate);

        if (CollectionUtils.isEmpty(exchangeRates)) {
            throw new BadRequestException("No exchange rates found at publisher site.");
        }

        for (ExchangeRate exchangeRate : exchangeRates) {
            exchangeRateRepository.save(exchangeRate);
        }
    }

    public PagedDataHolder<ExchangeRateForUI> findAllByGivenParams(
            int pageNumber, int pageSize, String publisherCode, String publishDateStr, String fromCurrencyCode, String toCurrencyCode) {

        ExchangeRatePublisher publisher = (publisherCode != null ? ExchangeRatePublisher.valueOf(publisherCode) : null);
        LocalDate publishDate = (publishDateStr != null ? LocalDate.parse(publishDateStr, Constants.DEFAULT_DATE_FORMATTER) : null);

        Page<ExchangeRate> page = exchangeRateRepository.findAllByGivenParams(
                publisher,
                publishDate,
                fromCurrencyCode,
                toCurrencyCode,
                new PageRequest(pageNumber, pageSize));

        return new PagedDataHolder(Util.convert(page.getContent()), page.getTotalElements());
    }

    public BigDecimal convert(
            String publisherCode, String conversionDateStr, String fromCurrencyCode, String toCurrencyCode, String crossRateCurrencyCode,
            String preferredValueCode) {

        if (fromCurrencyCode.equals(toCurrencyCode)) {
            return BigDecimal.ONE;
        } else {

            ExchangeRatePublisher publisher = ExchangeRatePublisher.valueOf(publisherCode);
            LocalDate conversionDate = LocalDate.parse(conversionDateStr, Constants.DEFAULT_DATE_FORMATTER);
            PreferredValue preferredValue = (preferredValueCode != null ? PreferredValue.valueOf(preferredValueCode) : null);

            LocalDate actualPublishDate = exchangeRateRepository.findMaxPublishDateByPublisherAndPublishDate(publisher, conversionDate);

            System.out.println("actualPublishDate: " + actualPublishDate);

            List<ExchangeRate> exchangeRates = IterableUtils.toList(exchangeRateRepository.findAllByPublisherAndPublishDate(publisher, actualPublishDate));

            return Util.getExchangeRateValue(exchangeRates, fromCurrencyCode, toCurrencyCode, crossRateCurrencyCode, preferredValue);
        }
    }
    
    public boolean isInProduction() {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles != null && activeProfiles.length > 0) {
            for (String profile : activeProfiles) {
                if ("prod".equals(profile)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Scheduled(cron = "0 0 5 * * *", zone = "UTC")
    public void checkMissingDays() throws MessagingException {

        if (isInProduction()) {

            LocalDate today = LocalDate.now(ZoneId.of("UTC"));
            int numberOfDaysToCheck = 7;
            List<MissingDay> missingDays = new ArrayList<>();

            for (int i = 1; i <= numberOfDaysToCheck; i++) {
                LocalDate publishDate = today.minusDays(i);
                if (!publishDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !publishDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                    for (ExchangeRatePublisher publisher : ExchangeRatePublisher.values()) {
                        if (!doExchangeRatesExist(publisher, publishDate)) {
                            missingDays.add(new MissingDay(Constants.CONVERTER_TYPE_FOR_PUBLISHER_ENUM.convert(publisher), publishDate.toString()));
                        }
                    }
                }
            }

            MimeMessage mimeMessage = defaultMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(javaMailSenderConfig.getUsername());
            helper.setTo("oneorder.integration@ekol.com");

            if (CollectionUtils.isEmpty(missingDays)) {
                helper.setSubject("exchange rates: no missing day for last " + numberOfDaysToCheck + " days");
                helper.setText("", true);
            } else {
                helper.setSubject("exchange rates: check missing days for last " + numberOfDaysToCheck + " days");
                Collections.sort(missingDays, MissingDay.SORTER);
                StringBuilder sb = new StringBuilder();
                sb.append("<table>");
                sb.append("<tr>");
                sb.append("<td><b>Publisher</b></td>");
                sb.append("<td><b>Publish Date</b></td>");
                sb.append("</tr>");
                for (MissingDay missingDay : missingDays) {
                    sb.append("<tr>");
                    sb.append("<td>" + missingDay.getPublisher() + "</td>");
                    sb.append("<td>" + missingDay.getPublishDate() + "</td>");
                    sb.append("</tr>");
                }
                sb.append("</table>");
                helper.setText(sb.toString(), true);
            }

            defaultMailSender.send(mimeMessage);
        }
    }
}
