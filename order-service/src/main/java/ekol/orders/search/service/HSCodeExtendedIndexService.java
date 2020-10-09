package ekol.orders.search.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import ekol.orders.lookup.domain.HSCodeExtended;
import ekol.orders.lookup.repository.HSCodeExtendedRepository;
import ekol.orders.search.domain.HSCodeExtendedSearchDocument;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class HSCodeExtendedIndexService {

    private ElasticsearchTemplate elasticsearchTemplate;
    private HSCodeExtendedRepository hsCodeExtendeddRepository;
    
    @PostConstruct
    private void init() {
    	checkIndexAndCreate();
    }
    
    private void checkIndexAndCreate() {
    	if (!elasticsearchTemplate.indexExists(HSCodeExtendedSearchDocument.class)) {
            elasticsearchTemplate.createIndex(HSCodeExtendedSearchDocument.class);
            elasticsearchTemplate.putMapping(HSCodeExtendedSearchDocument.class);
            elasticsearchTemplate.refresh(HSCodeExtendedSearchDocument.class);
        }
    }

    public void indexHSCodes(){
        elasticsearchTemplate.deleteIndex(HSCodeExtendedSearchDocument.class);
        elasticsearchTemplate.createIndex(HSCodeExtendedSearchDocument.class );

        List<IndexQuery> indexQueries = new ArrayList<>();

        try (Stream<HSCodeExtended> hsCodes = hsCodeExtendeddRepository.findByOrderByCode().stream()) {
            hsCodes.forEach(c -> {
                indexQueries.add(new IndexQueryBuilder().withId(c.getId().toString()).withObject(HSCodeExtendedSearchDocument.fromHSCode(c)).build());
            });
        }

        elasticsearchTemplate.bulkIndex(indexQueries);
    }

    public void indexHSCode(HSCodeExtended hsCode){
        elasticsearchTemplate.index(new IndexQueryBuilder().withId(hsCode.getId().toString()).withObject(HSCodeExtendedSearchDocument.fromHSCode(hsCode)).build());
    }

    public void removeHSCode(HSCodeExtended hsCode){
        elasticsearchTemplate.delete(HSCodeExtendedSearchDocument.class, String.valueOf(hsCode.getId()));
    }
}
