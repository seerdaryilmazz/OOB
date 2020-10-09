delete from EQUIPMENT_TYPE;
insert into EQUIPMENT_TYPE (id, code, name, deleted, last_updated, last_updated_by) values (seq_equipment_type.nextval, 'SPANZET', 'SPANZET', 0, systimestamp, 'admin');
insert into EQUIPMENT_TYPE (id, code, name, deleted, last_updated, last_updated_by) values (seq_equipment_type.nextval, 'NONSKID', 'NONSKID', 0, systimestamp, 'admin');
