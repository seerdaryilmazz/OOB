declare
  relationExists INTEGER;
begin
  select count(*) into relationExists from EMPLOYEE_CUSTOMER_RELATION where code = 'PORTFOLIO_OWNER';
  IF relationExists = 0 THEN
    insert into EMPLOYEE_CUSTOMER_RELATION values (SEQ_EMPLOYEECUSTOMERRELATION.nextval, 'PORTFOLIO_OWNER', 'Portfolio Owner', 1, null, null);
  end IF;
end;