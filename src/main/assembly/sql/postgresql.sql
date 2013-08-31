CREATE DATABASE fps WITH ENCODING='UTF8' OWNER=postgres;
COMMENT ON DATABASE fps IS 'Baza danych do serwera wydruku fiskalnego';

-- Table: slips

-- DROP TABLE slips;

CREATE TABLE slips
(
  cashboxlogo character(8) NOT NULL,
  externalreference character(16) NOT NULL,
  cashiername character varying NOT NULL,
  created_ts timestamp without time zone NOT NULL DEFAULT now(),
  printed_ts timestamp without time zone,
  id integer NOT NULL DEFAULT nextval('"SLIPS_ID_seq"'::regclass),
  stage integer NOT NULL DEFAULT 0,
  error character varying(120) DEFAULT 'OK'::character varying,
  CONSTRAINT "SLIPS_pkey" PRIMARY KEY (id),
  CONSTRAINT slips_externalreference_key UNIQUE (externalreference)
)
WITH (OIDS=FALSE);
ALTER TABLE slips OWNER TO postgres;

-- Table: sliplines

-- DROP TABLE sliplines;

CREATE TABLE sliplines
(
  fkid_slip integer NOT NULL,
  "name" character(40) NOT NULL,
  amount double precision NOT NULL,
  price double precision NOT NULL,
  taxrate character(5) NOT NULL,
  id integer NOT NULL DEFAULT nextval('"SLIPLINES_ID_seq"'::regclass),
  CONSTRAINT "SLIPLINES_pkey" PRIMARY KEY (id),
  CONSTRAINT "SLIPLINES_FKID_SLIP_fkey" FOREIGN KEY (fkid_slip)
      REFERENCES slips (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE sliplines OWNER TO postgres;

-- Table: slippayments

-- DROP TABLE slippayments;

CREATE TABLE slippayments
(
  fkid_slip integer NOT NULL,
  amount double precision NOT NULL,
  "name" character varying(16),
  form character(10) NOT NULL,
  id integer NOT NULL DEFAULT nextval('"SLIPPAYMENTS_ID_seq"'::regclass),
  CONSTRAINT "SLIPPAYMENTS_pkey" PRIMARY KEY (id),
  CONSTRAINT "SLIPPAYMENTS_FKID_SLIP_fkey" FOREIGN KEY (fkid_slip)
      REFERENCES slips (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE slippayments OWNER TO postgres;
