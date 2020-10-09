delete from PAYMENT_METHOD
insert into PAYMENT_METHOD (id, code, name, deleted) values (seq_payment_method.nextval,'CAD','Cash Against Documents',0)
insert into PAYMENT_METHOD (id, code, name, deleted) values (seq_payment_method.nextval,'CAG','Cash Against Goods',0)
insert into PAYMENT_METHOD (id, code, name, deleted) values (seq_payment_method.nextval,'L/C','Letter of Credit',0)
insert into PAYMENT_METHOD (id, code, name, deleted) values (seq_payment_method.nextval,'DP','Down Payment',0)
insert into PAYMENT_METHOD (id, code, name, deleted) values (seq_payment_method.nextval,'FREE','Free Of Charge',0)