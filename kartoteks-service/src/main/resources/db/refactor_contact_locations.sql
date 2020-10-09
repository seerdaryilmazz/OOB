declare
locationId NUMBER(19,0);
locationExists INTEGER;
begin

FOR contact in (select * from company_contact where location_name is not null and deleted = 0)
LOOP
  select count(*) into locationExists from company_location where company_id = contact.company_id and name = contact.location_name and deleted = 0;
  if locationExists >= 1 then
    select id into locationId from company_location where company_id = contact.company_id and name = contact.location_name and deleted = 0 and rownum = 1;
    update company_contact set company_location_id = locationId where id = contact.id;
  end if;
END LOOP;
end;
