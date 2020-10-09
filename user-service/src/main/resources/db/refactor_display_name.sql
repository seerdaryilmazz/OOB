declare
begin
FOR u in (select * from USERS)
LOOP
  update USERS set display_name = u.first_name || ' ' || u.last_name where id = u.id;
END LOOP;

end;