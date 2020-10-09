package ekol.orders.transportOrder.dto;

/**
 * Created by burak on 10/08/16.
 */
public class TradeTypeModal {

    private TradeTypePostalAddress postaladdress;

    public TradeTypePostalAddress getPostaladdress() {
        return postaladdress;
    }

    public void setPostaladdress(TradeTypePostalAddress postaladdress) {
        this.postaladdress = postaladdress;
    }

    public static class TradeTypePostalAddress {

        private TradeTypeCountry country;

        public TradeTypeCountry getCountry() {
            return country;
        }

        public void setCountry(TradeTypeCountry country) {
            this.country = country;
        }
    }

    public static class TradeTypeCountry {

        private String countryName;
        private boolean euMember;

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public boolean isEuMember() {
            return euMember;
        }

        public void setEuMember(boolean euMember) {
            this.euMember = euMember;
        }
    }
}
