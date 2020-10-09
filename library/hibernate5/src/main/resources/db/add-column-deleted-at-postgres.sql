do $$
declare
  dbOwner VARCHAR;
  tbl record;
  hasDeletedAt INTEGER;
  hasDeleted INTEGER;
begin
  SELECT LOWER('${DB_OWNER}') into dbOwner;
  FOR tbl in (SELECT tablename FROM pg_tables where tableowner = dbOwner and tablename not in('DATABASECHANGELOG', 'DATABASECHANGELOGLOCK', 'REVISION_INFO'))
  LOOP
  	SELECT count(*) into hasDeletedAt FROM information_schema.columns WHERE table_schema = dbOwner AND table_name = tbl.tablename and COLUMN_NAME = 'deleted_at';
  	SELECT count(*) into hasDeleted FROM information_schema.columns WHERE table_schema = dbOwner AND table_name = tbl.tablename and COLUMN_NAME = 'deleted';
  
    IF hasDeleted = 1 AND hasDeletedAt = 0 THEN
      execute 'ALTER TABLE "' || dbOwner || '".' || tbl.tablename || ' ADD DELETED_AT TIMESTAMP(6)';
    END IF;
  END LOOP;
end;
$$;