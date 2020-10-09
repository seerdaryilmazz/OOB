insert into SECTOR values(seq_sector.nextval, 'Hizmet', null, 0, null, null, 'S001');
insert into SECTOR values(seq_sector.nextval, 'Perakende', null, 0, null, null, 'S002');
insert into SECTOR values(seq_sector.nextval, 'Endüstri', null, 0, null, null, 'S003');
insert into SECTOR values(seq_sector.nextval, 'Tekstil', null, 0, null, null, 'S004');
insert into SECTOR values(seq_sector.nextval, 'Otomotiv', null, 0, null, null, 'S005');
insert into SECTOR values(seq_sector.nextval, 'Kimya', null, 0, null, null, 'S006');
insert into SECTOR values(seq_sector.nextval, 'Sağlık & Kişisel Bakım', null, 0, null, null, 'S007');
insert into SECTOR values(seq_sector.nextval, 'Teknoloji Ürünleri', null, 0, null, null, 'S008');
insert into SECTOR values(seq_sector.nextval, 'Yapı & Dekorasyon', null, 0, null, null, 'S009');
insert into SECTOR values(seq_sector.nextval, 'FMCG', null, 0, null, null, 'S010');
insert into SECTOR values(seq_sector.nextval, 'E-Ticaret', null, 0, null, null, 'S012');
insert into SECTOR values(seq_sector.nextval, 'Enerji', null, 0, null, null, 'S013');
insert into SECTOR values(seq_sector.nextval, 'Diğer', null, 0, null, null, 'S099');

insert into SECTOR values(seq_sector.nextval, 'Enerji', (select id from sector where code = 'S013'), 0, null, null, 'S01301');

insert into SECTOR values(seq_sector.nextval, 'Alt Sektörü Bilinmiyor', (select id from sector where code = 'S012'), 0, null, null, 'S01202');
insert into SECTOR values(seq_sector.nextval, 'Dikey E-Ticaret Siteleri', (select id from sector where code = 'S012'), 0, null, null, 'S01204');
insert into SECTOR values(seq_sector.nextval, 'Yatay E-Ticaret Siteleri', (select id from sector where code = 'S012'), 0, null, null, 'S01203');

insert into SECTOR values(seq_sector.nextval, 'Gıda (Soğuk Zincire Tabi)', (select id from sector where code = 'S010'), 0, null, null, 'S01009');
insert into SECTOR values(seq_sector.nextval, 'Gıda- Taze &  Günlük Tüketim Ürünleri', (select id from sector where code = 'S010'), 0, null, null, 'S01010');
insert into SECTOR values(seq_sector.nextval, 'Alt Sektör Bilinmiyor', (select id from sector where code = 'S010'), 0, null, null, 'S01011');
insert into SECTOR values(seq_sector.nextval, 'Kişisel Bakım Ürünleri', (select id from sector where code = 'S010'), 0, null, null, 'S01012');
insert into SECTOR values(seq_sector.nextval, 'İçecekler', (select id from sector where code = 'S010'), 0, null, null, 'S01007');
insert into SECTOR values(seq_sector.nextval, 'Ev Bakım Ürünleri ( Home Care )', (select id from sector where code = 'S010'), 0, null, null, 'S01003');
insert into SECTOR values(seq_sector.nextval, 'Sigara & Tütün', (select id from sector where code = 'S010'), 0, null, null, 'S01002');
insert into SECTOR values(seq_sector.nextval, 'Besin Maddeleri', (select id from sector where code = 'S010'), 0, null, null, 'S01009');
insert into SECTOR values(seq_sector.nextval, 'Gıda Ürünleri', (select id from sector where code = 'S010'), 0, null, null, 'S01008');

insert into SECTOR values(seq_sector.nextval, 'Alt Sektör Bilinmiyor', (select id from sector where code = 'S009'), 0, null, null, 'S00905');
insert into SECTOR values(seq_sector.nextval, 'Cam ve Seramik Üreticileri', (select id from sector where code = 'S009'), 0, null, null, 'S00909');
insert into SECTOR values(seq_sector.nextval, 'Mermer Üreticileri', (select id from sector where code = 'S009'), 0, null, null, 'S00910');
insert into SECTOR values(seq_sector.nextval, 'Yapı & Dekorasyon', (select id from sector where code = 'S009'), 0, null, null, 'S00904');
insert into SECTOR values(seq_sector.nextval, 'İnşaat', (select id from sector where code = 'S009'), 0, null, null, 'S00902');
insert into SECTOR values(seq_sector.nextval, 'Mobilya ve Aksesuar Üreticileri', (select id from sector where code = 'S009'), 0, null, null, 'S00906');
insert into SECTOR values(seq_sector.nextval, 'Hırdavat & El Aletleri', (select id from sector where code = 'S009'), 0, null, null, 'S00907');
insert into SECTOR values(seq_sector.nextval, 'Yapı Malzemeleri Üreticisi', (select id from sector where code = 'S009'), 0, null, null, 'S00908');

