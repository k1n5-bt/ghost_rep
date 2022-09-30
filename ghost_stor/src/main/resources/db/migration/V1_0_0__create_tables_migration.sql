create sequence hibernate_sequence start 1 increment 1;
create table data
(
    id                                  int4 not null,
    okccode                             varchar(255),
    okccode_first_redaction             varchar(255),
    okpdcode                            varchar(255),
    okpdcode_first_redaction            varchar(255),
    active_links                        varchar(255),
    active_links_first_redaction        varchar(255),
    adoption_date                       varchar(255),
    adoption_date_first_redaction       varchar(255),
    code_name                           varchar(255),
    contents                            varchar(255),
    contents_first_redaction            varchar(255),
    developer                           varchar(255),
    developer_first_redaction           varchar(255),
    file_desc                           varchar(255),
    file_desc_first_redaction           varchar(255),
    filename                            varchar(255),
    head_content                        varchar(255),
    head_content_first_redaction        varchar(255),
    inactive_links                      varchar(255),
    inactive_links_first_redaction      varchar(255),
    introduction_date                   varchar(255),
    introduction_date_first_redaction   varchar(255),
    key_phrases                         varchar(255),
    key_phrases_first_redaction         varchar(255),
    keywords                            varchar(255),
    keywords_first_redaction            varchar(255),
    level_of_acceptance                 varchar(255),
    level_of_acceptance_first_redaction varchar(255),
    modifications                       varchar(255),
    modifications_first_redaction       varchar(255),
    name                                varchar(255),
    name_first_redaction                varchar(255),
    predecessor                         varchar(255),
    predecessor_first_redaction         varchar(255),
    state_id                            int4,
    status                              varchar(255),
    status_first_redaction              varchar(255),
    user_id                             int8,
    primary key (id)
);

create table user_role
(
    user_id int8 not null,
    roles   varchar(255)
);

-- таблица с информацией какие госты ссылаются на данный
-- dataId - id данного госта
-- referralDataId - id госта с сылкой на данный
create table ghost_relation
(
    id               int4 not null,
    data_id          int4,
    referral_data_id int4,
    primary key (id)
);

create table usr
(
    id              int8    not null,
    activation_code varchar(255),
    active          boolean not null,
    company         varchar(255),
    division        varchar(255),
    email           varchar(255),
    field           varchar(255),
    name            varchar(255),
    password        varchar(255),
    patronymic      varchar(255),
    surname         varchar(255),
    username        varchar(255),
    primary key (id)
);

create table favorites
(
    data_id      int4 not null,
    favorites_id int8 not null
);

alter table if exists data
    add constraint FKlbca5nxa8xqrb7ieybvrxxuo3 foreign key (user_id) references usr;
alter table if exists favorites
    add constraint FKnlpo1843llbxhi5nl80gao0mu foreign key (favorites_id) references usr;
alter table if exists favorites
    add constraint FKamd4jwxn0g1sxq6jnoxoqgpqq foreign key (data_id) references data;
alter table if exists user_role
    add constraint FKfpm8swft53ulq2hl11yplpr5 foreign key (user_id) references usr;