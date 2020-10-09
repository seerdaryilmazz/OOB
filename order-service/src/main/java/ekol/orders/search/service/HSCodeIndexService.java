package ekol.orders.search.service;

import ekol.orders.search.domain.HSCodeSearchDocument;
import ekol.orders.lookup.domain.HSCode;
import ekol.orders.lookup.repository.HSCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by kilimci on 25/03/16.
 */
@Service
public class HSCodeIndexService {

    private ElasticsearchTemplate elasticsearchTemplate;
    private HSCodeRepository hsCodeRepository;

    @Autowired
    public HSCodeIndexService(ElasticsearchTemplate elasticsearchTemplate, HSCodeRepository hsCodeRepository){
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.hsCodeRepository = hsCodeRepository;
    }

    private static final String HSCODE_INDEX_NAME = "hscode";
    private static final int TIER3_CODE_LENGTH = 6;
    private static final int TIER2_CODE_LENGTH = 4;
    private static final int TIER1_CODE_LENGTH = 2;

    public void indexHSCodes(){
        elasticsearchTemplate.deleteIndex(HSCODE_INDEX_NAME);
        elasticsearchTemplate.createIndex(HSCODE_INDEX_NAME);

        List<IndexQuery> indexQueries = new ArrayList<>();

        try (Stream<HSCode> hsCodes = hsCodeRepository.findByOrderByCode().stream()) {
            hsCodes.forEach(c -> {
                List<HSCode> parents = findParents(c);
                indexQueries.add(
                        new IndexQueryBuilder().withId(c.getId().toString()).withObject(
                                HSCodeSearchDocument.fromHSCode(c).addParents(parents)).build());
            });
        }

        elasticsearchTemplate.bulkIndex(indexQueries);
    }

    private List<HSCode> findParents(HSCode hscode){
        List<HSCode> parents = new ArrayList<>();
        if(hscode.getCode().length() == TIER1_CODE_LENGTH){
            return parents;
        }
        if(hscode.getCode().length() >= TIER2_CODE_LENGTH){
            HSCode parent = findTier1Parent(hscode);
            if(parent != null){
                parents.add(parent);
            }
        }
        if(hscode.getCode().length() >= TIER3_CODE_LENGTH){
            HSCode parent = findTier2Parent(hscode);
            if(parent != null){
                parents.add(parent);
            }
        }
        return parents;
    }
    private HSCode findTier1Parent(HSCode hscode){
        String parentCode = hscode.getCode().substring(0,TIER1_CODE_LENGTH);
        return hsCodeRepository.findByCode(parentCode);
    }
    private HSCode findTier2Parent(HSCode hscode){
        String parentCode = hscode.getCode().substring(0,TIER2_CODE_LENGTH);
        return hsCodeRepository.findByCode(parentCode);
    }

    public void indexHSCode(HSCode hsCode){
        elasticsearchTemplate.index(new IndexQueryBuilder().withId(hsCode.getId().toString()).withObject(HSCodeSearchDocument.fromHSCode(hsCode)).build());
    }

    public void removeHSCode(HSCode hsCode){
        elasticsearchTemplate.delete(HSCodeSearchDocument.class, String.valueOf(hsCode.getId()));
    }
}