insert into SECTOR values(seq_sector.nextval, 'Eletronik Teçhizat', (select id from sector where code = 'S008'), 0, null, null, 'S00804');
insert into SECTOR values(seq_sector.nextval, 'Otomasyon Kontrol', (select id from sector where code = 'S008'), 0, null, null, 'S00803');
insert into SECTOR values(seq_sector.nextval, 'Büro Makinaları', (select id from sector where code = 'S008'), 0, null, null, 'S00802');
insert into SECTOR values(seq_sector.nextval, 'Bilişim', (select id from sector where code = 'S008'), 0, null, null, 'S00805');
insert into SECTOR values(seq_sector.nextval, 'Alt Sektör Bilinmiyor', (select id from sector where code = 'S008'), 0, null, null, 'S00806');
insert into SECTOR values(seq_sector.nextval, 'Tüketici Elektroniği', (select id from sector where code = 'S008'), 0, null, null, 'S00801');

insert into SECTOR values(seq_sector.nextval, 'Alt Sektör Bilinmiyor', (select id from sector where code = 'S007'), 0, null, null, 'S00705');
insert into SECTOR values(seq_sector.nextval, 'Sağlık & Kişisel Bakım', (select id from sector where code = 'S007'), 0, null, null, 'S00704');
insert into SECTOR values(seq_sector.nextval, 'Kozmetik', (select id from sector where code = 'S007'), 0, null, null, 'S00702');
insert into SECTOR values(seq_sector.nextval, 'İlaç', (select id from sector where code = 'S007'), 0, null, null, 'S00701');
insert into SECTOR values(seq_sector.nextval, 'Medikal Gereçler', (select id from sector where code = 'S007'), 0, null, null, 'S00703');

insert into SECTOR values(seq_sector.nextval, 'Alt Sektör Bilinmiyor', (select id from sector where code = 'S006'), 0, null, null, 'S00606');
insert into SECTOR values(seq_sector.nextval, 'Petrol Ürünleri', (select id from sector where code = 'S006'), 0, null, null, 'S00602');
insert into SECTOR values(seq_sector.nextval, 'Kimyasal Maddeler', (select id from sector where code = 'S006'), 0, null, null, 'S00603');
insert into SECTOR values(seq_sector.nextval, 'Baskı Matbaa Ürünleri', (select id from sector where code = 'S006'), 0, null, null, 'S00601');
insert into SECTOR values(seq_sector.nextval, 'Plastik Hammaddeleri', (select id from sector where code = 'S006'), 0, null, null, 'S00605');
insert into SECTOR values(seq_sector.nextval, 'Organik Kimyasallar', (select id from sector where code = 'S006'), 0, null, null, 'S00604');

insert into SECTOR values(seq_sector.nextval, 'Ticari Araçlar', (select id from sector where code = 'S005'), 0, null, null, 'S00512');
insert into SECTOR values(seq_sector.nextval, 'Lastik', (select id from sector where code = 'S005'), 0, null, null, 'S00514');
insert into SECTOR values(seq_sector.nextval, 'Alt Sektör Bilinmiyor', (select id from sector where code = 'S005'), 0, null, null, 'S00509');
insert into SECTOR values(seq_sector.nextval, 'İş ve Tarım Makinaları', (select id from sector where code = 'S005'), 0, null, null, 'S00510');
insert into SECTOR values(seq_sector.nextval, 'Jant', (select id from sector where code = 'S005'), 0, null, null, 'S00515');
insert into SECTOR values(seq_sector.nextval, 'After Market (yedek parça)', (select id from sector where code = 'S005'), 0, null, null, 'S00503');
insert into SECTOR values(seq_sector.nextval, 'Yedek Parça', (select id from sector where code = 'S005'), 0, null, null, 'S00507');
insert into SECTOR values(seq_sector.nextval, 'Makina Yan Sanayii', (select id from sector where code = 'S005'), 0, null, null, 'S00505');
insert into SECTOR values(seq_sector.nextval, 'Otomobil', (select id from sector where code = 'S005'), 0, null, null, 'S00511');
insert into SECTOR values(seq_sector.nextval, 'Akü', (select id from sector where code = 'S005'), 0, null, null, 'S00513');

insert into SECTOR values(seq_sector.nextval, 'Ev Tekstili Sanayisi', (select id from sector where code = 'S004'), 0, null, null, 'S00414');
insert into SECTOR values(seq_sector.nextval, 'Alt Sektör Bilinmiyor', (select id from sector where code = 'S004'), 0, null, null, 'S00415');
insert into SECTOR values(seq_sector.nextval, 'Hazır Giyim Perakendeciliği', (select id from sector where code = 'S004'), 0, null, null, 'S00413');
insert into SECTOR values(seq_sector.nextval, 'Hazır Giyim üreticileri', (select id from sector where code = 'S004'), 0, null, null, 'S00408');
insert into SECTOR values(seq_sector.nextval, 'Tekstil Hammadde', (select id from sector where code = 'S004'), 0, null, null, 'S00411');

