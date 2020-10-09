insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'RES', 'Reservation', 'ORDER', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'STI', 'Service Type Instruction', 'ORDER', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'FRO', 'Freight Offer', 'ORDER', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'LOI', 'Loading Instruction', 'ORDER', 0, systimestamp, 'admin');

insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'INV', 'Invoice', 'TRANSPORTATION', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'ATR', 'ATR', 'TRANSPORTATION', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'EU1', 'EURO 1', 'TRANSPORTATION', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'EX1', 'EX 1', 'TRANSPORTATION', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'CMR', 'CMR', 'TRANSPORTATION', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'PAL', 'Packing List', 'TRANSPORTATION', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'COO', 'Certificate of Origin', 'TRANSPORTATION', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'WAB', 'Waybill', 'TRANSPORTATION', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'ATA', 'ATA Carnet', 'TRANSPORTATION', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'PRI', 'Proforma Invoice', 'TRANSPORTATION', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'DEN', 'Delivery Note', 'TRANSPORTATION', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'SUD', 'Supplier Declaration', 'TRANSPORTATION', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'VDA', 'VDA (Volkswagen)', 'TRANSPORTATION', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'BLR', 'BL (Renault)', 'TRANSPORTATION', 0, systimestamp, 'admin');

insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'HEC', 'Health Certificate', 'HEALTH_CERTIFICATE', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'CED', 'CED (Common Entry Document)', 'HEALTH_CERTIFICATE', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'AQD', 'Agricultural Quarantine Document', 'HEALTH_CERTIFICATE', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'PHC', 'Phytosanitary Certificate', 'HEALTH_CERTIFICATE', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'CVED', 'CVED (Common Veterinary Entry Document)', 'HEALTH_CERTIFICATE', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'COA', 'Certificate of Analysis', 'HEALTH_CERTIFICATE', 0, systimestamp, 'admin');

insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'MSD', '(MSDS) Material Safety Data Sheet', 'DANGEROUS_GOODS', 0, systimestamp, 'admin');
insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'IMO', '(IMO) Dangerous Goods Declaration', 'DANGEROUS_GOODS', 0, systimestamp, 'admin');

insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'AVI', 'AVIS', 'AGENT_DOCUMENTS', 0, systimestamp, 'admin');

insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'MAL', 'Mail', 'COMMUNICATION', 0, systimestamp, 'admin');

insert into DOCUMENT_TYPE (id, code, name, document_group, deleted, last_updated, last_updated_by) values
  (seq_document_type.nextval, 'OTH', 'Other', 'OTHER', 0, systimestamp, 'admin');
