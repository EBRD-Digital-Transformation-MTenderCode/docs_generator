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
	'replication_factor' : 1
};

/*----Old tables----*/

CREATE TABLE IF NOT EXISTS document_generator.templates_old(
    document_id text,
    document_kind text,
    lang text,
    start_date date,
    format text,
    engine text,
    body blob,
    PRIMARY KEY ((document_id, document_kind), lang, start_date)
);

CREATE TABLE IF NOT EXISTS document_generator.descriptors(
    command_id text,
    document_id text,
    document_kind text,
    lang text,
    descriptor text,
    PRIMARY KEY (command_id)
);

/*----New tables----*/

CREATE TABLE IF NOT EXISTS document_generator.documents(
    cpid text,
    ocid text,
    processInitiator text,
    objectId text,
    pmd text,
    country text,
    lang text,
    documents text,
    PRIMARY KEY (cpid, ocid, processInitiator, objectId)
);

CREATE TABLE IF NOT EXISTS document_generator.records(
    country text,
    pmd text,
    processInitiator text,
    mainProcess text,
    relationships text,
    PRIMARY KEY (country, pmd, processInitiator)
);

CREATE TABLE IF NOT EXISTS document_generator.parameter_paths(
    pmd text,
    processInitiator text,
    parameter text,
    record text,
    path text,
    PRIMARY KEY (pmd, processInitiator, parameter)
);

CREATE TABLE IF NOT EXISTS document_generator.templates(
    country text,
    pmd text,
    processInitiator text,
    lang text,
    subGroup text,
    date timestamp,
    typeOfEngine text,
    format text,
    template blob,
    PRIMARY KEY (country, pmd, processInitiator, lang, subGroup, date)
);