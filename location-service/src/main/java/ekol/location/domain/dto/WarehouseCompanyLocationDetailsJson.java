package ekol.location.domain.dto;

import ekol.location.domain.location.comnon.Establishment;
import ekol.location.domain.location.comnon.IdNameEmbeddable;
import ekol.model.IdNamePair;

public class WarehouseCompanyLocationDetailsJson {

    private IdNamePair company;
    private IdNamePair location;
    private Establishment establishment;

    public static WarehouseCompanyLocationDetailsJson with(IdNameEmbeddable company, IdNameEmbeddable location, Establishment establishment){
        WarehouseCompanyLocationDetailsJson json = new WarehouseCompanyLocationDetailsJson();
        json.setCompany(new IdNamePair(company.getId(), company.getName()));
        json.setLocation(new IdNamePair(location.getId(), location.getName()));
        json.setEstablishment(establishment);
        return json;
    }

    public IdNamePair getCompany() {
        return company;
    }

    public void setCompany(IdNamePair company) {
        this.company = company;
    }

    public IdNamePair getLocation() {
        return location;
    }

    public void setLocation(IdNamePair location) {
        this.location = location;
    }

    public Establishment getEstablishment() {
        return establishment;
    }

    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }
}
