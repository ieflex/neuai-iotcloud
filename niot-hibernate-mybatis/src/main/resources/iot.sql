DROP TABLE IF EXISTS "users";
CREATE TABLE "users" (
  "user_id" varchar(31)  NOT NULL,
  "user_name" varchar(64) ,
  "phone" varchar(20) ,
  "password" varchar(255) ,
  "email" varchar(128) ,
  "name" varchar(64) ,
  "signup_time" timestamp,
  "create_time" timestamp,
  "status" int2 DEFAULT 0,
  "detail" varchar 
);
ALTER TABLE "users" ADD CONSTRAINT "users_pkey" PRIMARY KEY ("user_id");

DROP TABLE IF EXISTS "device";
CREATE TABLE "device" (
  "device_id" varchar(31) NOT NULL,
  "user_id" varchar(31) ,
   "name" varchar(255) ,
  "type" varchar(255) ,
    "status" int2 DEFAULT 0,
  "create_time" timestamp,
  "credentials_type" varchar(255) ,
  "credentials_value" varchar ,
    "detail" varchar 
);
ALTER TABLE "device" ADD CONSTRAINT "device_pkey" PRIMARY KEY ("device_id");


DROP TABLE IF EXISTS "device_data";
CREATE TABLE "device_data" (
  "data_id" varchar(31)  NOT NULL,
  "device_id" varchar(31)  NOT NULL,
  "type" varchar(255)  NOT NULL,
  "key" varchar(255)  NOT NULL,
  "time_series" int8 NOT NULL,
  "bool_value" bool,
  "str_value" varchar(10000000) ,
  "int_value" bigint,
  "double_value" double precision
);
ALTER TABLE "device_data" ADD CONSTRAINT "device_data_pkey" PRIMARY KEY ("data_id","type",  "key", "device_id");