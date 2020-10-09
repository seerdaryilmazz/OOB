package ekol.orders.search.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ekol.orders.lookup.domain.HSCode;
import lombok.Data;

/**
 * Created by bverid on 08/08/16.
 */
@Data
@Document(indexName = "hscode")
public class HSCodeSearchDocument {

    private Long id;
    private String code;
    private String name;
    @JsonInclude(Include.NON_EMPTY)
    private List<HSCodeSearchDocument> parents = new ArrayList<>();

    public static HSCodeSearchDocument fromHSCode(HSCode hsCode){
        HSCodeSearchDocument doc = new HSCodeSearchDocument();
        doc.setId(hsCode.getId());
        doc.setCode(hsCode.getCode());
        doc.setName(hsCode.getName());

        return doc;
    }

    public HSCodeSearchDocument addParents(List<HSCode> parentHsCodes){
        this.parents = parentHsCodes.stream().sorted(Comparator.comparing(HSCode::getTier)).map(HSCodeSearchDocument::fromHSCode).collect(Collectors.toList());
        return this;
    }
}
