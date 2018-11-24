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
    mode text,
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