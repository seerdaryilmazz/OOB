delete from zone_type;
insert into zone_type (id, code, name, deleted, last_updated, last_updated_by) values (seq_zone_type.nextval, 'PLANNING', 'Planning', 0, systimestamp, 'admin');
insert into zone_type (id, code, name, deleted, last_updated, last_updated_by) values (seq_zone_type.nextval, 'SALES', 'Sales', 0, systimestamp, 'admin');
