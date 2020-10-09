package ekol.kartoteks.domain;

/**
 * Created by kilimci on 03/05/16.
 */
public enum RemoteApplication {
    QUADRO("quadroDataExporter"),
    SALESFORCE("salesforceDataExporter"),
    QLITE("qliteDataExporter");

    private String dataExporterBean;

    RemoteApplication(String dataExporterBean){
        this.dataExporterBean = dataExporterBean;
    }

    public String getDataExporterBean() {
        return dataExporterBean;
    }

}
