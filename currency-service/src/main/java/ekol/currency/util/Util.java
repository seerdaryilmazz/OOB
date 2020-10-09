package ekol.currency.util;

import ekol.currency.domain.ExchangeRate;
import ekol.currency.domain.PreferredValue;
import ekol.currency.domain.dto.ExchangeRateForUI;
import ekol.exceptions.ApplicationException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class Util {

    public static <E> HashSet<E> hashSet(E... args) {

        HashSet<E> set = new HashSet<>();

        for (E obj : args) {
            set.add(obj);
        }

        return set;
    }

    public static <E> LinkedHashSet<E> linkedHashSet(E... args) {

        LinkedHashSet<E> set = new LinkedHashSet<>();

        for (E obj : args) {
            set.add(obj);
        }

        return set;
    }

    public static <K, V> HashMap<K, V> hashMap(Pair<K, V>... args) {

        HashMap<K, V> map = new HashMap<>();

        for (Pair<K, V> pair : args) {
            map.put(pair.getLeft(), pair.getRight());
        }

        return map;
    }

    public static List<String> readLinesOfFileAtUrl(String urlStr) throws IOException {

        List<String> lines = new ArrayList<>();
        BufferedReader in = null;

        try {
            
            URL url = new URL(urlStr);

            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(Constants.DEFAULT_CONNECT_TIMEOUT);
            connection.setReadTimeout(Constants.DEFAULT_READ_TIMEOUT);
            
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            
            String line;
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }

            in.close();

        } catch (IOException e) {
            IOUtils.closeQuietly(in);
            throw e;
        }

        return lines;
    }

    public static String readContentOfFileAtUrl(String urlStr) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader in = null;

        try {
            
            URL url = new URL(urlStr);

            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(Constants.DEFAULT_CONNECT_TIMEOUT);
            connection.setReadTimeout(Constants.DEFAULT_READ_TIMEOUT);
            
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            
            String line;
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            
            in.close();
            
        } catch (IOException e) {
            IOUtils.closeQuietly(in);
            throw e;
        }

        return stringBuilder.toString();
    }

    public static Document getDocumentFromXml(String xml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(xml.getBytes()));
    }

    public static String removeNonAsciiChars(String s) {
        return s.replaceAll("[^\\x00-\\x7F]", "");
    }

    private static BigDecimal getExchangeRateValue(ExchangeRate exchangeRate, PreferredValue preferredValue) {

        BigDecimal value;

        if (preferredValue == null) {
            value = exchangeRate.getValue();
        } else {

            if (preferredValue.equals(PreferredValue.FOREX_BUYING)) {
                value = exchangeRate.getForexBuyingValue();
            } else if (preferredValue.equals(PreferredValue.FOREX_SELLING)) {
                value = exchangeRate.getForexSellingValue();
            } else if (preferredValue.equals(PreferredValue.BANKNOTE_BUYING)) {
                value = exchangeRate.getBanknoteBuyingValue();
            } else if (preferredValue.equals(PreferredValue.BANKNOTE_SELLING)) {
                value = exchangeRate.getBanknoteSellingValue();
            } else {
                throw new ApplicationException("No implementation for {0}", preferredValue);
            }

            if (value == null) {
                throw new ApplicationException("No value for {0}", preferredValue);
            }
        }

        return value;
    }

    private static BigDecimal getExchangeRateValue(
            List<ExchangeRate> exchangeRates,
            String fromCurrency,
            String toCurrency,
            PreferredValue preferredValue) {

        BigDecimal value = null;

        if (fromCurrency.equals(toCurrency)) {
            value = BigDecimal.ONE;
        } else {
            for (ExchangeRate exchangeRate : exchangeRates) {
                if (exchangeRate.getFromCurrency().equals(fromCurrency) && exchangeRate.getToCurrency().equals(toCurrency)) {
                    value = getExchangeRateValue(exchangeRate, preferredValue);
                    value = value.divide(BigDecimal.valueOf(exchangeRate.getUnit()), 10, BigDecimal.ROUND_HALF_UP);
                    break;
                } else if (exchangeRate.getFromCurrency().equals(toCurrency) && exchangeRate.getToCurrency().equals(fromCurrency)) {
                    value = getExchangeRateValue(exchangeRate, preferredValue);
                    value = BigDecimal.valueOf(exchangeRate.getUnit()).divide(value, 10, BigDecimal.ROUND_HALF_UP);
                    break;
                }
            }
        }

        return value;
    }

    /**
     * @param crossRateCurrency Çapraz hesaplama yapmak durumunda kalırsak arada kullanacağımız para birimi
     */
    public static BigDecimal getExchangeRateValue(
            List<ExchangeRate> exchangeRates,
            String fromCurrency,
            String toCurrency,
            String crossRateCurrency,
            PreferredValue preferredValue) {

        BigDecimal value = getExchangeRateValue(exchangeRates, fromCurrency, toCurrency, preferredValue);

        // Çapraz kur bulmaya çalışacağız.
        if (value == null && crossRateCurrency != null) {
            BigDecimal value1 = getExchangeRateValue(exchangeRates, fromCurrency, crossRateCurrency, preferredValue);
            if (value1 != null) {
                BigDecimal value2 = getExchangeRateValue(exchangeRates, crossRateCurrency, toCurrency, preferredValue);
                if (value2 != null) {
                    value = value1.multiply(value2);
                }
            }
        }

        if (value == null) {
            throw new ApplicationException("{0} cannot be converted to {1}", fromCurrency, toCurrency);
        } else {

            /**
             * Döviz kurları çevriminde virgülden sonra 6 rakamdan fazla gösteren yer/site görmedik. O yüzden
             * yuvarlama yaparken 6 kullanıyoruz.
             */

            return value.setScale(6, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * TODO: Normalde böyle bir çevrime gerek yok ancak currency-service'i ve currency-service'i kullanan management-ui'ı aynı anda prod'a 
     * alamadığımız için böyle yaptık. Uygun bir zamanda ExchangeRateForUI silinip yerine yine ExchangeRate kullanılabilir.
     */
    public static List<ExchangeRateForUI> convert(List<ExchangeRate> list) {
        List<ExchangeRateForUI> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (ExchangeRate elem : list) {
                resultList.add(ExchangeRateForUI.convert(elem));
            }
        }
        return resultList;
    }
}
