package ekol.authorization.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.json.serializers.common.EnumJsonDeserializer;
import ekol.json.serializers.common.EnumJsonSerializer;
import org.hibernate.annotations.Where;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.*;

/**
 * Created by ozer on 27/02/2017.
 */
@Entity
@Table(name = "operation_url_method")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OperationUrlMethod extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_operation_url_method", sequenceName = "seq_operation_url_method")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_operation_url_method")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operationUrlId")
    private OperationUrl operationUrl;

    @Column
    @Enumerated(EnumType.STRING)
    @JsonSerialize(using = EnumJsonSerializer.class)
    @JsonDeserialize(using = EnumJsonDeserializer.class)
    private RequestMethod method;

    public OperationUrlMethod() {
    }

    public OperationUrlMethod(OperationUrl operationUrl, RequestMethod method) {
        this.operationUrl = operationUrl;
        this.method = method;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OperationUrl getOperationUrl() {
        return operationUrl;
    }

    public void setOperationUrl(OperationUrl operationUrl) {
        this.operationUrl = operationUrl;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }
}
