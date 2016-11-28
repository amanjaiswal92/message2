--liquibase formatted sql

--changeset jayant.mukherji:1
alter  table outbound_messages
modify payload varchar(30000);

alter  table inbound_messages
modify payload varchar(30000);

alter  table outbound_messages
modify created_at datetime default NOW();

alter  table inbound_messages
modify  created_at datetime default NOW();
