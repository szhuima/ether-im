create table `ether-im`.im_message
(
    id              bigint      not null comment '消息ID'
        primary key,
    sender_id       varchar(32) not null comment '发送者ID',
    sender_terminal varchar(8)  not null comment '发送者终端',
    receiver_id     varchar(32) not null comment '接收者ID',
    receiver_type   int         not null comment '接收者类型,0-个人 1-群组',
    content         text        not null comment '消息内容',
    content_type    varchar(8)  not null comment '消息类型',
    status          varchar(8)  not null comment '消息状态  ',
    send_time       bigint      not null comment '发送时间',
    create_time     datetime    not null comment '创建时间'
)
    comment '消息表';



create table `ether-im`.im_group
(
    id          bigint auto_increment
        primary key,
    name        varchar(64)                           not null comment '群名称',
    remark      varchar(32) default ''                not null comment '群备注',
    owner_id    varchar(32)                           not null comment '群主',
    creator_id  varchar(32)                           not null comment '创建人ID',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '用户群' charset = utf8mb4;

create table `ether-im`.im_group_user
(
    group_id  varchar(32)                        not null comment '群ID',
    user_id   varchar(32)                        not null comment '用户ID',
    join_time datetime default CURRENT_TIMESTAMP not null comment '加入时间'
)
    comment '群和用户关系' charset = utf8mb4;


