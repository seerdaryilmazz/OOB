delete from CUSTOMS_SERVICE_TYPE;
insert into CUSTOMS_SERVICE_TYPE (id, code, name, deleted) values (seq_customs_service_type.nextval,'NORMAL','Normal',0);
insert into CUSTOMS_SERVICE_TYPE (id, code, name, deleted) values (seq_customs_service_type.nextval,'FISCAL','Fiscal',0);
insert into CUSTOMS_SERVICE_TYPE (id, code, name, deleted) values (seq_customs_service_type.nextval,'ATA','ATA Numarası ile',0);

delete from customs_process_type;
insert into customs_process_type (id, code, name, deleted) values (seq_customs_process_type.nextval,'EKOL','Gümrükleme Ekol',0);
insert into customs_process_type (id, code, name, deleted) values (seq_customs_process_type.nextval,'MUSTERI','Gümrükleme Müşteri',0);
