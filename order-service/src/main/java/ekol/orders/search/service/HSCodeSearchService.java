package ekol.orders.search.service;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import ekol.orders.search.domain.HSCodeSearchDocument;

/**
 * Created by kilimci on 29/04/16.
 */
@Service
public class HSCodeSearchService {

    private ElasticsearchTemplate elasticsearchTemplate;
    
    private Converter<HSCodeSearchDocument,HSCodeSearchDocument> pageConverter = new Converter<HSCodeSearchDocument, HSCodeSearchDocument>() {
    	@Override
		public HSCodeSearchDocument convert(HSCodeSearchDocument source) {
			return source;
		}
	};

    @Autowired
    public HSCodeSearchService(ElasticsearchTemplate elasticsearchTemplate){
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    public Page<HSCodeSearchDocument> searchHSCode(String q, int page, int size) {

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(new PageRequest(page - 1, size))
                .withQuery(QueryBuilders.multiMatchQuery(q, "name", "code")).build();
        return elasticsearchTemplate.queryForPage(searchQuery, HSCodeSearchDocument.class).map(pageConverter);
    }

    public Page<HSCodeSearchDocument> searchHSCode(String q) {

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(q, "name", "code")).build();
        return elasticsearchTemplate.queryForPage(searchQuery, HSCodeSearchDocument.class).map(pageConverter);
    }

    public HSCodeSearchDocument findHSCode(String code) {
        CriteriaQuery query = new CriteriaQuery(Criteria.where("code").is(code));
        return elasticsearchTemplate.queryForObject(query, HSCodeSearchDocument.class);
    }
}
