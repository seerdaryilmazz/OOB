delete from TRANSPORT_TYPE;
insert into TRANSPORT_TYPE (id, code, name, deleted, last_updated, last_updated_by) values (seq_transport_type.nextval, 'LAND', 'Karayolu Taşımacılığı', 0, systimestamp, 'admin');
insert into TRANSPORT_TYPE (id, code, name, deleted, last_updated, last_updated_by) values (seq_transport_type.nextval, 'SEA', 'Denizyolu Taşımacılığı', 0, systimestamp, 'admin');
insert into TRANSPORT_TYPE (id, code, name, deleted, last_updated, last_updated_by) values (seq_transport_type.nextval, 'TRAIN', 'Demiryolu Taşımacılığı', 0, systimestamp, 'admin');
insert into TRANSPORT_TYPE (id, code, name, deleted, last_updated, last_updated_by) values (seq_transport_type.nextval, 'BROAD', 'Sınır Kapısı', 0, systimestamp, 'admin');