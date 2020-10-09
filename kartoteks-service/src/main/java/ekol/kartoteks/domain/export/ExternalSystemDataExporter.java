package ekol.kartoteks.domain.export;


import ekol.kartoteks.domain.CompanyExportQueue;

/**
 * Created by kilimci on 12/07/16.
 */
@FunctionalInterface
public interface ExternalSystemDataExporter {
    void exportCompany(CompanyExportQueue queue);
}
