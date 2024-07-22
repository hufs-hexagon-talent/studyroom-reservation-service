create table room_partition
(
    partition_id bigint auto_increment
        primary key,
    room_id      bigint not null,
    partition_number varchar(255) not null,
    constraint UK_room_partition
        unique (room_id, partition_number),
    constraint FK_room_partition_room
        foreign key (room_id) references room (room_id)
);

alter table reservation
    add column room_partition_id bigint null;

alter table reservation
drop constraint FK_reservation_room;

alter table reservation
    add constraint FK_reservation_partition
        foreign key (room_partition_id) references room_partition (partition_id);

-- RoomPartition 테이블에 데이터 삽입
insert into room_partition (room_id, partition_number)
select room_id, '1' from room;

-- 기존 데이터 업데이트
update reservation r
    join room_partition rp on r.room_id = rp.room_id
    set r.room_partition_id = rp.partition_id;

-- room_partition_id 필수 컬럼으로 설정
alter table reservation
    modify room_partition_id bigint not null;

-- room_id 컬럼 삭제
alter table reservation
drop column room_id;
