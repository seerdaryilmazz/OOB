package ekol.crm.search.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * org.springframework.data.domain.PageImpl sınıfında default constructor olmadığından deserialize aşamasında hata oluşuyor,
 * hata almamak için PageImpl yerine aşağıdaki sınıfı kullanıyoruz.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomPageImpl<T> extends PageImpl<T> {

    public CustomPageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public CustomPageImpl(List<T> content) {
        super(content);
    }

    public CustomPageImpl() {
        super(new ArrayList<>());
    }
}
