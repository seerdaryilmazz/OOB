package ekol.crm.activity.util;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import ekol.crm.activity.domain.Activity;
import ekol.model.CodeNamePair;

public class Utils {

    public static String generateMongoAndCondition(String... subConditions) {
        return "{$and: [" + StringUtils.join(Arrays.asList(subConditions), ", ") + "]}";
    }

    public static String generateMongoOrCondition(String... subConditions) {
        return "{$or: [" + StringUtils.join(Arrays.asList(subConditions), ", ") + "]}";
    }

    // TODO: LocalDateDeserializer'ın içinde public static bir deserialize metodu olmalı ve
    // manual deserialize işlemlerinde bu metod kullanılmalı.
    public static LocalDate deserializeLocalDateStr(String str) {
        if (StringUtils.isNotBlank(str)) {
            return LocalDate.parse(str, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else {
            return null;
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ioe) {
                // ignore
            }
        }
    }

    public static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }

    public static void rollbackQuietly(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                // ignore
            }
        }
    }
    
    public static Timestamp convertInstantToTimestamp(Instant instant) {
        LocalDateTime localDateTime = instant.atZone(ZoneId.of("UTC")).toLocalDateTime();
        return Timestamp.valueOf(localDateTime);
    }

    public static List<String> stringifyServiceAreas(Activity activity) {
        List<String> serviceAreaStrings = new ArrayList<>();
        if (!CollectionUtils.isEmpty(activity.getServiceAreas())) {
            for (CodeNamePair serviceArea : activity.getServiceAreas()) {
                serviceAreaStrings.add(serviceArea.getCode() + "|" + serviceArea.getName());
            }
        }
        return serviceAreaStrings;
    }

    /**
     * Not: Bu metodun performansı konusunda bir gözlem/ölçüm yapmadık.
     * @param tableName DatabaseMetaData.getTables metodunun açıklamasından alıntı: 
     *                  tableNamePattern - a table name pattern; must match the table name as it is stored in the database
     *                  Mesela Oracle için tablo ismini büyük harf vermek gerekiyor. 
     *                  Garanti olsun diye doesTableExist metodu hem büyük harf tableName ile hem de küçük harf tableName ile çağırılabilir.
     */
    public static boolean doesTableExist(Connection connection, String tableName) throws SQLException {
		String[] tableNames = { tableName.toUpperCase(), tableName.toLowerCase() };
        DatabaseMetaData md = connection.getMetaData();
        for(int i = 0; i < tableNames.length; i++) {
        	ResultSet tables = md.getTables(null, null, tableNames[i], ArrayUtils.toArray("TABLE"));
        	if(tables.next()) {
        		return true;
        	}
        }
        return false;
    }
    
    public static boolean doesColumnExist(Connection connection, String tableName, String columName) throws SQLException {
    	String[] tableNames = { tableName.toUpperCase(), tableName.toLowerCase() };
    	String[] columNames = { columName.toUpperCase(), columName.toLowerCase() };
    	
    	DatabaseMetaData md = connection.getMetaData();
    	for(int t = 0; t < tableNames.length; t++) { 
    		for(int c = 0; c < columNames.length; c++) { 
    			ResultSet columns = md.getColumns(null, null, tableNames[t], columNames[c]);
    			if(columns.next()) {
            		return true;
            	}
    		}
    	}
    	return false;
    }
    
    public static String addColumnStatement(String tableName, String columName, String type) {
    	return "alter table " + tableName + " ADD " + columName + " " + type;
    }
    
    public static void setParameter(PreparedStatement ps, int parameterIndex, Long parameterValue) throws SQLException {
        if (parameterValue != null) {
            ps.setLong(parameterIndex, parameterValue);
        } else {
            ps.setNull(parameterIndex, Types.NUMERIC);
        }
    }

    public static void setParameter(PreparedStatement ps, int parameterIndex, String parameterValue) throws SQLException {
        if (StringUtils.isNotBlank(parameterValue)) {
            ps.setString(parameterIndex, parameterValue);
        } else {
            ps.setNull(parameterIndex, Types.VARCHAR);
        }
    }

    public static void setParameter(PreparedStatement ps, int parameterIndex, Timestamp parameterValue, Calendar calendar) throws SQLException {
        if (parameterValue != null) {
            ps.setTimestamp(parameterIndex, parameterValue, calendar);
        } else {
            ps.setNull(parameterIndex, Types.TIMESTAMP);
        }
    }
}
