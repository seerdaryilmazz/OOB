declare
  portfolioOwnerExists INTEGER;
begin
  select count(*) into portfolioOwnerExists from EMPLOYEE_CUSTOMER_RELATION where code = 'PORTFOLIO_OWNER';

  IF portfolioOwnerExists = 0 THEN
    insert into EMPLOYEE_CUSTOMER_RELATION(ID,CODE,NAME,DELETED) values(SEQ_EMPLOYEECUSTOMERRELATION.nextval, 'PORTFOLIO_OWNER', 'Portfolio Owner', 0);
  END IF;

end;