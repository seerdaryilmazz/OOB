declare

begin
  FOR companyRole in (select COMPANY_ID, SALES_PORTFOLIO_ID from COMPANY_ROLE where SALES_PORTFOLIO_ID is not null)
  LOOP
    update COMPANY set SALES_PORTFOLIO_ID = companyRole.SALES_PORTFOLIO_ID where id = companyRole.COMPANY_ID;
  END LOOP;
end;