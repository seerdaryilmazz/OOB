declare
  hasDeletedAt INTEGER;
  hasDeleted INTEGER;
begin
  FOR tbl in (SELECT owner, table_name FROM all_tables
  where owner = '${DB_OWNER}' and table_name not in('DATABASECHANGELOG', 'DATABASECHANGELOGLOCK', 'REVISION_INFO'))
  LOOP
    SELECT count(*) into hasDeletedAt FROM user_tab_cols WHERE table_name = tbl.TABLE_NAME and COLUMN_NAME = 'DELETED_AT';
    SELECT count(*) into hasDeleted FROM user_tab_cols WHERE table_name = tbl.TABLE_NAME and COLUMN_NAME = 'DELETED';
    IF hasDeleted = 1 AND hasDeletedAt = 0 THEN
      execute immediate 'ALTER TABLE ' || tbl.TABLE_NAME || ' ADD DELETED_AT TIMESTAMP(6)';
    END IF;
  END LOOP;
end;
