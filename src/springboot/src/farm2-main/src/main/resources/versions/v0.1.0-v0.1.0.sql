INSERT INTO `alone_app_version` VALUES ('v0.1.0',  now(), 'USERNAME');
ALTER TABLE FARM2_AUTH_POST ADD FAMILYID varchar(32);
ALTER TABLE FARM2_AUTH_POST ADD GRADEID varchar(32);
ALTER TABLE FARM2_AUTH_POST ADD TRACKTYPE varchar(32);
ALTER TABLE FARM2_AUTH_POST ADD MAXUNUM int;
ALTER TABLE FARM2_AUTH_POST ADD ORGID varchar(32);
ALTER TABLE FARM2_LOCAL_USER ADD GRADEID varchar(32);
create table FARM2_AUTH_GRADE
(
   ID                   varchar(32) not null,
   NAME                 varchar(256) not null,
   KEYID                varchar(256),
   TRACKTYPE            varchar(32) comment 'MANAGER管理序列 / PROFESSIONAL专业序列',
   SORTCODE             int not null,
   MINSALARY            int,
   MAXSALARY            int,
   primary key (ID)
);
create table WHRS_ATTENDANCE_SUMMARY
(
   ID                   varchar(32) not null,
   CTIME                varchar(14) not null,
   USERKEY              varchar(64),
   ATTENDANCETIME       varchar(14) not null,
   WORKHOURS            float not null,
   LATEM                int,
   EARLYM               int,
   OVERTIMEM            int,
   ABSENTIS             varchar(1),
   EXCEPTIONTYPE        varchar(32) ,
   STATE                varchar(32) ,
   EXEMPTNOTE           varchar(256),
   WORKING              varchar(1) ,
   BACKUP               varchar(1),
   SSTIME               varchar(14),
   SXTIME               varchar(14),
   XSTIME               varchar(14),
   XXTIME               varchar(14),
   primary key (ID)
);
create table WHRS_SALARY_TEMPLATE
(
   ID                   varchar(32) not null,
   CUSER                varchar(32),
   CTIME                varchar(14) not null,
   STATE                varchar(2) not null,
   NOTE                 varchar(512),
   NAME                 varchar(256) not null,
   primary key (ID)
);

create table WHRS_SALARY_TEMPLATE_ITEM
(
   ID                   varchar(32) not null,
   NOTE                 varchar(512),
   NAME                 varchar(256) not null,
   DEFAULTVAL           decimal(18,2) not null,
   KEYCODE              varchar(32) not null,
   COMPONENTTYPE        varchar(32) comment '收入 / 扣款 / 税费 / 补贴',
   SOURCEMODEL          varchar(32) comment 'INPUT：输入',
   USEROVER             varchar(1) not null,
   SORTCODE             int not null,
   TEMPLATEID           varchar(32) not null,
   SHOWMODEL            varchar(1),
   primary key (ID)
);
create table WHRS_SALARY_TEMPLATE_FORMULA
(
   ID                   varchar(32) not null,
   NAME                 varchar(256),
   RULEVAL              varchar(512),
   VALNAME              varchar(32),
   VALCODE              varchar(32),
   TEMPLATEID           varchar(32),
   SHOWMODEL            varchar(1),
   SORTCODE             int,
   STEPCODE             int,
   NOTE                 varchar(512),
   primary key (ID)
);

create table WHRS_SALARY_USER
(
   ID                   varchar(32) not null,
   USERKEY              varchar(32) not null,
   USERNAME             varchar(32) not null,
   NOTE                 varchar(256),
   STATE                varchar(2) not null,
   TEMPLATEID           varchar(32),
   primary key (ID)
);




create table WHRS_SALARY_USER_BASE
(
   ID                   varchar(32) not null,
   NAME                 varchar(256) not null,
   KEYCODE              varchar(32) not null,
   VAL                  decimal(18,2) not null,
   USEROVER             varchar(1) not null comment '1手动更新，2月更新，0不更新',
   SHOWMODEL            varchar(1) not null,
   USERKEY              varchar(32) not null,
   USERNAME             varchar(32) not null,
   SALARYTIME           varchar(8) not null,
   primary key (ID)
);

create table WHRS_SALARY_USER_UNIT
(
   ID                   varchar(32) not null,
   SALARYTIME           varchar(8) not null,
   USERKEY              varchar(32) not null,
   USERNAME             varchar(32) not null,
   CTIME                varchar(14) not null,
   NOTE                 varchar(256),
   STATE                varchar(2) not null comment '0待计算 / 1已计算 ',
   GROSSPAY             decimal(18,2) not null comment '应发工资Gross Pay',
   NETPAY               decimal(18,2) not null comment '实发工资Net Pay',
   TAXAMOUNT            decimal(18,2) not null,
   primary key (ID)
);
create table WHRS_SALARY_USER_ITEM
(
   ID                   varchar(32) not null,
   NAME                 varchar(256) not null,
   KEYCODE              varchar(32) not null,
   CTIME                varchar(14) not null,
   VAL                  decimal(18,2) not null,
   SHOWMODEL            varchar(1) not null,
   SALARYTIME           varchar(8) not null,
   USERKEY              varchar(32) not null,
   USERNAME             varchar(32) not null,
   primary key (ID)
);

