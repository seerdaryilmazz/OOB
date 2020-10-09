package ekol.location.domain.location.comnon;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.location.domain.Country;
import ekol.location.domain.LocationType;
import ekol.location.domain.location.comnon.workinghour.WorkingHour;

/**
 * Created by burak on 03/04/17.
 */
@MappedSuperclass
public abstract class Place extends BaseEntity {

    private String name;

    private String localName;
    
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean active;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "locationId")
    private Location location;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "establishmentId")
    private Establishment establishment;

    public abstract LocationType getType();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Establishment getEstablishment() {
        return establishment;
    }

    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }


    @JsonIgnore
    public Country getCountry(){
        return getEstablishment() != null ? getEstablishment().getCountry() : null;
    }
    public void setCountry(Country country){
        if(getEstablishment() != null){
            getEstablishment().setCountry(country);
        }
    }
    @JsonIgnore
    public String getCountryName(){
        return getCountry() != null ? getCountry().getName() : null;
    }

    @JsonIgnore
    public String getCountryIso(){
        return getCountry() != null ? getCountry().getIso() : null;
    }

    @JsonIgnore
    public Set<WorkingHour> getWorkingHours(){
        return getEstablishment() != null ? getEstablishment().getWorkingHours() : new HashSet<>();
    }
}
