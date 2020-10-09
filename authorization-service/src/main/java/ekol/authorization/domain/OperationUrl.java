package ekol.authorization.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by ozer on 27/02/2017.
 */
@Entity
@Table(name = "operation_url")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedEntityGraphs({
        @NamedEntityGraph(name = "OperationUrl.withOperation", attributeNodes = {
                @NamedAttributeNode("operation")
        })
})
public class OperationUrl extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_operation_url", sequenceName = "seq_operation_url")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_operation_url")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operationId")
    private Operation operation;

    @Column
    private String serviceName;

    @Column
    private String url;

    public OperationUrl() {
    }

    public OperationUrl(Operation operation, String serviceName, String url) {
        this.operation = operation;
        this.serviceName = serviceName;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
