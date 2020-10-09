package ekol.kartoteks.builder;

import ekol.kartoteks.domain.SalesPortfolio;

/**
 * Created by kilimci on 17/10/16.
 */
public class SalesPortfolioBuilder {
    private Long id;
    private String code;
    private String name;
    private boolean deleted;

    private SalesPortfolioBuilder() {
    }

    public static SalesPortfolioBuilder aSalesPortfolio() {
        return new SalesPortfolioBuilder();
    }

    public SalesPortfolioBuilder withId(Long id) {
        this.id = id;
        return this;
    }
    public SalesPortfolioBuilder withCode(String code) {
        this.code = code;
        return this;
    }
    public SalesPortfolioBuilder withName(String name) {
        this.name = name;
        return this;
    }
    public SalesPortfolioBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public SalesPortfolioBuilder but() {
        return aSalesPortfolio().withId(id).withName(name).withCode(code).withDeleted(deleted);
    }

    public SalesPortfolio build() {
        SalesPortfolio salesPortfolio = new SalesPortfolio();
        salesPortfolio.setId(id);
        salesPortfolio.setName(name);
        salesPortfolio.setCode(code);
        salesPortfolio.setDeleted(deleted);
        return salesPortfolio;
    }
}
