package ekol.kartoteks.builder;

import ekol.kartoteks.domain.Sector;

/**
 * Created by kilimci on 14/10/16.
 */
public final class SectorBuilder {
    private Long id ;
    private Sector parent;
    private String name;
    private String code;
    private boolean deleted;

    private SectorBuilder() {
    }

    public static SectorBuilder aSector() {
        return new SectorBuilder();
    }

    public SectorBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public SectorBuilder withParent(Sector parent) {
        this.parent = parent;
        return this;
    }

    public SectorBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SectorBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public SectorBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public SectorBuilder but() {
        return aSector().withId(id).withParent(parent).withName(name).withCode(code).withDeleted(deleted);
    }

    public Sector build() {
        Sector sector = new Sector();
        sector.setId(id);
        sector.setParent(parent);
        sector.setName(name);
        sector.setCode(code);
        sector.setDeleted(deleted);
        return sector;
    }
}
