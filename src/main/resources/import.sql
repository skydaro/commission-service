insert into clients (id, client_id, has_discount) values (gen_random_uuid(),42, true) ON CONFLICT DO NOTHING