package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.*;

/**
 * TODO: from ve to alanları şuan için nullable ama böyle olmamaları lazım.
 * Değiştirmek için LinehaulRouteService'teki createOrUpdate metodunda değişiklik yapmak gerek.
 */
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "LinehaulRoute.allDetails",
                attributeNodes = {
                        @NamedAttributeNode(value = "from"),
                        @NamedAttributeNode(value = "to"),
                        @NamedAttributeNode(value = "fragments", subgraph = "LinehaulRouteFragment.allDetails")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "LinehaulRouteFragment.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "leg"),
                                        @NamedAttributeNode(value = "route"),
                                        @NamedAttributeNode(value = "from"),
                                        @NamedAttributeNode(value = "to")
                                }
                        )
                }
        )
})
@Entity
@Table(name = "LhaulRoute")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinehaulRoute extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_lhaul_route", sequenceName = "seq_lhaul_route")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lhaul_route")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromId")
    private LinehaulRouteLegStop from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toId")
    private LinehaulRouteLegStop to;

    @OneToMany(mappedBy = "parent")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<LinehaulRouteFragment> fragments = new HashSet<>();

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

    public LinehaulRouteLegStop getFrom() {
        return from;
    }

    public void setFrom(LinehaulRouteLegStop from) {
        this.from = from;
    }

    public LinehaulRouteLegStop getTo() {
        return to;
    }

    public void setTo(LinehaulRouteLegStop to) {
        this.to = to;
    }

    public Set<LinehaulRouteFragment> getFragments() {
        return fragments;
    }

    public void setFragments(Set<LinehaulRouteFragment> fragments) {
        this.fragments = fragments;
    }

    // TODO: Tek bir veritabanı sorgusu ile yapabilir miyiz?
    public List<LinehaulRouteLeg> getRouteLegs() {
        List<LinehaulRouteLeg> routeLegs = new ArrayList<>();
        appendRouteLegs(routeLegs, getFragments());
        return routeLegs;
    }

    private void appendRouteLegs(List<LinehaulRouteLeg> routeLegs, Set<LinehaulRouteFragment> linehaulFragments) {

        if (linehaulFragments != null) {

            List<LinehaulRouteFragment> list = new ArrayList<>(linehaulFragments);
            Collections.sort(list, LinehaulRouteFragment.FRAGMENT_COMPARATOR);

            list.forEach(fragment -> {
                if (LinehaulRouteFragmentType.ONE_LEGGED.equals(fragment.getType())) {
                    // from'un initiliaze olması için herhangi bir metodunu çağırıyoruz, yani getName() metodu olmasının bir önemi yok.
                    fragment.getLeg().getFrom().getName();
                    // to'nun initiliaze olması için herhangi bir metodunu çağırıyoruz, yani getName() metodu olmasının bir önemi yok.
                    fragment.getLeg().getTo().getName();
                    routeLegs.add(fragment.getLeg());
                } else {
                    appendRouteLegs(routeLegs, fragment.getRoute().getFragments());
                }
            });
        }
    }
}