insert into SECTOR values(seq_sector.nextval, 'Bağlantı Elemanları', (select id from sector where code = 'S003'), 0, null, null, 'S00318');
insert into SECTOR values(seq_sector.nextval, 'Alt Sektör Bilinmiyor', (select id from sector where code = 'S003'), 0, null, null, 'S00319');
insert into SECTOR values(seq_sector.nextval, 'İmalat Sanayi Yedek Parça', (select id from sector where code = 'S003'), 0, null, null, 'S00316');
insert into SECTOR values(seq_sector.nextval, 'Su Sistemleri ve Yedek Parçaları', (select id from sector where code = 'S003'), 0, null, null, 'S00317');
insert into SECTOR values(seq_sector.nextval, 'Elektrikli El Aletleri', (select id from sector where code = 'S003'), 0, null, null, 'S00314');
insert into SECTOR values(seq_sector.nextval, 'Beyaz Eşya', (select id from sector where code = 'S003'), 0, null, null, 'S00310');
insert into SECTOR values(seq_sector.nextval, 'Havacılık ve Uzay Sanayii', (select id from sector where code = 'S003'), 0, null, null, 'S00305');
insert into SECTOR values(seq_sector.nextval, 'Isıtma & Soğutma Makinaları', (select id from sector where code = 'S003'), 0, null, null, 'S00309');
insert into SECTOR values(seq_sector.nextval, 'Savunma Sanayii', (select id from sector where code = 'S003'), 0, null, null, 'S00303');
insert into SECTOR values(seq_sector.nextval, 'Makina', (select id from sector where code = 'S003'), 0, null, null, 'S00301');
insert into SECTOR values(seq_sector.nextval, 'Endüstri', (select id from sector where code = 'S003'), 0, null, null, 'S00313');
insert into SECTOR values(seq_sector.nextval, 'Tır Sanayii', (select id from sector where code = 'S003'), 0, null, null, 'S00311');
insert into SECTOR values(seq_sector.nextval, 'Elektirikli Ev Aletleri', (select id from sector where code = 'S003'), 0, null, null, 'S00308');
insert into SECTOR values(seq_sector.nextval, 'Gemi Sanayii', (select id from sector where code = 'S003'), 0, null, null, 'S00307');
insert into SECTOR values(seq_sector.nextval, 'Yarım Mamüller', (select id from sector where code = 'S003'), 0, null, null, 'S00302');
insert into SECTOR values(seq_sector.nextval, 'Ambalaj', (select id from sector where code = 'S003'), 0, null, null, 'S00304');

insert into SECTOR values(seq_sector.nextval, 'Yapı Marketleri', (select id from sector where code = 'S002'), 0, null, null, 'S00206');
insert into SECTOR values(seq_sector.nextval, 'Zincir Mağazacılık', (select id from sector where code = 'S002'), 0, null, null, 'S00207');
insert into SECTOR values(seq_sector.nextval, 'Teknomarket', (select id from sector where code = 'S002'), 0, null, null, 'S00204');
insert into SECTOR values(seq_sector.nextval, 'Cosmoshop', (select id from sector where code = 'S002'), 0, null, null, 'S00205');
insert into SECTOR values(seq_sector.nextval, 'Alt Sektör Bilinmiyor', (select id from sector where code = 'S002'), 0, null, null, 'S00201');
insert into SECTOR values(seq_sector.nextval, 'Hazır Giyim Perakendeciliği', (select id from sector where code = 'S002'), 0, null, null, 'S00202');

insert into SECTOR values(seq_sector.nextval, 'Kitapevi', (select id from sector where code = 'S001'), 0, null, null, 'S00109');
insert into SECTOR values(seq_sector.nextval, 'Danışmanlık', (select id from sector where code = 'S001'), 0, null, null, 'S00101');
insert into SECTOR values(seq_sector.nextval, 'Basın ve Yayın', (select id from sector where code = 'S001'), 0, null, null, 'S00105');
insert into SECTOR values(seq_sector.nextval, 'Turizm', (select id from sector where code = 'S001'), 0, null, null, 'S00108');
insert into SECTOR values(seq_sector.nextval, 'Finansal Hizmetler', (select id from sector where code = 'S001'), 0, null, null, 'S00104');
insert into SECTOR values(seq_sector.nextval, 'Kamu', (select id from sector where code = 'S001'), 0, null, null, 'S00103');
insert into SECTOR values(seq_sector.nextval, 'Hizmet', (select id from sector where code = 'S001'), 0, null, null, 'S00110');
insert into SECTOR values(seq_sector.nextval, 'Lojistik', (select id from sector where code = 'S001'), 0, null, null, 'S00106');
insert into SECTOR values(seq_sector.nextval, 'Eğitim', (select id from sector where code = 'S001'), 0, null, null, 'S00107');
insert into SECTOR values(seq_sector.nextval, 'Banka', (select id from sector where code = 'S001'), 0, null, null, 'S00102');