create table t_announce
(
	aid int auto_increment
		primary key,
	uid varchar(200) not null,
	title varchar(255) null,
	create_time datetime null,
	update_time datetime null,
	content longtext null
);

create table t_contest
(
	cid varchar(25) not null
		primary key,
	title varchar(255) not null,
	start_date date not null comment '比赛开始时间',
	create_date date not null comment '比赛创建时间
',
	end_date date not null comment '比赛结束时间',
	problems varchar(255) not null comment '比赛题目id列表',
	tags varchar(255) not null
);

create table t_problem
(
	pid varchar(32) default '' not null
		primary key,
	tags varchar(255) null comment '标签',
	uid varchar(255) null comment '创建题目用户的id',
	title varchar(255) null comment '标题',
	description varchar(255) null comment '问题描述',
	samples varchar(255) null comment '样例',
	hint longtext null comment '提示',
	languages varchar(255) null comment '允许使用的语言',
	create_time datetime null,
	update_time datetime null,
	time_limit int null comment '单位毫秒',
	memory_limit int null comment '单位MB',
	test_case_id varchar(32) null,
	level int default 0 null comment '0 easy 1 mid 2 hard',
	pass int default 0 null,
	total_submit int default 0 null,
	input_description varchar(255) null,
	output_description varchar(255) null
);

create table t_submission
(
	sid varchar(32) not null,
	pid int null comment '题目ID',
	create_time datetime null comment '创建/提交时间',
	uid varchar(32) null comment '用户ID',
	language varchar(32) null comment '语言',
	time_cost int null comment '单位ms',
	memory_cost int null comment '单位b',
	code longtext null comment '代码',
	result int null comment '评测结果 ',
	constraint t_submission_id_uindex
		unique (sid)
);

alter table t_submission
	add primary key (sid);

create table t_testcase
(
	id varchar(25) not null,
	input longtext null,
	output longtext null,
	constraint t_testcase_id_uindex
		unique (id)
);

alter table t_testcase
	add primary key (id);

create table t_user
(
	uid varchar(32) not null
		primary key,
	username varchar(255) not null,
	password varchar(255) not null,
	email varchar(255) null,
	tel char(11) null,
	level bigint default 1500 null,
	authority int default 0 null comment '0 普通用户
1 管理员
-1 封禁/被删除用户',
	avatar varchar(255) null,
	real_name varchar(255) null,
	deleted int default 1 not null
);

