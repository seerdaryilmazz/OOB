package ekol.kartoteks.event;

import ekol.kartoteks.domain.Company;

/**
 * Created by kilimci on 27/11/2017.
 */
public class CompanyDataEventMessage {

    private Long id;
    private String name;
    private Operation operation;

    public static CompanyDataEventMessage createWith(Company company, Operation operation){
        CompanyDataEventMessage message = new CompanyDataEventMessage();
        message.setId(company.getId());
        message.setName(company.getName());
        message.setOperation(operation);
        return message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
