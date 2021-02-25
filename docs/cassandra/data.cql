CREATE KEYSPACE documents
WITH replication = {
	'class' : 'SimpleStrategy',
	'replication_factor' : 3
};

CREATE TABLE IF NOT EXISTS documents.templates(
    document_id text,
    document_kind text,
    lang text,
    start_date date,
    format text,
    engine text,
    body blob,
    PRIMARY KEY ((document_id, document_kind), lang, start_date)
);

CREATE TABLE IF NOT EXISTS documents.descriptors(
    command_id text,
    document_id text,
    document_kind text,
    lang text,
    descriptor text,
    PRIMARY KEY (command_id)
);

CREATE KEYSPACE document_generator
WITH replication = {
	'class' : 'SimpleStrategy',
	'replication_factor' : 3
};

CREATE TABLE IF NOT EXISTS document_generator.documents(
    cpid text,
    ocid text,
    pmd text,
    country text,
    lang text,
    documentInitiator text,
    documents text,
    objectId text,
    PRIMARY KEY (cpid, ocid, pmd, country, lang, documentInitiator)
);

CREATE TABLE IF NOT EXISTS document_generator.records(
    country text,
    pmd text,
    documentInitiator text,
    mainProcess text,
    relationships text,
    PRIMARY KEY (country, pmd, documentInitiator)
);

CREATE TABLE IF NOT EXISTS document_generator.parameter_paths(
    pmd text,
    documentInitiator text,
    parameter text,
    record text,
    path text,
    PRIMARY KEY (pmd, documentInitiator)
);

CREATE TABLE IF NOT EXISTS document_generator.templates(
    country text,
    pmd text,
    documentInitiator text,
    lang text,
    subGroup text,
    date timestamp,
    typeOfEngine text,
    format text,
    template blob,
    PRIMARY KEY (country, pmd, documentInitiator, lang, subGroup, date)
);