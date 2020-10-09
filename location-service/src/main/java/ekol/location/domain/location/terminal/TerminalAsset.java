package ekol.location.domain.location.terminal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.location.domain.location.enumeration.TerminalAssetType;

import javax.persistence.*;

/**
 * Created by kilimci on 28/04/2017.
 */
@Entity
@Table(name="terminal_asset")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TerminalAsset {
    @Id
    @SequenceGenerator(name = "seq_terminal_asset", sequenceName = "seq_terminal_asset")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_terminal_asset")
    private Long id;

    @Enumerated
    private TerminalAssetType type;

    private int amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "terminalId")
    @JsonBackReference
    private Terminal terminal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TerminalAssetType getType() {
        return type;
    }

    public void setType(TerminalAssetType type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }
}
