package ekol.orders.order.domain.dto.response.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.order.domain.dto.response.kartoteks.Country;
import ekol.orders.transportOrder.common.domain.IdNamePair;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private IdNamePair companyLocation;

    private Location location;

    private boolean ekolWarehouse;

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

    public IdNamePair getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(IdNamePair companyLocation) {
        this.companyLocation = companyLocation;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isEkolWarehouse() {
        return ekolWarehouse;
    }

    public void setEkolWarehouse(boolean ekolWarehouse) {
        this.ekolWarehouse = ekolWarehouse;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {

        private Long id;
        private String name;
        private PostalAddress postaladdress;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public PostalAddress getPostaladdress() {
            return postaladdress;
        }

        public void setPostaladdress(PostalAddress postaladdress) {
            this.postaladdress = postaladdress;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PostalAddress {

            //  private String formattedAddress;

            private String postalCode;

            private Country country;

            private Point pointOnMap;

      /*      public String getFormattedAddress() {
                return formattedAddress;
            }

            public void setFormattedAddress(String formattedAddress) {
                this.formattedAddress = formattedAddress;
            }*/

            public String getPostalCode() {
                return postalCode;
            }

            public void setPostalCode(String postalCode) {
                this.postalCode = postalCode;
            }

            public Country getCountry() {
                return country;
            }

            public void setCountry(Country country) {
                this.country = country;
            }

            public Point getPointOnMap() {
                return pointOnMap;
            }

            public void setPointOnMap(Point pointOnMap) {
                this.pointOnMap = pointOnMap;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Point {

                private BigDecimal lat;
                private BigDecimal lng;

                public BigDecimal getLat() {
                    return lat;
                }

                public void setLat(BigDecimal lat) {
                    this.lat = lat;
                }

                public BigDecimal getLng() {
                    return lng;
                }

                public void setLng(BigDecimal lng) {
                    this.lng = lng;
                }
            }

        }
    }
}
