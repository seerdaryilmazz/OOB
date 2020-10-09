delete from zone_zip_code_type;
insert into zone_zip_code_type (id, code, name, deleted, last_updated, last_updated_by) values (seq_zone_zip_code_type.nextval, 'STARTSWITH', 'Starts with', 0, systimestamp, 'admin');
insert into zone_zip_code_type (id, code, name, deleted, last_updated, last_updated_by) values (seq_zone_zip_code_type.nextval, 'EQUALS', 'Equals', 0, systimestamp, 'admin');
