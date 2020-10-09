declare
begin
FOR loc in (select * from COMPANY_LOCATION where EORI_NUMBER is not null)
LOOP
  update company set EORI_NUMBER = loc.EORI_NUMBER where id = loc.COMPANY_ID;
END LOOP;
end;
