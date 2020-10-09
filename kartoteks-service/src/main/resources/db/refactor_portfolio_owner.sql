declare
  relationId NUMBER(19,0);
begin
  select id into relationId from EMPLOYEE_CUSTOMER_RELATION where code = 'PORTFOLIO_OWNER';
  FOR companyEmpRel in (select COMPANY_ID, EMPLOYEE_ACCOUNT, COMPANY_EMPLOYEE_RELATION.ID COMPRELID from COMPANY_EMPLOYEE_RELATION, COMPANY_ROLE where COMPANY_ROLE.ID = COMPANY_EMPLOYEE_RELATION.COMPANY_ROLE_ID and RELATION_ID = relationId)
  LOOP
    update COMPANY set PORTFOLIO_OWNER = companyEmpRel.EMPLOYEE_ACCOUNT where id = companyEmpRel.COMPANY_ID;
    update COMPANY_EMPLOYEE_RELATION set deleted = 1 where ID = companyEmpRel.COMPRELID;
  END LOOP;
  update EMPLOYEE_CUSTOMER_RELATION set deleted = 1 where ID = relationId;
end;