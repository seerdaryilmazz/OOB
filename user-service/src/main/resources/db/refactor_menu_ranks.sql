declare
r INTEGER;
begin
update UIMENU set rank = 1 where deleted = 1;
FOR parents in (select distinct parent_id from UIMENU where deleted = 0 and parent_id is not null)
LOOP
  r := 1;
  FOR menu in (select * from UIMENU where deleted = 0 and parent_id = parents.parent_id order by id)
  loop

      update UIMENU set rank = r where id = menu.id;
      r := r+1;

  end loop;
END LOOP;
r := 1;
FOR menu in (select * from UIMENU where deleted = 0 and parent_id is null order by id)
  loop

      update UIMENU set rank = r where id = menu.id;
      r := r+1;

  end loop;
end;