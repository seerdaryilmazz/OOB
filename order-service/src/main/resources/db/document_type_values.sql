delete from DOCUMENT_TYPE
insert into DOCUMENT_TYPE (id, code, name, deleted, last_updated, last_updated_by) values (seq_document_type.nextval, 'FIP', 'For Information Purposes', 0, systimestamp, 'admin')
insert into DOCUMENT_TYPE (id, code, name, deleted, last_updated, last_updated_by) values (seq_document_type.nextval, 'MBR', 'Must Be Read', 0, systimestamp, 'admin')
