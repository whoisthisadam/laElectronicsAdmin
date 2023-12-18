insert into roles (name)
values ('ROLE_USER_AUTHORIZED');

insert into discount (name, discount_percent, creation_date, modification_date, is_deleted)
values ('LOGIN_DISCOUNT', 5, CURRENT_TIMESTAMP(6),null, false);

insert into address (line_1, line_2, line_3, city, province, postcode, country)
values ('1', 'Main', 'Str', 'NYC', 'New York', '10001', 'US');

insert into users (login, password, first_name, last_name, mobile_phone, creation_date, modification_date,
                   is_deleted, email, discount_id, address_id, role_id)
values ('testLogin', 'testPassword1', 'Default','User','+1 (111) 892-1624', CURRENT_TIMESTAMP(6), null, false, 'testemail@gmail.com', 1, 1, 1);