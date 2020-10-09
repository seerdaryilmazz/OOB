package ekol.currency.repository;

import ekol.currency.domain.ExchangeRate;
import ekol.currency.domain.ExchangeRatePublisher;
import ekol.hibernate5.domain.repository.ApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateRepository extends ApplicationRepository<ExchangeRate> {

    @Query("select er from ExchangeRate er where " +
            "(:publisher is null or er.publisher = :publisher) and " +
            "(:publishDate is null or er.publishDate = :publishDate) and " +
            "(:fromCurrency is null or er.fromCurrency = :fromCurrency) and " +
            "(:toCurrency is null or er.toCurrency = :toCurrency)")
    Page<ExchangeRate> findAllByGivenParams(
            @Param("publisher") ExchangeRatePublisher publisher,
            @Param("publishDate") LocalDate publishDate,
            @Param("fromCurrency") String fromCurrency,
            @Param("toCurrency") String toCurrency,
            Pageable pageable);

    Iterable<ExchangeRate> findAllByPublisherAndPublishDate(ExchangeRatePublisher publisher, LocalDate publishDate);

    @Query("select distinct er.publishDate from ExchangeRate er where er.publisher = :publisher order by er.publishDate desc")
    List<LocalDate> findDistinctPublishDatesByPublisher(
            @Param("publisher") ExchangeRatePublisher publisher,
            Pageable pageable);

    @Query("select count(distinct er.publishDate) from ExchangeRate er where er.publisher = :publisher order by er.publishDate desc")
    long countDistinctPublishDatesByPublisher(
            @Param("publisher") ExchangeRatePublisher publisher);

    long countByPublisherAndPublishDate(ExchangeRatePublisher publisher, LocalDate publishDate);

    @Query("select max(er.publishDate) from ExchangeRate er where er.publisher = :publisher and er.publishDate <= :publishDate")
    LocalDate findMaxPublishDateByPublisherAndPublishDate(
            @Param("publisher") ExchangeRatePublisher publisher,
            @Param("publishDate") LocalDate publishDate);

}
