package ekol.orders.search.service;

import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import ekol.orders.search.domain.HSCodeExtendedSearchDocument;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class HSCodeExtendedSearchService {

	private ElasticsearchTemplate elasticsearchTemplate;

	final private Converter<HSCodeExtendedSearchDocument,HSCodeExtendedSearchDocument> pageConverter = new Converter<HSCodeExtendedSearchDocument, HSCodeExtendedSearchDocument>() {
		@Override
		public HSCodeExtendedSearchDocument convert(HSCodeExtendedSearchDocument source) {
			return source;
		}
	};

	public Page<HSCodeExtendedSearchDocument> searchHSCode(String q, int page, int size) {

		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withPageable(new PageRequest(page - 1, size))
				.withQuery(QueryBuilders.multiMatchQuery(q, "name", "code").type(Type.PHRASE_PREFIX).slop(10).maxExpansions(50))
				.build();
		return elasticsearchTemplate.queryForPage(searchQuery, HSCodeExtendedSearchDocument.class).map(pageConverter);
	}

	public Page<HSCodeExtendedSearchDocument> searchHSCode(String q) {

		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withPageable(new PageRequest(0, 20))
				.withQuery(QueryBuilders.multiMatchQuery(q, "name", "code").type(Type.PHRASE_PREFIX).slop(10).maxExpansions(50))
				.build();
		return elasticsearchTemplate.queryForPage(searchQuery, HSCodeExtendedSearchDocument.class).map(pageConverter);
	}

}
