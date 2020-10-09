package ekol.kartoteks.service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.ElasticsearchException;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import ekol.kartoteks.domain.Company;
import ekol.kartoteks.domain.search.CompanySearchDoc;
import ekol.kartoteks.repository.CompanyRepository;
import lombok.AllArgsConstructor;

/**
 * Created by kilimci on 25/03/16.
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyIndexingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyIndexingService.class);

    private ElasticsearchTemplate elasticsearchTemplate;
    private CompanyRepository companyRepository;

    @Transactional
    public void indexImportedCompanyData(){

        String uuid = UUID.randomUUID().toString();

        try {

        	LOGGER.info("Company indexing started. uuid: {}", uuid);

            elasticsearchTemplate.deleteIndex(CompanySearchDoc.class);
            elasticsearchTemplate.createIndex(CompanySearchDoc.class);
            elasticsearchTemplate.putMapping(CompanySearchDoc.class);
            elasticsearchTemplate.refresh(CompanySearchDoc.class);

            AtomicLong companyCount = new AtomicLong();
            try (Stream<Company> companies = companyRepository.findByNameNotNull()) {
            	companies.forEach(c->{
            		elasticsearchTemplate.index(new IndexQueryBuilder().withId(c.getId().toString()).withObject(CompanySearchDoc.fromCompany(c)).build());
            		
            		long count = companyCount.getAndIncrement();
            		if (count % 100 == 0) {
            			LOGGER.info("{} company indexed. Please keep waiting...", count);
            		}
            	});
            }

            LOGGER.info("Company indexing finished successfully. uuid: {}", uuid);

        } catch (ElasticsearchException e) {
        	LOGGER.error("Company indexing failed. uuid: " + uuid, e);
            if (!CollectionUtils.isEmpty(e.getFailedDocuments())) {
                StringBuilder sb = new StringBuilder();
                for (String key : e.getFailedDocuments().keySet()) {
                    sb.append(key + ": " + e.getFailedDocuments().get(key) + "\n");
                }
                LOGGER.error("See failed documents below. uuid: {}\n{}", uuid, sb);
            }
        }
    }

    public void indexCompany(Company company){
        elasticsearchTemplate.index(new IndexQueryBuilder().withId(company.getId().toString()).withObject(CompanySearchDoc.fromCompany(company)).build());
    }

    public void removeCompany(Company company){
        elasticsearchTemplate.delete(CompanySearchDoc.class, String.valueOf(company.getId()));
    }
}
