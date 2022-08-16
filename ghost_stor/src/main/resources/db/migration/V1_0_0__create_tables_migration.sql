create sequence hibernate_sequence start 1 increment 1;
create table company
(
    id   int8 not null,
    name varchar(255) not null,
    primary key (id)
);
create table company_role
(
    user_id       int8 not null,
    company_roles varchar(255)
);
create table data
(
    id                  int4 not null,
    okccode             varchar(255),
    okpdcode            varchar(255),
    adoption_date       varchar(255),
    changes             varchar(255),
    code_name           varchar(255),
    contents            varchar(255),
    developer           varchar(255),
    file_desc           varchar(255),
    filename            varchar(255),
    introduction_date   varchar(255),
    level_of_acceptance varchar(255),
    name                varchar(255),
    predecessor         varchar(255),
    references_amount   varchar(255),
    status              varchar(255),
    okccode_first_redaction             varchar(255),
    okpdcode_first_redaction            varchar(255),
    adoption_date_first_redaction       varchar(255),
    changes_first_redaction             varchar(255),
    code_name_first_redaction           varchar(255),
    contents_first_redaction            varchar(255),
    developer_first_redaction           varchar(255),
    file_desc_first_redaction           varchar(255),
    filename_first_redaction            varchar(255),
    introduction_date_first_redaction   varchar(255),
    level_of_acceptance_first_redaction varchar(255),
    name_first_redaction                varchar(255),
    predecessor_first_redaction         varchar(255),
    references_amount_first_redaction   varchar(255),
    status_first_redaction              varchar(255),
    okccode_second_redaction             varchar(255),
    okpdcode_second_redaction            varchar(255),
    adoption_date_second_redaction       varchar(255),
    changes_second_redaction             varchar(255),
    code_name_second_redaction           varchar(255),
    contents_second_redaction            varchar(255),
    developer_second_redaction           varchar(255),
    file_desc_second_redaction           varchar(255),
    filename_second_redaction            varchar(255),
    introduction_date_second_redaction   varchar(255),
    level_of_acceptance_second_redaction varchar(255),
    name_second_redaction                varchar(255),
    predecessor_second_redaction         varchar(255),
    references_amount_second_redaction   varchar(255),
    status_second_redaction              varchar(255),
    user_id             int8,
    primary key (id)
);
create table user_role
(
    user_id int8 not null,
    roles   varchar(255)
);
create table usr
(
    id              int8    not null,
    activation_code varchar(255),
    active          boolean not null,
    email           varchar(255) not null,
    password        varchar(255) not null,
    username        varchar(255) not null,
    company_id      int8,
    primary key (id)
);
alter table if exists company_role
    add constraint FK17m5ave3spct6gtr8sh1imxcp foreign key (user_id) references usr;
alter table if exists data
    add constraint FKlbca5nxa8xqrb7ieybvrxxuo3 foreign key (user_id) references usr;
alter table if exists user_role
    add constraint FKfpm8swft53ulq2hl11yplpr5 foreign key (user_id) references usr;
alter table if exists usr
    add constraint FK4yj0ffdl8cj1tgjsa3hfcw46o foreign key (company_id) references company;